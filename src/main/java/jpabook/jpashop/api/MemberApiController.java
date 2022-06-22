package jpabook.jpashop.api;

import jpabook.jpashop.controller.Valid;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/memberZ")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        // 회원등록 api 만들어보자
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);

        // 엔티티 = api가 매핑되어 있는데 아주 위험!!! 엔티티를 외부에 노출 하는 것도 위험!! api를 위한 dto를 따로 파라미터값으로 받아야함
    }
    @PostMapping("/api/v2/memberZ")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

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

}
