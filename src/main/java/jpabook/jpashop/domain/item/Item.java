package jpabook.jpashop.domain.item;

// 구현체를 가지고 진행할 거기 때문에 추상클래스로.

import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
// Joined : 가장 정규화된 스타일 / SINGLE_TABLE : 테이블 하나에 다 떄려박는거 / TABLE_PER_CALSS : 테이블 나누는거
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name="item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    private List<Category> categories = new ArrayList<>();
}
