package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }
    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    public List<Order> findAllByString(OrderSearch orderSearch){

        String jpql = "select  o from Order o join o.member m";
        boolean isFirstCondition = false;

        // 주문 상태 검색
        if (orderSearch.getOrderStatus()!=null){
            if(isFirstCondition){
                jpql +="where";
                isFirstCondition = false;
            }else {
                jpql += "and";
            }
            jpql += "o.status = :status";
        }

        // 회원이름검색
        if (StringUtils.hasText(orderSearch.getMemberName())){
            if(isFirstCondition){
                jpql += "where";
                isFirstCondition = false;
            }else{
                jpql += "and";
            }
            jpql += "m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null){
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())){
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    // JPA Criteria
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery(){ // 패치조인
        return em.createQuery(
                "select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class)
                .getResultList();
    }

    public List<Order> findAllWithItem(){
        return em.createQuery(
                "select distinct o from Order o" + // distinct 증복제거 + 아이디 값이 같으면 중복이라고 판단, 하나만 반환함.
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch  oi.item i", Order.class)
                .setFirstResult(1)
                .setMaxResults(100)
                .getResultList();
        // 오더-멤버 걸어서 투원걸고 한 방 쿼리로 만들기
        // orderItems는 투원관계가 아니니까 손 대지 않고
        //
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                        "select o from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}

