package jpabook.jpashop.api;

import jpabook.jpashop.controller.Valid;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        // 회원등록 api 만들어보자
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);

        // 엔티티 = api가 매핑되어 있는데 아주 위험!!! 엔티티를 외부에 노출 하는 것도 위험!! api를 위한 dto를 따로 파라미터값으로 받아야함
    }
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    // 수정
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request){

        memberService.update(id, request.getName()); // 수정은 변경감지!

        // 업데이트에서 그냥 @Transaction public Member update(Long id, String name) 으로 member 반환해도 되는데
        // 업데이트 하면서 멤버를 커맨드와 쿼리가 같이 되게 됨. 그러니까 그냥 id 값 정도만 반환하는 게 좋음 -> 유지보수에 좋음.
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(); // Lombok에서 AllArgsConstructor 썼기 때문에 모두 반환하는 파라미터 필요함

    }

    // 조회
    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
        // 이 방법으로 하면 안됨!
        // 엔티티 직접 노출하게 되면 회원 정보 뿐만 아니라 모든 정보들이 외부에 노출됨 => @JsonIgnore
        // 하지만, 다른 api를 만들게 되면 엔티티 안에 @JsonIgnore로 모두 해결할 수 없음..  의존관계 깨지고 수정하기 어려워짐
        // => api 응답 스펙에 맞춰서 별도의 dto 반환해야함
    }

    @GetMapping("api/v2/members")
    public Result memberV2(){
        List<Member> findMembers = memberService.findMembers();
        // memberDto로 변환해야함
        List<MemberDto> collect = findMembers.stream().map(
                m->new MemberDto(m.getName())).collect(Collectors.toList());
        // 필요한 것만 노출하는 게 좋음!!
        return new Result(collect);
    }

    // 안에서만 쓸거니까 여기에 만들기
    // 등록
    @Data
    static class CreateMemberRequest{
        private String name;
    }
    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    // 수정
    @Data
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }
    @Data
    static class UpdateMemberRequest{
        private String name;

    }

    // 조회
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }
    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;

    }

}
