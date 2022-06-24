package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        // 그냥 가져오는 방법 (추천 안함!)
        // 양방향에 걸리는 애들은 다 @JsonIgnore 붙여줘야함
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        // force lazy loading 옵션 끄고 강제로 lazy 로딩 하기
        for (Order order : all){
            order.getMember().getName();
            order.getDelivery().getAddress();
        }
        return all;
    }

    @GetMapping("api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2() {
        // n+1 문제
     List<Order> orders = orderRepository.findAllByString(new OrderSearch());
     List<SimpleOrderDto> result = orders.stream().map(o->new SimpleOrderDto(o)).collect(Collectors.toList());

     return result;
    }

    @GetMapping("api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3(){ // 패치조인 정확하게 이해하기! 성능 최적화에 도움이 많이 됨 !!!!
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream().map(o->new SimpleOrderDto(o)).collect(Collectors.toList());

        return result;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderData;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderData = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
