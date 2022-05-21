package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B") // SINGLE_TABLE 이니까 저장될 떄 구분할 수 있어야 하니까 디비가 분류하기 위해서 넣는 값. 설정안하면 클래스 이름으로 들어감.
@Setter
@Getter
public class Book extends Item{
    private String author;
    private String isbn;


}
