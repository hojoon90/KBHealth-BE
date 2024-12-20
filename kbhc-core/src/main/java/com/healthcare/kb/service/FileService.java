package com.healthcare.kb.service;

import com.healthcare.kb.domain.File;
import com.healthcare.kb.dto.FileDto;
import com.healthcare.kb.repository.FileRepository;
import com.healthcare.kb.type.BoardType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public void saveFile(List<FileDto.Register> fileDtoList) {
        fileRepository.saveAll(fileDtoList.stream().map(FileDto.Register::toEntity).toList());
    }

    public List<FileDto.FileInfo> findAllFileList(Long postNo, BoardType boardType) {
        List<File> entityList = fileRepository.findAllByPostNoAndBoardType(postNo, boardType)
                .orElse(Collections.emptyList());

        return entityList.stream()
                .map(FileDto.FileInfo::from)
                .toList();
    }
}
