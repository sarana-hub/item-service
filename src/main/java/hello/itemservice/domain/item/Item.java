package hello.itemservice.domain.item;

import lombok.Getter;
import lombok.Setter;


/*상품 객체*/

@Getter @Setter
public class Item {
    private Long id;
    private String itemName;
    //null일 경우 포함하기 위해 Integer
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    //id 제외한 생성자
    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
