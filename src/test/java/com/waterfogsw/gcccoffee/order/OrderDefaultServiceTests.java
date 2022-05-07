package com.waterfogsw.gcccoffee.order;

import com.waterfogsw.gcccoffee.order.model.Order;
import com.waterfogsw.gcccoffee.order.model.OrderProduct;
import com.waterfogsw.gcccoffee.order.repository.OrderRepository;
import com.waterfogsw.gcccoffee.order.service.OrderDefaultService;
import com.waterfogsw.gcccoffee.product.model.Category;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class OrderDefaultServiceTests {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderDefaultService orderDefaultService;

    @Nested
    @DisplayName("addOrder 메서드는")
    class Describe_addOrder {

        @Nested
        @DisplayName("인자가 null 인 경우")
        class Context_withNullArgument {

            @Test
            @DisplayName("IllegalArgument 에러를 발생시킨다")
            void It_IllegalArgument() {
                assertThrows(IllegalArgumentException.class, () -> orderDefaultService.addOrder(null));
            }
        }

        @Nested
        @DisplayName("인자가 null 이 아닌 경우")
        class Context_withNotNullArgument {

            @Test
            @DisplayName("repository insert 메서드를 호출한다")
            void It_IllegalArgument() {
                final var orderProduct = new OrderProduct(1, Category.COFFEE_GRINDER, 10000, 1);
                final var orderProducts = new ArrayList<>(List.of(orderProduct));
                final var order = new Order("test@naver.com", "영통구", "111-111", orderProducts);

                orderDefaultService.addOrder(order);
                verify(orderRepository).insert(any(Order.class));
            }
        }
    }
}
