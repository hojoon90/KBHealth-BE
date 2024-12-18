package com.healthcare.kb.domain;

import com.healthcare.kb.type.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "t_role")
@SQLDelete(sql = "UPDATE t_role SET deleted = true where board_no = ?")
@SQLRestriction("deleted = false")  //삭제가 아닌 유저만 조회하도록 조건처리
@NoArgsConstructor(access = PROTECTED)
public class Role extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleNo;

    @Column
    @Convert(converter = RoleType.Converter.class)
    private RoleType roleType;

    @ManyToMany(mappedBy="roles")
    private List<User> users;
}
