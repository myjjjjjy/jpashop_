package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.jni.Address;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String name;

    @Embedded // 내장 타입 명시
    private Address address;

    @JsonIgnore // 순수하게 회원 정보 api만 뿌리고 싶을 때 뿌리기 싫은 정보에 붙여주면 됨
    @OneToMany(mappedBy = "member") // 오더테이블에 있는 멤버필드에 의해 맵필될거야! 읽기 전용이 됨. 값을 여기에 넣어도 fk변경 안됨!
    private List<Order> orders = new ArrayList<>();


}
