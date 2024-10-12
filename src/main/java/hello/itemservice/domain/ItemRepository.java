package hello.itemservice.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    /* HashMap은 동시성 문제가 고려되어 있지 않음 (실무에서는 ConcurrentHashMap 사용 고려) */
    private static final Map<Long, Item> store=new HashMap<>();
    /* 실무에서는 AtomicLong 사용 고려 */
    private static long sequence=0L;

    public Item save(Item item){    //저장
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id){      //조회
        return store.get(id);
    }

    public List<Item> findAll(){        //전체 조회
        return new ArrayList<>(store.values());
    }


    //큰 프로젝트에서는, 업데이트할 요소(이름,가격,양)만 들어있는 클래스(객체) 따로 만들기
    public void update(Long itemId, Item updateParam){
        Item findItem=findById(itemId);

        //파라미터 정보 업데이트
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
        //findItem.setImageFiles(updateParam.getImageFiles());
    }

    public void clearStore(){
        store.clear();
    }

}
