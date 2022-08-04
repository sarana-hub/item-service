package hello.itemservice.domain.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRepositoryTest {
    ItemRepository itemRepository = new ItemRepository();

    @AfterEach      //각 테스트 끝날때마다 실행됨
    void afterEach() {
        itemRepository.clearStore();    //각 테스트 끝날때마다, 데이터 지워주기
    }


    @Test
    void save() {
        //given
        Item item = new Item("itemA", 10000, 10);

        //when
        Item savedItem = itemRepository.save(item);

        //then
        Item findItem = itemRepository.findById(item.getId());

        assertThat(findItem).isEqualTo(savedItem);   //저장된 값과 조회된 값이 같다
    }


    @Test
    void findAll() {
        //given
        Item item1 = new Item("item1", 10000, 10);
        Item item2 = new Item("item2", 20000, 20);
        itemRepository.save(item1);
        itemRepository.save(item2);

        //when
        List<Item> result = itemRepository.findAll();   

        //then
        assertThat(result.size()).isEqualTo(2);     //size는 2개이다
        assertThat(result).contains(item1, item2);  //item1, item2를 포함한다
    }


    @Test
    void updateItem() {
        //given
        Item item = new Item("item1", 10000, 10);
        Item savedItem = itemRepository.save(item);
        Long itemId = savedItem.getId();

        //when
        Item updatePa = new Item("item2", 20000, 30);
        itemRepository.update(itemId, updatePa);

        //then
        Item findItem = itemRepository.findById(itemId);
        //업데이트된 값과 조회된 값이 같다
        assertThat(findItem.getItemName()).isEqualTo(updatePa.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updatePa.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(updatePa.getQuantity());
    }

}
