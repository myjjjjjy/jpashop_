package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 데이터 변경 할 때는 꼭 명시해야함.
@RequiredArgsConstructor // final가지고 있는 필드만 생성자 만들어줌! -> 이거 썼으니까 아래 생성자 생성 코드 삭제.
public class MemberService {

    private final MemberRepository memberRepository; // 필드는 파이널로 해주는 걸 권장. -> 컴파일 시점에 체크 할 수 있음.

//    @Autowired // setter 인젝션. 필드는 주입하기 좀 까다로운데 메소드로 진행하니까 테스트 돌릴 때 좋음.
//    public void setMemberRepository(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }

//    @Autowired // 생성자 인젝션. 한번 생성할 때 완성이 되니까 중간에 set으로 변경 안됨. 멤버 서비스 개발할 떄 의존관계 명확하게 파악할 수 있음.
//                // 생성자 하나일 땐 자동으로 생성자 인젝션 됨. @Autowired 명시 안해도됨.
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    // 회원가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member); // 중복회원검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member){ // 멤버 수를 세서 그 멤버가 0보다 크면 문제가 있다. 이런식으로 하는 게 더 최적화
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
    // 회원 전체조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name){
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
