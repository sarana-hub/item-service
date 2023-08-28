package hello.itemservice.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {
    private static final Map<Long, Item> store=new HashMap<>();
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
