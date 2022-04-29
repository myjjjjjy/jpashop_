package jpabook.jpashop;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component // 자동으로 스프링빈에 등록
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    // 회원 저장
    public Long save(Member member){
        em.persist(member);
        return member.getId();
        // 커맨드와 쿼리 분리하기 위해 아이디만 리턴

    }
    // 회원 검색
    public Member find(Long id){
        return em.find(Member.class, id);
    }

}
