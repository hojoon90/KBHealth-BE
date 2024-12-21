package com.healthcare.kb.component;

import com.healthcare.kb.dto.FileDto;
import com.healthcare.kb.exception.NotFoundException;
import com.healthcare.kb.type.BoardType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.healthcare.kb.constant.AppConst.*;
import static com.healthcare.kb.constant.MessageConst.FILE_NOT_FOUND;

@Slf4j
@Component
public class FileComponent {

    @Value("${post-file.path}")
    private String uploadPath;

    /**
     * 단일 파일 업로드
     * @param file
     * @return
     */
    public FileDto.Register uploadFile(final MultipartFile file, Long postNo, BoardType boardType) {

        String saveName = generateSaveFilename(file.getOriginalFilename());
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern(YYYY_MM_DD));
        String finalUploadPath = getUploadPath(today) + File.separator + saveName;
        File uploadFile = new File(finalUploadPath);

        FileDto.Register fileDto = FileDto.Register.builder()
                .postNo(postNo)
                .boardType(boardType)
                .saveName(saveName)
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
     * @param filename
     * @return
     */
    public String generateSaveFilename(final String filename) {
        String uuid = UUID.randomUUID().toString().replaceAll(DASH, "");
        String extension = StringUtils.getFilenameExtension(filename);
        return uuid + DOT + extension;
    }

    /**
     * 업로드 경로 반환
     * @param addPath
     * @return
     */
    public String getUploadPath(final String addPath) {
        return makeDirectories(uploadPath + File.separator + addPath);
    }

    /**
     * 업로드 폴더(디렉터리) 생성
     * @param path
     * @return
     */
    public String makeDirectories(final String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    /**
     * 파일 삭제
     * @param addPath
     * @param filename
     */
    public void deleteFile(final String addPath, final String filename) {
        String filePath = Paths.get(uploadPath, addPath, filename).toString();
        deleteFile(filePath);
    }

    /**
     * 파일 삭제
     * @param filePath
     */
    private void deleteFile(final String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 다운로드
     * @param file - 첨부파일 상세정보
     * @return
     */
    public Resource readFileAsResource(final FileDto.FileInfo file) {
        String uploadedDate = file.getCreatedAt().toLocalDate().format(DateTimeFormatter.ofPattern(YYYY_MM_DD));
        String filename = file.getFileName();
        Path filePath = Paths.get(uploadPath, uploadedDate, filename);

        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isFile()) {
                log.error("File not found Exception. resource.exist(): {}, resource.isFile(): {}",
                        resource.exists(), resource.isFile());
                throw new NotFoundException(FILE_NOT_FOUND);
            }
            return resource;
        } catch (MalformedURLException e) {
            log.error("File not found : {}", filePath);
            throw new NotFoundException(FILE_NOT_FOUND);
        }
    }
}

