package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional
    // 테스트폴더에 Transactional있으면 테스트 진행하고 바로 데이터 롤백해버림. h2에서 데이터 확인 불가. => @Rollback(false)해주면 롤백 안됨
    @Rollback(false)
    public void testMember() throws Exception{
        // given
        Member member = new Member();
        member.setUsername("memberA");

        // when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        // then
        // 트랜젝션이 없어서 오류남! 엔티티 매니저를 통한 모든 데이터 변경은 모두 트랜젝션 안에서 해결해야함! => annotation 명시!
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        //
        Assertions.assertThat(findMember).isEqualTo(member); // true. 같은 트랜젝션 안에서 저장하고 조회하면 id값이 같아서 같은 데이터로 조회됨.

    }

}