package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*상품 저장소*/

@Repository
public class ItemRepository {
    private static final Map<Long, Item> store=new HashMap<>();  
    private static  long sequence=0L;   

    public Item save(Item item){    //상품 저장 기능
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

    public void update(Long itemId, Item updateParam){      //업데이트      //파라미터 정보(이름,가격,수량)
        Item findItem=findById(itemId);
        findItem.setItemName(updateParam.getItemName());    
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore(){
        store.clear();
    }
}
