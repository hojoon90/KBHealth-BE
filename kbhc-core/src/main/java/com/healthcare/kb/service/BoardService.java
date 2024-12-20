package com.healthcare.kb.service;

import com.healthcare.kb.dto.BoardDto;
import com.healthcare.kb.type.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 추후 확장 고려를 위한 인터페이스화
 */
public interface BoardService<T extends BoardDto.PostDetail>{

    boolean isValidService(BoardType boardType);
    Long savePostData(BoardDto.PostInfo dto);
    void updatePostData(Long postNo, BoardDto.PostInfo dto);
    void deletePostData(Long postNo, Long userNo);
    T findPostData(Long qnaNo);
    Page<T> findPostDataList(BoardDto.SearchCreteria criteria, Pageable pageable);
    Long savePostCommentData(BoardDto.CommentRegist dto);
    void updatePostCommentData(Long commentNo, BoardDto.CommentRegist dto);
    void deletePostCommentData(Long commentNo, Long userNo);


}
