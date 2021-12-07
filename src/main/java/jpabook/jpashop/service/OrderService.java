package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        Member findMember = memberRepository.findOne(memberId);
        Item findItem = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(findMember.getAddress());
        
        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(findItem, findItem.getPrice(), count);

        //주문생성
        Order order = Order.createOrder(findMember, delivery, orderItem);
        orderItem.setOrder(order);

        orderRepository.save(order);

        return order.getId();
    }

    //주문취소
    @Transactional
    public void cancelOrder(Long orderId) {
        Order findOrder = orderRepository.findOne(orderId);
        findOrder.cancel();
    }

    //검색
    public List<Order> searchOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }

}
