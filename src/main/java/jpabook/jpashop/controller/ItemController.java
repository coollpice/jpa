package jpabook.jpashop.controller;


import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("bookForm", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(@Valid BookForm bookForm, BindingResult result) {

        if (result.hasErrors()) {
            return "items/createItemForm";
        }

        Book book = new Book();
        //setter 보다 생성메서드를 만드는게 더 좋은 설계
        book.setName(bookForm.getName());
        book.setPrice(bookForm.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());
        book.setAuthor(bookForm.getAuthor());
        book.setIsbn(bookForm.getIsbn());

        itemService.saveItem(book);

        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("bookForm", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId") Long itemId , @ModelAttribute("bookForm") BookForm bookForm) {
        itemService.updateItem(itemId , bookForm.getName(), bookForm.getPrice(), bookForm.getStockQuantity());
        return "redirect:/items";

    }

    @GetMapping("/items/{itemId}/delete")
    public String deleteItem(@PathVariable("itemId") Long itemId , RedirectAttributes redirectAttributes) {

        try {
            itemService.deleteItem(itemId);
        } catch (Exception e) {
            redirectAttributes.addAttribute("status", true);
            return "redirect:/items";
        }
        return "redirect:/items";
    }

}
