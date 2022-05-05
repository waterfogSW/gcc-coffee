package com.waterfogsw.gcccoffee.order;

import com.waterfogsw.gcccoffee.order.model.Email;
import com.waterfogsw.gcccoffee.order.model.Order;
import com.waterfogsw.gcccoffee.order.model.OrderProduct;
import com.waterfogsw.gcccoffee.order.repository.OrderRepository;
import com.waterfogsw.gcccoffee.order.service.OrderDefaultService;
import com.waterfogsw.gcccoffee.product.model.Category;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class OrderDefaultServiceTests {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderDefaultService orderDefaultService;

    @Nested
    class addOrder_메서드는 {

        @Nested
        class 인자가_null_이면 {

            @Test
            void IllegalArgument_에러를_발생시킨다() {
                assertThrows(IllegalArgumentException.class, () -> orderDefaultService.addOrder(null));
            }
        }

        @Nested
        class 인자가_null_이_아니면 {

            @Test
            void repository_insert_메서드를_호출한다() {
                final var orderProduct = new OrderProduct(1, Category.COFFEE_GRINDER, 10000, 1);
                final var orderProducts = new ArrayList<>(List.of(orderProduct));
                final var order = new Order(new Email("test@naver.com"), "영통구", "111-111", orderProducts);

                orderDefaultService.addOrder(order);
                verify(orderRepository).insert(any(Order.class));
            }
        }
    }
}
