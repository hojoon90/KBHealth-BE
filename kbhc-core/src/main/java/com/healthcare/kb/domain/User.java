package com.healthcare.kb.domain;

import com.healthcare.kb.dto.UserDto;
import com.healthcare.kb.type.RoleActionType;
import com.healthcare.kb.type.RoleType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;
import java.util.Set;

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
    private Set<Role> roles;

    public void updateRole(UserDto.UserRole dto, Role role){
        if(dto.getRoleAction().equals(RoleActionType.ADD)) this.roles.add(role);
        if(dto.getRoleAction().equals(RoleActionType.REMOVE)) this.roles.remove(role);
    }

    public void updateDefaultRole(Role role){
        this.roles.add(role);
    }

    @Builder
    public User(String email, String password, String name, String nickName, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.roles = roles;
    }
}
