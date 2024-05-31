package com.hchoaf.jpashop.service;

import com.hchoaf.jpashop.entity.Member;
import com.hchoaf.jpashop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
    private void validateDuplicateMember(Member member) {
        List<Member> existingMembers = memberRepository.findByName(member.getName());
        if (!existingMembers.isEmpty()) {
            throw new IllegalStateException("Member already exists");
        }
    }
}
