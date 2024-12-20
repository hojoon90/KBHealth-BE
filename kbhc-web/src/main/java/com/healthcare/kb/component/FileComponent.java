package com.healthcare.kb.component;

import com.healthcare.kb.dto.FileDto;
import com.healthcare.kb.service.FileService;
import com.healthcare.kb.type.BoardType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.healthcare.kb.constant.AppConst.*;

@Component
@RequiredArgsConstructor
public class FileComponent {

    private final FileService fileService;

    @Value("${post-file.path}")
    private String uploadPath;

    @Transactional
    public void registerFile(List<MultipartFile> fileList, Long postId, BoardType boardType) {
        final List<FileDto.Register> dtoList = fileList.stream()
                .filter(Objects::nonNull)
                .map(i -> uploadFile(i, postId, boardType))
                .toList();

        fileService.saveFile(dtoList);
    }

    /**
     * 단일 파일 업로드
     * @param file - 파일 객체
     * @return DB에 저장할 파일 정보
     */
    public FileDto.Register uploadFile(final MultipartFile file, Long postNo, BoardType boardType) {

        String saveName = generateSaveFilename(file.getOriginalFilename());
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern(YYYY_MM_DD));
        String finalUploadPath = getUploadPath(today) + File.separator + saveName;
        File uploadFile = new File(finalUploadPath);

        FileDto.Register fileDto = FileDto.Register.builder()
                .postNo(postNo)
                .boardType(boardType)
                .saveName(generateSaveFilename(file.getOriginalFilename()))
                .originName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .build();

        try {
            // https://stackoverflow.com/questions/60336929/java-nio-file-nosuchfileexception-when-file-transferto-is-called
            file.transferTo(uploadFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileDto;
    }

    /**
     * 저장 파일명 생성
     * @param filename 원본 파일명
     * @return 디스크에 저장할 파일명
     */
    private String generateSaveFilename(final String filename) {
        String uuid = UUID.randomUUID().toString().replaceAll(DASH, "");
        String extension = StringUtils.getFilenameExtension(filename);
        return uuid + DOT + extension;
    }

    /**
     * 업로드 경로 반환
     * @param addPath - 추가 경로
     * @return 업로드 경로
     */
    private String getUploadPath(final String addPath) {
        return makeDirectories(uploadPath + File.separator + addPath);
    }

    /**
     * 업로드 폴더(디렉터리) 생성
     * @param path - 업로드 경로
     * @return 업로드 경로
     */
    private String makeDirectories(final String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath();
    }



}

