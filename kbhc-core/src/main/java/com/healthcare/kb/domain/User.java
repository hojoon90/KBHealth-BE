package com.healthcare.kb.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "t_user")
@SQLDelete(sql = "UPDATE t_user SET deleted = true where user_no = ?")
@SQLRestriction("deleted = false")  //삭제가 아닌 유저만 조회하도록 조건처리
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickName;

    @ManyToMany(cascade=CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name="t_user_role",
            joinColumns={@JoinColumn(name="USER_NO", referencedColumnName="userNo")},
            inverseJoinColumns={@JoinColumn(name="ROLE_NO", referencedColumnName="roleNo")}
    )
    private List<Role> roles;


    @Builder
    public User(String email, String password, String name, String nickName, List<Role> roles) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.roles = roles;
    }
}
