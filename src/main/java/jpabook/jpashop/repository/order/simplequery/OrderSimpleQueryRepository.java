package jpabook.jpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;

// repostory는 최대한 엔티티만을 넣는 게 좋아서 따로 빼줌
@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos(){
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                        "from Order o" +
                        " join o.member m" +
                        " join  o.delivery d", OrderSimpleQueryDto.class).getResultList();
    }
}

// 엔티티로 조회하면 리포지토리 재사용성도 좋고 개발도 단순해짐.
// 우선 엔티티를 DTO로 변홚
// 필요하면 패치조인으로 성능 최적화 -> 대부분의 성능 이슈 해결
// 그래도 안되면 DTO 직접 조회 방법 사용
// 최후의 방법으로 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC 템플릿 사용해서 SQL 직접 사용