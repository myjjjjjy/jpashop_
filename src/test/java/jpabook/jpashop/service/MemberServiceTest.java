package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // 기본적으로 롤백이 돼서 insert쿼리 안보임.
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    @Rollback(false)
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("jo");

        //when
        Long saveId = memberService.join(member);

        //then
        em.flush();
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class) // 이거 작성해줘서 하단에 try~catch문 삭제.
    public void 중복회원조회() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("jo");

        Member member2 = new Member();
        member2.setName("jo");

        //when
        memberService.join(member1);
        memberService.join(member2);
//        try {
//            memberService.join(member2);
//        }catch (IllegalStateException e){
//            return;
//        }
        //then
        fail("예외 발생 해야 함!"); // 코드 돌다가 뭔가 잘못되면 fail로 빠짐.
    }

}