package jpabook.jpashop.domain.item;

// 구현체를 가지고 진행할 거기 때문에 추상클래스로.

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
// Joined : 가장 정규화된 스타일 / SINGLE_TABLE : 테이블 하나에 다 떄려박는거 / TABLE_PER_CALSS : 테이블 나누는거
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter //@Setter setter로 값을 외부에서 변경할 게 아니라 이 안에서 메서드를 통해서 해결.
public abstract class Item {

    @Id @GeneratedValue
    @Column(name="item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // 비니지스 로직
    public void addStock(int quantity){
        this.stockQuantity=+quantity; // stock증가

    }
    public void removeStock(int quantity){
       int restStock = this.stockQuantity-quantity;
       if(restStock < 0){
           throw new NotEnoughStockException("need more stock");
       }
       this.stockQuantity = restStock;
    }
}
