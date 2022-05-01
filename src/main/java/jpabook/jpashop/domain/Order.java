package jpabook.jpashop.domain;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태. [order, cancel]
}

// fk 업데이트는 하나만 하기로 약속!! 객체는 변경포인트는 두군데인데 테이블은 fk 함나만 설정하면 됨.
// Orders 와 Member 중에 어떤걸로 업데이트 할 지 명시를 해주면 됨. (연관관계 설정->가까운 곳)
// 현재 Orders가 주인. 아닌 Member클래스에는 (mappedBy="") 적어주기.