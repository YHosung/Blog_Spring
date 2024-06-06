package com.test.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.test.project.entity.MemberEntity;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    default Optional<MemberEntity> findByMemberEmail() {
        return findByMemberEmail(null);
    }

    Optional<MemberEntity> findByMemberEmail(String email);

    @Transactional
    void deleteByMemberEmail(String email);
}
