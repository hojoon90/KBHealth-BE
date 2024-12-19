package com.healthcare.kb.controller;

import com.healthcare.kb.dto.AppResponse;
import com.healthcare.kb.dto.AppUserDetails;
import com.healthcare.kb.dto.request.QnaBoardRequest;
import com.healthcare.kb.dto.response.QnaBoardResponse;
import com.healthcare.kb.facade.QnaBoardFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kbhc/qna")
public class QnaBoardController {

    private final QnaBoardFacade qnaBoardFacade;

    @PostMapping
    public ResponseEntity<AppResponse<Void>> registQnaPost(
            @RequestPart @Valid final QnaBoardRequest.Regist request,
            @AuthenticationPrincipal AppUserDetails userDetails,
            @RequestPart("file") final List<MultipartFile> fileList
    ){
        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(qnaBoardFacade.registQnaPost(request, userDetails.getUserNo(), fileList));

    }

    @PutMapping("/{qnaNo}")
    public ResponseEntity<AppResponse<Void>> updateQnaPost(
            @PathVariable Long qnaNo,
            @RequestPart @Valid final QnaBoardRequest.Regist request,
            @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(qnaBoardFacade.updateQnaPost());
    }

    @DeleteMapping("/{qnaNo}")
    public ResponseEntity<AppResponse<Void>> deleteQnaPost(
            @PathVariable Long qnaNo,
            @AuthenticationPrincipal AppUserDetails userDetails
    ){
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(qnaBoardFacade.deleteQnaPost());
    }

    @DeleteMapping("/{qnaNo}")
    public ResponseEntity<AppResponse<QnaBoardResponse.QnaDetail>> selectQnaPost(
            @PathVariable Long qnaNo,
            @AuthenticationPrincipal AppUserDetails userDetails
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(qnaBoardFacade.selectQnaPost());
    }



}

