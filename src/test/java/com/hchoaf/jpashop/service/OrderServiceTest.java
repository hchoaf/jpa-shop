package com.hchoaf.jpashop.service;

import com.hchoaf.jpashop.entity.*;
import com.hchoaf.jpashop.entity.item.Book;
import com.hchoaf.jpashop.exception.NotEnoughStockException;
import com.hchoaf.jpashop.repository.ItemRepository;
import com.hchoaf.jpashop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Test
    public void createOrderTest() throws Exception {
        Member member = new Member();
        member.setName("Hangsun");
        member.setAddress(new Address("Seoul", "Songpa", "12345"));
        em.persist(member);

        Book book = new Book();
        book.setName("JPA Book");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderQty = 8;

        Long orderId = orderService.createOrder(member.getId(), book.getId(), orderQty);
        Order createdOrder = orderRepository.findOne(orderId);

        // OrderStatus check
        assertEquals(OrderStatus.ORDER, createdOrder.getOrderStatus());

        // TotalPrice check
        assertEquals(book.getPrice()*orderQty, createdOrder.getTotalPrice());

        // StockQuantity check
        assertEquals(10 - orderQty, book.getStockQuantity());
        assertEquals(1, createdOrder.getOrderItems().size());
    }

    @Test
    public void cancelOrderTest() throws Exception {
        Member member = new Member();
        member.setName("Hangsun");
        member.setAddress(new Address("Seoul", "Songpa", "12345"));
        em.persist(member);

        Book book = new Book();
        book.setName("JPA Book");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderQty = 8;

        Long orderId = orderService.createOrder(member.getId(), book.getId(), orderQty);
        Order createdOrder = orderRepository.findOne(orderId);
        Long cancelledOrderId = orderService.cancelOrder(orderId);

        assertEquals(OrderStatus.CANCEL, createdOrder.getOrderStatus());
        assertEquals(10, book.getStockQuantity());

        Long orderId2 = orderService.createOrder(member.getId(), book.getId(), orderQty);
        Order createdOrder2 = orderRepository.findOne(orderId2);
        createdOrder2.getDelivery().setStatus(DeliveryStatus.COMPLETED);
        System.out.println(createdOrder2.getDelivery().getStatus().toString());
        IllegalStateException exception =
                assertThrows(IllegalStateException.class, () -> orderService.cancelOrder(orderId2));
        System.out.println(exception.getMessage());
        assertTrue(exception.getMessage().contains("Cannot cancel an order that is already delivered"));
    }
    @Test
    public void createOrderFailureTest() throws Exception {
            Member member = new Member();
            member.setName("Hangsun");
            member.setAddress(new Address("Seoul", "Songpa", "12345"));
            em.persist(member);

            Book book = new Book();
            book.setName("JPA Book");
            book.setPrice(10000);
            book.setStockQuantity(2);
            em.persist(book);

            int orderQty = 8;
            NotEnoughStockException exception =
                    assertThrows(NotEnoughStockException.class, () -> orderService.createOrder(member.getId(), book.getId(), orderQty));
            assertTrue(exception.getMessage().contains("Not enough stock"));
    }
}