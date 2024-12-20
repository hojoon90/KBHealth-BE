package com.healthcare.kb.facade;

import com.healthcare.kb.component.FileComponent;
import com.healthcare.kb.dto.AppResponse;
import com.healthcare.kb.dto.AppUserDetails;
import com.healthcare.kb.dto.BoardDto;
import com.healthcare.kb.dto.board.QnaBoardDto;
import com.healthcare.kb.dto.request.QnaBoardRequest;
import com.healthcare.kb.dto.response.QnaBoardResponse;
import com.healthcare.kb.service.board.QnaBoardService;
import com.healthcare.kb.type.BoardType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaBoardFacade {

    private final QnaBoardService qnaBoardService;
    private final FileComponent fileComponent;

    /**
     * 게시글 등록
     * @param request
     * @param userDetails
     * @param fileList
     * @return
     */
    public AppResponse<Void> registQnaPost(QnaBoardRequest.Regist request,
                                           AppUserDetails userDetails,
                                           List<MultipartFile> fileList) {
        Long userNo = userDetails.getUserNo();

        QnaBoardDto.QnaPostInfo dto = QnaBoardDto.QnaPostInfo.builder()
                .title(request.getTitle())
                .contents(request.getContents())
                .createdBy(userNo)
                .build();


        //파일 저장 처리
        Long postNo = qnaBoardService.savePostData(dto);
        if(!CollectionUtils.isEmpty(fileList)){
            fileComponent.registerFile(fileList, postNo, BoardType.QNA);
        }

        return AppResponse.responseVoidSuccess(HttpStatus.CREATED.value());
    }

    /**
     * 게시글 수정
     * @param qnaNo
     * @param request
     * @param userDetails
     * @return
     */
    public AppResponse<Void> updateQnaPost(Long qnaNo, QnaBoardRequest.Regist request, AppUserDetails userDetails) {
        Long userNo = userDetails.getUserNo();

        QnaBoardDto.QnaPostInfo dto = QnaBoardDto.QnaPostInfo.builder()
                .title(request.getTitle())
                .contents(request.getContents())
                .createdBy(userNo)
                .build();


        qnaBoardService.updatePostData(qnaNo, dto);
        return AppResponse.responseVoidSuccess(HttpStatus.OK.value());

    }

    /**
     * 게시글 삭제
     * @param qnaNo
     * @param userDetails
     * @return
     */
    public AppResponse<Void> deleteQnaPost(Long qnaNo, AppUserDetails userDetails){
        Long userNo = userDetails.getUserNo();
        qnaBoardService.deletePostData(qnaNo, userNo);
        return AppResponse.responseVoidSuccess(HttpStatus.NO_CONTENT.value());

    }

    /**
     * 게시글 조회
     * @param qnaNo
     * @return
     */
    public AppResponse<QnaBoardResponse.QnaDetail> selectQnaPost(Long qnaNo){
        final BoardDto.PostDetail qnaPost = qnaBoardService.findPostData(qnaNo);

        return AppResponse.responseSuccess(QnaBoardResponse.QnaDetail.from(qnaPost));
    }

    /**
     * 게시글 리스트 조회
     * @param request
     * @return
     */
    public AppResponse<QnaBoardResponse.QnaBoardPages> selectQnaPostList(QnaBoardRequest.PageablePostSearchRequest request){
        BoardDto.SearchCreteria creteria = BoardDto.SearchCreteria.builder()
                .title(request.getTitle())
                .build();

        Page<QnaBoardDto.QnaPostDetail> qnaSummaries = qnaBoardService.findPostDataList(creteria, request.getPageRequest());

        final QnaBoardResponse.QnaBoardPages page = QnaBoardResponse.QnaBoardPages.of(qnaSummaries.getTotalElements(), qnaSummaries.getTotalPages()
                , qnaSummaries.getPageable().getPageSize(), qnaSummaries.getContent());

        return AppResponse.responseSuccess(page);
    }

    /**
     * 답글 등록
     * @param request
     * @param userDetails
     * @return
     */
    public AppResponse<Void> registQnaPostComment(QnaBoardRequest.CommentRegist request,
                                                  AppUserDetails userDetails) {
        Long userNo = userDetails.getUserNo();

        QnaBoardDto.QnaCommentRegist dto = QnaBoardDto.QnaCommentRegist.builder()
                .postNo(request.getPostNo())
                .contents(request.getContents())
                .createdBy(userNo)
                .build();

        qnaBoardService.savePostCommentData(dto);
        return AppResponse.responseVoidSuccess(HttpStatus.CREATED.value());
    }

    /**
     * 답글 수정
     * @param commentNo
     * @param request
     * @param userDetails
     * @return
     */
    public AppResponse<Void> updateQnaPostComment(Long commentNo, QnaBoardRequest.CommentRegist request, AppUserDetails userDetails) {
        Long userNo = userDetails.getUserNo();

        QnaBoardDto.QnaCommentRegist dto = QnaBoardDto.QnaCommentRegist.builder()
                .postNo(request.getPostNo())
                .contents(request.getContents())
                .createdBy(userNo)
                .build();

        qnaBoardService.updatePostCommentData(commentNo, dto);
        return AppResponse.responseVoidSuccess(HttpStatus.OK.value());

    }

    /**
     * 답글 삭제
     * @param qnaNo
     * @param userDetails
     * @return
     */
    public AppResponse<Void> deleteQnaPostComment(Long qnaNo, AppUserDetails userDetails){
        Long userNo = userDetails.getUserNo();
        qnaBoardService.deletePostCommentData(qnaNo, userNo);
        return AppResponse.responseVoidSuccess(HttpStatus.NO_CONTENT.value());

    }


}
