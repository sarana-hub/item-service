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
@RequiredArgsConstructor           //final이 붙은 멤버변수만 사용해서 생성자를 자동 생성
public class BasicItemController {

    private final ItemRepository itemRepository;

    /** 상품 목록 - 타임리프 */
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();    //모든 item 조회
        model.addAttribute("items", items);  //items(모든 item)을 모델에 담는다
        return "basic/items";   //뷰 템플릿 호출
    }

    /** 상품 상세 */
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);    //PathVariable 로 넘어온 itemId로 item 조회
        model.addAttribute("item", item);
        return "basic/item";
    }

    /** 상품 등록 폼 */
    @GetMapping("/add")
    public String addForm() {   //단순히 뷰 템플릿만 호출

        return "basic/addForm";
    }



    /** 상품 등록 처리 - @RequestParam
    * 요청 파라미터 형식을 처리해야 하므로 @RequestParam 을 사용
    */
    //@PostMapping("/add")      //이전 코드의 매핑(중복매핑) 주석처리
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
     * @ModelAttribute 를 사용해서 !!한번에!! 처리
    */
    /**
     * @ModelAttribute("item") Item item    //이름을 "item" 로 지정 (모델에 "item" 이름으로 저장)
     * @ModelAttribute 로 지정한 객체를 모델(Model)에 자동으로 넣어준다.
     * (즉 model.addAttribute("item", item); 가 주석처리되어 있어도 잘 동작한다.)
     *
     * @ModelAttribute 는 Item 객체를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법(setXxx)으로 입력
     */
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
        itemRepository.save(item);
        //model.addAttribute("item", item); 자동 추가(생략 가능)
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
     * @ModelAttribute 전체 생략 가능
     */
    //@PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }
    //새로 고침하면, ID만 다른 상품 데이터가 계속 쌓이게 된다.


    /**
     * PRG - Post/Redirect/Get
     * 새로 고침 문제를 해결하려면, 상품 저장 후에 뷰 템플릿으로 이동하는 것이 아니라,
     * 상품 상세 화면으로 리다이렉트를 호출해주면 된다
     */
    //@PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }
    /**
     * RedirectAttributes
     * +item.getId() 처럼 URL에 변수를 더해서 사용하는 것은 URL 인코딩이 안되기 때문에 위험
     * 그러므로 RedirectAttributes 를 사용!
     */
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }



    /** 상품 수정 폼 */
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";    //수정용 폼 뷰를 호출
    }
    /** 상품 수정 처리 */
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
