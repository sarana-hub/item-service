package hello.itemservice.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter @Setter
//@Data //핵심 도메인 모델에 사용하기에는 매우 위험
public class Item {

    private Long id;
    private String itemName;
    //null일 경우 포함하기 위해 Integer
    private Integer price;
    private Integer quantity;
    //private List<UploadFile> imageFiles;


    public Item() {
    }
    //public Item(String itemName, Integer price, Integer quantity, List<UploadFile> imageFiles) {
    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        //this.imageFiles = imageFiles;
    }

}
