package com.healthcare.kb.service;

import com.healthcare.kb.constant.MessageConst;
import com.healthcare.kb.domain.File;
import com.healthcare.kb.dto.FileDto;
import com.healthcare.kb.exception.NotFoundException;
import com.healthcare.kb.repository.FileRepository;
import com.healthcare.kb.type.BoardType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.healthcare.kb.constant.MessageConst.DATA_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    @Transactional
    public void saveFile(List<FileDto.Register> fileDtoList) {
        fileRepository.saveAll(fileDtoList.stream().map(FileDto.Register::toEntity).toList());
    }

    @Transactional
    public void deleteFile(Long fileNo){
        fileRepository.deleteById(fileNo);
    }

    public List<FileDto.FileInfo> findAllFileList(BoardType boardType, Long postNo) {
        List<File> entityList = fileRepository.findAllByPostNoAndBoardType(postNo, boardType)
                .orElse(Collections.emptyList());

        return entityList.stream()
                .map(FileDto.FileInfo::from)
                .toList();
    }

    public FileDto.FileInfo findFileInfo(Long fileNo){
        File file = fileRepository.findById(fileNo)
                .orElseThrow(() -> new NotFoundException(DATA_NOT_FOUND));

        return FileDto.FileInfo.from(file);
    }
}
