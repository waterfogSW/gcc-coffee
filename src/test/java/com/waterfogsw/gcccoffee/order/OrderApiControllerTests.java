package com.waterfogsw.gcccoffee.order;

import com.google.gson.Gson;
import com.waterfogsw.gcccoffee.common.exception.ControllerAdvisor;
import com.waterfogsw.gcccoffee.order.controller.api.OrderApiController;
import com.waterfogsw.gcccoffee.order.model.OrderProduct;
import com.waterfogsw.gcccoffee.order.service.OrderService;
import com.waterfogsw.gcccoffee.product.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class OrderApiControllerTests {

    private static final Gson gson = new Gson();
    private static final String url = "/api/v1/orders";

    @Mock
    OrderService orderService;

    @InjectMocks
    OrderApiController orderApiController;

    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderApiController)
                .setControllerAdvice(ControllerAdvisor.class)
                .build();
    }

    @Nested
    @DisplayName("orderAdd 메서드는")
    class Describe_orderAdd {

        @Nested
        @DisplayName("값이 생성된 경우")
        class Context_with_AllValidDate {

            @Test
            @DisplayName("created response 를 반환한다")
            void it_ResponseCreated() throws Exception {
                final var orderProduct1 = new OrderProduct(1, Category.COFFEE_GRINDER, 10000, 1);
                final var orderProduct2 = new OrderProduct(2, Category.COFFEE_BEAN_PACKAGE, 12000, 1);
                final var orderProducts = new ArrayList<>(Arrays.asList(orderProduct1, orderProduct2));

                final var postRequest = new HashMap<String, Object>();
                postRequest.put("email", "test@naver.com");
                postRequest.put("address", "영통구");
                postRequest.put("postcode", "111-111");
                postRequest.put("orderProducts", orderProducts);

                final var content = gson.toJson(postRequest);
                final var request = post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isCreated());
            }
        }

        @Nested
        @DisplayName("id 값이 양수가 아닌 경우")
        class Context_with_NotPositiveId {

            @ParameterizedTest
            @ValueSource(ints = {-1, 0})
            @DisplayName("BadRequest 의 response 를 반환한다")
            void it_ResponseBadRequest(int price) throws Exception {
                final var orderProduct = new OrderProduct(price, Category.COFFEE_GRINDER, 10000, 1);
                final var orderProducts = new ArrayList<>(List.of(orderProduct));

                final var postRequest = new HashMap<String, Object>();
                postRequest.put("email", "test@naver.com");
                postRequest.put("address", "영통구");
                postRequest.put("postcode", "111-111");
                postRequest.put("orderProducts", orderProducts);

                final var content = gson.toJson(postRequest);
                final var request = post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("가격이 양수가 아닌 경우")
        class Context_with_NotPositivePrice {

            @ParameterizedTest
            @ValueSource(ints = {-1, 0})
            @DisplayName("BadRequest 의 response 를 반환한다")
            void it_ResponseBadRequest(int price) throws Exception {
                final var orderProduct = new OrderProduct(1, Category.COFFEE_GRINDER, price, 1);
                final var orderProducts = new ArrayList<>(List.of(orderProduct));

                final var postRequest = new HashMap<String, Object>();
                postRequest.put("email", "test@naver.com");
                postRequest.put("address", "영통구");
                postRequest.put("postcode", "111-111");
                postRequest.put("orderProducts", orderProducts);

                final var content = gson.toJson(postRequest);
                final var request = post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("수량이 양수가 아닌경우")
        class Context_with_NotPositiveQuantity {

            @ParameterizedTest
            @ValueSource(ints = {-1, 0})
            @DisplayName("BadRequest 의 response 를 반환한다")
            void it_ResponseBadRequest(int quantity) throws Exception {
                final var orderProduct = new OrderProduct(1, Category.COFFEE_GRINDER, 1000, quantity);
                final var orderProducts = new ArrayList<>(List.of(orderProduct));

                final var postRequest = new HashMap<String, Object>();
                postRequest.put("email", "test@naver.com");
                postRequest.put("address", "영통구");
                postRequest.put("postcode", "111-111");
                postRequest.put("orderProducts", orderProducts);

                final var content = gson.toJson(postRequest);
                final var request = post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("email 이 없는 경우")
        class Context_withoutEmail {

            @Test
            @DisplayName("BadRequest 의 response 를 반환한다")
            void it_response_ok() throws Exception {
                final var orderProduct1 = new OrderProduct(1, Category.COFFEE_GRINDER, 10000, 1);
                final var orderProduct2 = new OrderProduct(2, Category.COFFEE_BEAN_PACKAGE, 12000, 1);
                final var orderProducts = new ArrayList<>(Arrays.asList(orderProduct1, orderProduct2));

                final var postRequest = new HashMap<String, Object>();
                postRequest.put("address", "영통구");
                postRequest.put("postcode", "111-111");
                postRequest.put("orderProducts", orderProducts);

                final var content = gson.toJson(postRequest);
                final var request = post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("email 형식에 맞지 않은 경우")
        class Context_withInvalidEmail {

            @Test
            @DisplayName("BadRequest 의 response 를 반환한다")
            void it_response_ok() throws Exception {
                final var orderProduct1 = new OrderProduct(1, Category.COFFEE_GRINDER, 10000, 1);
                final var orderProduct2 = new OrderProduct(2, Category.COFFEE_BEAN_PACKAGE, 12000, 1);
                final var orderProducts = new ArrayList<>(Arrays.asList(orderProduct1, orderProduct2));

                final var postRequest = new HashMap<String, Object>();
                postRequest.put("email", "never.com");
                postRequest.put("address", "영통구");
                postRequest.put("postcode", "111-111");
                postRequest.put("orderProducts", orderProducts);

                final var content = gson.toJson(postRequest);
                final var request = post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("주소가 없는 경우")
        class Context_withoutAddress {

            @Test
            @DisplayName("BadRequest 의 response 를 반환한다")
            void it_ResponseOk() throws Exception {
                final var orderProduct1 = new OrderProduct(1, Category.COFFEE_GRINDER, 10000, 1);
                final var orderProduct2 = new OrderProduct(2, Category.COFFEE_BEAN_PACKAGE, 12000, 1);
                final var orderProducts = new ArrayList<>(Arrays.asList(orderProduct1, orderProduct2));

                final var postRequest = new HashMap<String, Object>();
                postRequest.put("email", "test@naver.com");
                postRequest.put("postcode", "111-111");
                postRequest.put("orderProducts", orderProducts);

                final var content = gson.toJson(postRequest);
                final var request = post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("우편번호가 없는 경우")
        class Context_withoutPostcode {

            @Test
            @DisplayName("BadRequest 의 response 를 반환한다")
            void it_ResponseOk() throws Exception {
                final var orderProduct1 = new OrderProduct(1, Category.COFFEE_GRINDER, 10000, 1);
                final var orderProduct2 = new OrderProduct(2, Category.COFFEE_BEAN_PACKAGE, 12000, 1);
                final var orderProducts = new ArrayList<>(Arrays.asList(orderProduct1, orderProduct2));

                final var postRequest = new HashMap<String, Object>();
                postRequest.put("email", "test@naver.com");
                postRequest.put("address", "영통구");
                postRequest.put("orderProducts", orderProducts);

                final var content = gson.toJson(postRequest);
                final var request = post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("주문할 제품 정보가 없는 경우 ")
        class Context_without_orderProduct {

            @Test
            @DisplayName("BadRequest 의 response 를 반환한다")
            void it_ResponseOk() throws Exception {
                final var postRequest = new HashMap<String, Object>();
                postRequest.put("email", "test@naver.com");
                postRequest.put("address", "영통구");
                postRequest.put("postcode", "111-111");

                final var content = gson.toJson(postRequest);
                final var request = post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }
    }
}
