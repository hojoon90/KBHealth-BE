package com.healthcare.kb.controller;

import com.healthcare.kb.dto.AppResponse;
import com.healthcare.kb.dto.request.FileRequest;
import com.healthcare.kb.dto.response.FileResponse;
import com.healthcare.kb.facade.FileFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@RestController
@RequiredArgsConstructor
@RequestMapping("/kbhc/files")
public class FileController {

    private final FileFacade fileFacade;

    //파일 삭제
    @DeleteMapping("/{fileNo}")
    public ResponseEntity<AppResponse<Void>> deleteFile(
            @PathVariable Long fileNo,
            @RequestBody FileRequest.FileInfo request
    ){
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(fileFacade.deleteFile(fileNo, request));
    }

    //파일리스트 조회
    @GetMapping("/{boardType}/post/{postNo}")
    public ResponseEntity<AppResponse<FileResponse.FileInfoList>> getFlieList(
            @PathVariable String boardType,
            @PathVariable Long postNo
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(fileFacade.findFileInfoList(boardType, postNo));
    }

    //다운로드
    @GetMapping("/{fileNo}/download")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable final Long fileNo
    ) {
        FileResponse.FileDownload resource = fileFacade.findFileInfo(fileNo);
        String filename = URLEncoder.encode(resource.getFileName(), StandardCharsets.UTF_8);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + filename + "\";")
                .header(HttpHeaders.CONTENT_LENGTH, resource.getFileSize() + "")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource.getResource());
    }
}
