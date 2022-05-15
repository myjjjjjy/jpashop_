package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository // 스프링 사용하는 거니까 스프링빈으로 자동관리됨.
@RequiredArgsConstructor
public class MemberRepository {

    @PersistenceContext // jpa 사용하기 때문에 jpa가 제공하는 표준 어노테이션 명시.
    private final EntityManager em; // 스프링이 엔티티매니저를 만들어서 주입해줌. 아래는 이제 저장로직 짜면됨.

//    @PersistenceUnit // 엔티티매니저팩토리를 직접 주입하고 싶을 때 사용.
//    private EntityManagerFactory emf;

    public void save(Member member){
        em.persist(member);
    }
    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();

    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name", name).getResultList();
    }

}
