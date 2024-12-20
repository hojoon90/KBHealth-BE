package com.healthcare.kb.controller;

import com.healthcare.kb.dto.AppResponse;
import com.healthcare.kb.dto.AppUserDetails;
import com.healthcare.kb.dto.request.QnaBoardRequest;
import com.healthcare.kb.dto.response.QnaBoardResponse;
import com.healthcare.kb.facade.QnaBoardFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
            @RequestPart(name = "file", required = false) final List<MultipartFile> fileList
    ){
        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(qnaBoardFacade.registQnaPost(request, userDetails, fileList));

    }

    @PutMapping("/{qnaNo}")
    public ResponseEntity<AppResponse<Void>> updateQnaPost(
            @PathVariable Long qnaNo,
            @RequestPart @Valid final QnaBoardRequest.Regist request,
            @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(qnaBoardFacade.updateQnaPost(qnaNo, request, userDetails));
    }

    @DeleteMapping("/{qnaNo}")
    public ResponseEntity<AppResponse<Void>> deleteQnaPost(
            @PathVariable Long qnaNo,
            @AuthenticationPrincipal AppUserDetails userDetails
    ){
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(qnaBoardFacade.deleteQnaPost(qnaNo, userDetails));
    }

    @GetMapping("/{qnaNo}")
    public ResponseEntity<AppResponse<QnaBoardResponse.QnaDetail>> selectQnaPost(
            @PathVariable Long qnaNo,
            @AuthenticationPrincipal AppUserDetails userDetails
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(qnaBoardFacade.selectQnaPost(qnaNo));
    }

    @GetMapping("/list")
    public ResponseEntity<AppResponse<QnaBoardResponse.QnaBoardPages>> selectQnaPostList(
            @Valid final QnaBoardRequest.PageablePostSearchRequest request,
            @AuthenticationPrincipal AppUserDetails userDetails
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(qnaBoardFacade.selectQnaPostList(request));
    }

    @PostMapping("/comment")
    public ResponseEntity<AppResponse<Void>> registQnaPostComment(
            @RequestPart @Valid final QnaBoardRequest.CommentRegist request,
            @AuthenticationPrincipal AppUserDetails userDetails
    ){
        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(qnaBoardFacade.registQnaPostComment(request, userDetails));

    }

    @PutMapping("/comment/{commentNo}")
    public ResponseEntity<AppResponse<Void>> updateQnaPostComment(
            @PathVariable Long commentNo,
            @RequestPart @Valid final QnaBoardRequest.CommentRegist request,
            @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(qnaBoardFacade.updateQnaPostComment(commentNo, request, userDetails));
    }

    @DeleteMapping("/comment/{commentNo}")
    public ResponseEntity<AppResponse<Void>> deleteQnaPostComment(
            @PathVariable Long commentNo,
            @AuthenticationPrincipal AppUserDetails userDetails
    ){
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(qnaBoardFacade.deleteQnaPostComment(commentNo, userDetails));
    }

}

