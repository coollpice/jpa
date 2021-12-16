package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.order.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional()
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    @Rollback(value = false)
    public void 주문() {

        //given
        Member member = createMember();

        Book book = createItem("JPA 기초", 10000, 10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order findOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER , findOrder.getStatus() , "상품주문시 상태는 ORDER");
        assertEquals(1 , findOrder.getOrderItems().size() , "주문한 상품 종류 수");
        assertEquals(findOrder.getTotalPrice() , 10000 * orderCount , "주문 가격은 가격 * 주문수량");
        assertEquals(8 , book.getStockQuantity() , "주문 수량만큼 재고가 줄어야 한다.");

    }

    @Test
    public void 상품주문_재고수량초과() throws Exception{

        //given
        Member member = createMember();
        Item item = createItem("JPA 기초", 10000, 10);

        //when
        int orderCount = 11;

        //then
        assertThrows(NotEnoughStockException.class ,  () -> orderService.order(member.getId(), item.getId(), orderCount) , "재고수량 예외가 터져야한다.");

    }

    @Test
    public void 주문취소() {
        //given
        Member member = createMember();
        Item item = createItem("JPA" , 10000, 10);

        int orderCount = 2;
        Long findOrderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(findOrderId);

        //then
        Order order = orderRepository.findOne(findOrderId);
        assertEquals(OrderStatus.CANCEL , order.getStatus() , "주문 취소시 상태는 CANCEL");
        assertEquals(item.getStockQuantity() , 10 , "주문이 취소된 상품은 다시 재고가 증가하여야 함");
    }

    private Book createItem(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원");
        member.setAddress(new Address("서울", "3번길", "12125"));
        em.persist(member);
        return member;
    }

}