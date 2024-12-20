package com.healthcare.kb.domain;


import com.healthcare.kb.type.BoardType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Table(name = "t_file")
@SQLRestriction("deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileNo;

    @Column(nullable = false)
    private Long postNo;

    @Column(nullable = false)
    @Convert(converter = BoardType.Converter.class)
    private BoardType boardType;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String saveName;

    @Column(nullable = false)
    private long fileSize;

    @Builder
    public File(Long postNo, BoardType boardType, String originName, String saveName, long fileSize) {
        this.postNo = postNo;
        this.boardType = boardType;
        this.originName = originName;
        this.saveName = saveName;
        this.fileSize = fileSize;
    }
}
