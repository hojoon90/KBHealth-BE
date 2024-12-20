package com.healthcare.kb.repository;

import com.healthcare.kb.domain.File;
import com.healthcare.kb.type.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    Optional<List<File>> findAllByPostNoAndBoardType(@Param("postNo") Long postNo,
                                                     @Param("boardType") BoardType boardType);

}