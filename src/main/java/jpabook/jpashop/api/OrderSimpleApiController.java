package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.order.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * X TO One ( Many To One , One To One )
 * 연관관계 정보
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public Result ordersV1() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());

        for (Order order : orders) {
            order.getMember().getName(); //Lazy 강제 초기화. 실제쿼리 나감
            order.getDelivery().getAddress(); //Lazy 강제 초기화. 실제쿼리 나감
        }
        return new Result<>(orders.size(), orders);
//        return orders;
    }

    @GetMapping("/api/v2/simple-orders")
    public Result ordersV2() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        List<SimpleOrderDto> orderDtoList = orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
        return new Result(orderDtoList.size(), orderDtoList);
    }

    @GetMapping("/api/v3/simple-orders")
    public Result orderV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> resultList = orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
        return new Result(resultList.size(), resultList);
    }

    @GetMapping("/api/v4/simple-orders")
    public Result orderV4() {
        List<OrderSimpleQueryDto> orderDtoList = orderSimpleQueryRepository.findOrderDto();
        return new Result(orderDtoList.size() , orderDtoList);
    }


    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderStatus = order.getStatus();
            this.orderDate = order.getOrderDate();
            this.address = order.getDelivery().getAddress();
        }
    }


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }
}
