package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.apache.tomcat.jni.Address;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Fail.fail;

// 좋은 테스트라고 볼 수는 없음. 순수하게 메서드만 테스트 하는 게 좋음. 
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
    
    @Autowired EntityManager em; 
    @Autowired OrderService orderService; 
    @Autowired OrderRepository orderRepository;
    
    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember();

        Book book = createBook("사회계약론", 10000, 10);

        int orderCount = 1; 
        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        
        //then
        Order getOrder = orderRepository.findOne(orderId);

        Assert.assertEquals("상품 주문 시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        Assert.assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        Assert.assertEquals("주문 가격은 가격 * 수량이다.", 10000*orderCount, getOrder.getTotalPrice());
        Assert.assertEquals("주문 수량만큼 재고가 줄어야 한다.", 9, book.getStockQuantity());
        
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception{
        // given
        Member member = createMember();
        Item item = createBook("사회계약론", 10000, 10);

        int orderCount = 10;

        //when
        orderService.order(member.getId(), item.getId(), orderCount);

        //then
        fail("재고 수량 부족 예외 발생해야함.");
    }
    
    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Book item = createBook("사회계약론", 10000, 10);

        int orderCount = 1;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancleOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        Assert.assertEquals("주문 취소 시 상태는 CANCEL 이다.", OrderStatus.CANCEL, getOrder.getStatus());
        Assert.assertEquals("주문이 취소된 상품은 그 만큼 재고가 증가해야 한다.", 10, item.getStockQuantity());

    }

    // item쪽에 Setter이노테니션이 뭔가 적용이 안되는 것 같다. 다시 확인해보기!
    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("이주연");
        //member.setAddress(new Address("광주시", "초월읍", "123-5"));
        em.persist(member);
        return member;
    }

}
