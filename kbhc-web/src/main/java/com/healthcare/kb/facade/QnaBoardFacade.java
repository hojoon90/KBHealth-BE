package com.healthcare.kb.facade;

import com.healthcare.kb.dto.AppResponse;
import com.healthcare.kb.dto.AppUserDetails;
import com.healthcare.kb.dto.QnaBoardDto;
import com.healthcare.kb.dto.request.QnaBoardRequest;
import com.healthcare.kb.dto.response.QnaBoardResponse;
import com.healthcare.kb.service.board.QnaBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaBoardFacade {

    private final QnaBoardService qnaBoardService;

    public AppResponse<Void> registQnaPost(QnaBoardRequest.Regist request,
                                           AppUserDetails userDetails,
                                           List<MultipartFile> fileList) {
        Long userNo = userDetails.getUserNo();

        QnaBoardDto.QnaRegist dto = QnaBoardDto.QnaRegist.builder()
                .title(request.getTitle())
                .contents(request.getContents())
                .createdBy(userNo)
                .build();

        qnaBoardService.saveQnaPost(dto);
        return AppResponse.responseVoidSuccess(HttpStatus.CREATED.value());
    }

    public AppResponse<Void> updateQnaPost(Long qnaNo, QnaBoardRequest.Regist request, AppUserDetails userDetails) {
        Long userNo = userDetails.getUserNo();

        QnaBoardDto.QnaRegist dto = QnaBoardDto.QnaRegist.builder()
                .title(request.getTitle())
                .contents(request.getContents())
                .createdBy(userNo)
                .build();


        qnaBoardService.updateQnaPost(qnaNo, dto);
        return AppResponse.responseVoidSuccess(HttpStatus.OK.value());

    }

    public AppResponse<Void> deleteQnaPost(Long qnaNo, AppUserDetails userDetails){
        Long userNo = userDetails.getUserNo();
        qnaBoardService.deleteQnaPost(qnaNo, userNo);
        return AppResponse.responseVoidSuccess(HttpStatus.NO_CONTENT.value());

    }

    public AppResponse<QnaBoardResponse.QnaDetail> selectQnaPost(Long qnaNo){
        final QnaBoardDto.QnaDetail qnaPost = qnaBoardService.findQnaPost(qnaNo);
        return AppResponse.responseSuccess(QnaBoardResponse.QnaDetail.from(qnaPost));
    }

    public AppResponse<QnaBoardResponse.QnaBoardPages> selectQnaPostList(QnaBoardRequest.PageablePostSearchRequest request){
        QnaBoardDto.SearchCreteria creteria = QnaBoardDto.SearchCreteria.builder()
                .title(request.getTitle())
                .build();

        Page<QnaBoardDto.QnaSummary> qnaSummaries = qnaBoardService.selectPostList(creteria, request.getPageRequest());

        final QnaBoardResponse.QnaBoardPages page = QnaBoardResponse.QnaBoardPages.of(qnaSummaries.getTotalElements(), qnaSummaries.getTotalPages()
                , qnaSummaries.getPageable().getPageSize(), qnaSummaries.getContent());

        return AppResponse.responseSuccess(page);
    }



}
