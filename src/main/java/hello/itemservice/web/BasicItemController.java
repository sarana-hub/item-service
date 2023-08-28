package hello.itemservice.web;

import hello.itemservice.domain.UploadFile;
import hello.itemservice.domain.Item;
import hello.itemservice.domain.ItemRepository;
import hello.itemservice.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/* 컨트롤러 - 폼 뷰를 호출 */


@Slf4j
@Controller
//@RequestMapping("/items")
@RequiredArgsConstructor           //final이 붙은 멤버변수만 사용해서 생성자를 자동 생성
public class BasicItemController {

    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    /** 상품 목록 */
    @GetMapping("/items")
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();    //모든 item 조회
        model.addAttribute("items", items);  //items(모든 item)을 모델에 담는다
        return "items";   //뷰 템플릿 호출
    }

    /** 상품 상세(조회) */
    @GetMapping("/items/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);    //PathVariable로 넘어온 itemId로 item 조회
        model.addAttribute("item", item);
        return "item";
    }

    /** 상품 등록 폼 */
    @GetMapping("/items/add")
    public String addForm() {   //단순히 뷰 템플릿만 호출
        return "addForm";
    }

    @PostMapping("/items/add")
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

        //데이터베이스에 저장
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());
        //item.setImageFiles(storeImageFiles);
        itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", item.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/items/{itemId}";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }


    /** 상품 수정 */
    @GetMapping("/items/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "editForm";    //수정용 폼 뷰를 호출
    }
    /** 상품 수정 처리
    @PostMapping("/items/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute ItemForm form,
                       RedirectAttributes redirectAttributes) throws IOException {
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

        Item item = itemRepository.findById(itemId);
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());

        if(!storeImageFiles.isEmpty()) {
            item.setImageFiles(storeImageFiles);
        }

        itemRepository.update(itemId, item);

        redirectAttributes.addAttribute("itemId", item.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/items/{itemId}";
        //(뷰 템플릿을 호출하는 대신에) 상품 상세 화면으로 이동하도록 "리다이렉트"를 호출
    }*/


    /** 상품 등록 처리 - @RequestParam
    * 요청 파라미터 형식을 처리해야 하므로 @RequestParam 을 사용
    */
    //@PostMapping("/add")      //중복매핑 주석처리
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model) {
        Item item = new Item();     //Item 객체를 생성
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);
        //저장된 item 을 모델에 담아서 뷰에 전달
        model.addAttribute("item", item);
        //모델에 "item" 이름으로 저장된다

        return "basic/item";
    }


    /** 상품 등록 처리 - @ModelAttribute
     * @RequestParam 으로 변수를 하나하나 받아서 Item 생성하지않고
     * @ModelAttribute 를 사용해서 !한번에! 처리
     * (@ModelAttribute 는 Item 객체를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법(setXxx)으로 입력)
    */
    /**
     * @ModelAttribute("item") Item item
     * 이름을 "item" 로 지정 (모델에 "item" 이름으로 저장)
     * ->model.addAttribute("item", item); 자동 추가
     */
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
        itemRepository.save(item);
        return "basic/item";
    }
    /*
     * @ModelAttribute 이름 생략 가능
     * 생략시 model에 저장(자동 추가)되는 name은 클래스명 첫글자만 소문자로 등록
     * (즉 model.addAttribute(item);가 자동 추가) (Item -> item)
     */
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "basic/item";
    }
    /*
     * @ModelAttribute 자체 생략 가능
     */
    //@PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }
    //새로 고침하면, ID만 다른 상품 데이터가 계속 쌓이게 된다 (중복등록)

    /**
     * PRG - Post/Redirect/Get
     * 새로 고침 문제를 해결하려면, 상품 저장 후에 뷰 템플릿으로 이동하는 것이 아니라,
     * 상품 상세 화면으로 리다이렉트를 호출해주면 된다
     */
    //@PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId(); //상품상세화면으로 다시 이동
        /* URL에 변수를 더해서 사용하는 것은 URL 인코딩이 안되기 때문에 위험
            * 그러므로 RedirectAttributes 를 사용!*/
   }
    /**
     * RedirectAttributes:  URL 인코딩도 해주고, pathVarible , 쿼리 파라미터까지 처리
     * 리다이렉트 할 때 status=true를 추가-> 뷰 템플릿에서 이 값이 있으면, "저장되었습니다."라는 메시지 출력
     */
    //@PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
        //http://localhost:8080/basic/items/3?status=true
    }


     /*
     테스트용 데이터(item목록) 추가
     */
    /*@PostConstruct  //해당 빈의 의존관계가 모두 주입되고 나면 초기화 용도로 호출
    public void init() {
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));
    }*/
}
