package com.healthcare.kb.repository;

import com.healthcare.kb.domain.Role;
import com.healthcare.kb.type.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(@Param("roleType") RoleType roleType);
}
