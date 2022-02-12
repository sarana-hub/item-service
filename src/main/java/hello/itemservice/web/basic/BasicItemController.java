package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

/* 컨트롤러 - 폼 뷰를 호출 */

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor    //final이 붙은 멤버변수만 사용해서 생성자를 자동 생성
public class BasicItemController {

    private final ItemRepository itemRepository;

    /* 상품 목록 - 타임리프 */
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();    //모든 item 조회
        model.addAttribute("items", items);  //items(모든 item)을 모델에 담는다
        return "basic/items";   //뷰 템플릿 호출
    }

    /* 상품 상세 */
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);    //PathVariable 로 넘어온 itemId로 item 조회
        model.addAttribute("item", item);
        return "basic/item";
    }

    /* 상품 등록 폼 */
    @GetMapping("/add")
    public String addForm() {   //단순히 뷰 템플릿만 호출

        return "basic/addForm";
    }


    /* 상품 등록 처리 - @RequestParam
    * 요청 파라미터 형식을 처리해야 하므로 @RequestParam 을 사용
    */
    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }
    /**
     * @ModelAttribute("item") Item item
     * model.addAttribute("item", item); 자동 추가
     */
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
        itemRepository.save(item);
        //model.addAttribute("item", item); //자동 추가, 생략 가능
        return "basic/item";
    }
    /**
     * @ModelAttribute name 생략 가능
     * model.addAttribute(item); 자동 추가, 생략 가능
     * 생략시 model에 저장되는 name은 클래스명 첫글자만 소문자로 등록 Item -> item
     */
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "basic/item";
    }
    /**
     * @ModelAttribute 자체 생략 가능
     * model.addAttribute(item) 자동 추가
     */
    //@PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }
    /**
     * PRG - Post/Redirect/Get
     */
    //@PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }
    /**
     * RedirectAttributes
     */
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }


    /* 상품 수정 */
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }
    /* 상품 수정 저장 */
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
        //(뷰 템플릿을 호출하는 대신에) 상품 상세 화면으로 이동하도록 리다이렉트를 호출
    }




     /*
     테스트용 데이터(item목록) 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));
    }
}
