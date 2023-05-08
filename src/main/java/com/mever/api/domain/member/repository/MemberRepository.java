package com.mever.api.domain.member.repository;

import com.mever.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    public boolean existsByEmailAndPassword(String email, String password);
    public Optional<Member> findByEmail(String email);
    public Member findByName(String name);
}