package hello.itemservice.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/*상품 객체*/

@Getter @Setter
@Data
public class Item {
    private Long id;
    private String itemName;
    //null일 경우 포함하기 위해 Integer
    private Integer price;
    private Integer quantity;

    private List<UploadFile> imageFiles;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity, List<UploadFile> imageFiles) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.imageFiles = imageFiles;
    }

}
