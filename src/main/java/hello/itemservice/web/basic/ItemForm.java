package hello.itemservice.web.basic;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**상품 저장용 폼*/

@Data
public class ItemForm {
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    //private List<MultipartFile> imageFiles;
}
