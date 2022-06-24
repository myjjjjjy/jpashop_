package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 직접 생성하면 안되고 메서드를 이용하겠구나! 하고 알 수 있음.
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    /* cascade : order와 orderItem 각자 저장해줘야 하는데
     persist(oderItemA) persist(oderItemB) persist(oderItemC) persist(order)
     이런식으로 엔티티 당 하나씩 persist 호출해야 하는데 cascade두면 persist(order)만 해주고 아이템 각각 하나씩은 안해줘도됨.
     */

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    // 이 경우도 cascade 없으면 persist(order), persist(delivery)도 각각 해줘야 함.

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태. [order, cancel]

    // 연관관계 편입 메서드. 양방향일 때 쓰면 좋음. 양쪽에서 세팅해줘야 하는 걸 한 코드로 연결.
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
//    public static void main(String[] args){
//        Member member = new Member();
//        Order order = new Order();

//        member.getOrders().add(order);
//        order.setMember(member);
//    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }
    // 생성메서드
    // 핵심 비지니스 로직 구현하기. 여러개의 연관관계 들어가야 하니까 별도의 생성메서드가 있으면 좋음.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 비지니스 로직
    // 주문취소
    public void cancel(){
        if (delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    // 조회 로직
    // 전체주문가격 조회
    public int getTotalPrice(){
        return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum(); // option+enter sum 어쩌구로 축약.
    }
}


// fk 업데이트는 하나만 하기로 약속!! 객체는 변경포인트는 두군데인데 테이블은 fk 함나만 설정하면 됨.
// Orders 와 Member 중에 어떤걸로 업데이트 할 지 명시를 해주면 됨. (연관관계 설정->가까운 곳)
// 현재 Orders가 주인. 아닌 Member클래스에는 (mappedBy="") 적어주기.