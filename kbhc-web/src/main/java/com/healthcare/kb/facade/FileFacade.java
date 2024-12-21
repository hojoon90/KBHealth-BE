package com.healthcare.kb.facade;

import com.healthcare.kb.component.FileComponent;
import com.healthcare.kb.dto.AppResponse;
import com.healthcare.kb.dto.FileDto;
import com.healthcare.kb.dto.request.FileRequest;
import com.healthcare.kb.dto.response.FileResponse;
import com.healthcare.kb.service.FileService;
import com.healthcare.kb.type.BoardType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static com.healthcare.kb.constant.AppConst.YYYY_MM_DD;

@Service
@RequiredArgsConstructor
public class FileFacade {

    private final FileComponent fileComponent;
    private final FileService fileService;

    @Transactional
    public void registerFile(List<MultipartFile> fileList, BoardType boardType, Long postId) {
        final List<FileDto.Register> dtoList = fileList.stream()
                .filter(Objects::nonNull)
                .map(i -> fileComponent.uploadFile(i, postId, boardType))
                .toList();

        fileService.saveFile(dtoList);
    }

    @Transactional
    public AppResponse<Void> deleteFile(Long fileNo, FileRequest.FileInfo request){
        String addPath = request.getCreatedAt().toLocalDate().format(DateTimeFormatter.ofPattern(YYYY_MM_DD));
        //db 파일 삭제
        fileService.deleteFile(fileNo);
        //로컬 파일 삭제
        fileComponent.deleteFile(addPath, request.getFileName());

        return AppResponse.responseVoidSuccess(HttpStatus.NO_CONTENT.value());
    }


    public AppResponse<FileResponse.FileInfoList> findFileInfoList(String boardTypeStr, Long postNo) {
        BoardType boardType = BoardType.valueOf(boardTypeStr);
        List<FileDto.FileInfo> fileInfoList = fileService.findAllFileList(boardType, postNo);

        final FileResponse.FileInfoList response = FileResponse.FileInfoList.from(postNo, boardType, fileInfoList);
        return AppResponse.responseSuccess(response);
    }

    public FileResponse.FileDownload findFileInfo(Long fileNo){

        FileDto.FileInfo fileInfo = fileService.findFileInfo(fileNo);
        Resource resource = fileComponent.readFileAsResource(fileInfo);

        return FileResponse.FileDownload.builder()
                .fileName(fileInfo.getOriginName())
                .fileSize(fileInfo.getFileSize())
                .resource(resource)
                .build();
    }
}
