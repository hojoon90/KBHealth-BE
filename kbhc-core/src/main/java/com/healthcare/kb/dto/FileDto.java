package com.healthcare.kb.dto;

import com.healthcare.kb.domain.File;
import com.healthcare.kb.type.BoardType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileDto {

    @Getter
    @Builder
    public static class Register{
        private long postNo;
        private BoardType boardType;
        private String originName;
        private String saveName;
        private long fileSize;

        public File toEntity(){
            return File.builder()
                    .postNo(postNo)
                    .boardType(boardType)
                    .originName(originName)
                    .saveName(saveName)
                    .fileSize(fileSize)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class FileInfo{
        private Long fileNo;
        private Long postNo;
        private BoardType boardType;
        private String fileName;

        public static FileInfo from(File entity) {
            return FileInfo.builder()
                    .fileNo(entity.getFileNo())
                    .postNo(entity.getPostNo())
                    .boardType(entity.getBoardType())
                    .fileName(entity.getSaveName())
                    .build();
        }
    }

}
