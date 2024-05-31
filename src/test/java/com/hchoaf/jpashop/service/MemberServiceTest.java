package com.hchoaf.jpashop.service;

import com.hchoaf.jpashop.entity.Member;
import com.hchoaf.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    // @Rollback(value = false)
    public void registerMemberTest() throws Exception {
        Member member = new Member();
        member.setName("Hangsun");

        Long savedId = memberService.join(member);

        assertEquals(memberService.findOne(savedId), member);
    }

    @Test
    public void validateDuplicateMemberTest() throws Exception {
        Member member = new Member();
        member.setName("Hangsun");

        Long savedId = memberService.join(member);

        Member member2 = new Member();
        member2.setName("Hangsun");

        IllegalStateException exception =
                assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertTrue(exception.getMessage().contains("Member already exist"));
    }
}