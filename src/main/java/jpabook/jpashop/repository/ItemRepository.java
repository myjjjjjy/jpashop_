package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item){
        if (item.getId() == null){ // getId == null > 새로 생성된 객체.
            em.persist(item); // 새로 값 넣어주기.
        }else{
            em.merge(item);  // 업데이트와 비슷한 기능. 디비 값을 가져와서 변경.
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }
    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
