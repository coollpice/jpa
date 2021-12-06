package jpabook.jpashop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello");
        return "hello";
    }

 /*   @GetMapping("/test")
    @ResponseBody
    public Member test() {
        Member m = new Member();
        m.setId(1L);
        m.setUsername("테스터");
        return m;
    }

    @GetMapping("/test2")
    @ResponseBody
    public List<Member> test2() {

        Member m = new Member();
        m.setId(1L);
        m.setUsername("테스터");

        Member m2 = new Member();
        m2.setId(2L);
        m2.setUsername("테스터2");

        List<Member> memberList = new ArrayList<>();
        memberList.add(m);
        memberList.add(m2);

        return memberList;
    }

    @PostMapping("/test3")
    @ResponseBody
    public List<Member> test3(@RequestBody List<Member> members) {
        members.forEach(m -> log.info("Member = {}" , m.toString()));
        return members;
    }*/

}
