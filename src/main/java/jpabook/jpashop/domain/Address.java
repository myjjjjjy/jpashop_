package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable // 어딘가에 내장될 수 있음 // Address에 @Embedded 와 둘 중 하나만 써도 되는데 보통 둘 다 명시해줌.
@Getter @Setter// 실무에서는 거의 Getter만 쓰고 Setter는 꼭 필요한 경우에만!
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address(){
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}