package com.healthcare.kb.facade;

import com.healthcare.kb.dto.AppResponse;
import com.healthcare.kb.dto.QnaBoardDto;
import com.healthcare.kb.dto.request.QnaBoardRequest;
import com.healthcare.kb.dto.response.QnaBoardResponse;
import com.healthcare.kb.service.QnaBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaBoardFacade {

    private final QnaBoardService qnaBoardService;

    public AppResponse<Void> registQnaPost(QnaBoardRequest.Regist request,
                                           Long userNo,
                                           List<MultipartFile> fileList){

        QnaBoardDto.QnaRegist dto = QnaBoardDto.QnaRegist.builder()
                .title(request.getTitle())
                .contents(request.getContents())
                .createdBy(userNo)
                .build();

        qnaBoardService.saveQnaPost(dto);
        return AppResponse.responseVoidSuccess(HttpStatus.CREATED.value());
    }

    public AppResponse<Void> updateQnaPost(){

        return AppResponse.responseVoidSuccess(HttpStatus.OK.value());

    }

    public AppResponse<Void> deleteQnaPost(){
        return AppResponse.responseVoidSuccess(HttpStatus.NO_CONTENT.value());

    }

    public AppResponse<QnaBoardResponse.QnaDetail> selectQnaPost(){
        return AppResponse.responseSuccess(null);

    }



}
