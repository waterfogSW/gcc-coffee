package com.waterfogsw.gcccoffee.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waterfogsw.gcccoffee.product.controller.api.ProductApiController;
import com.waterfogsw.gcccoffee.product.model.Category;
import com.waterfogsw.gcccoffee.product.model.Product;
import com.waterfogsw.gcccoffee.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductApiControllerTests {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    ProductService productService;

    @InjectMocks
    ProductApiController productApiController;

    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(productApiController)
                .build();
    }

    @Nested
    @DisplayName("productAdd 메서드는")
    class Describe_productAdd {

        String url = "/api/v1/products";

        @Nested
        @DisplayName("name 이 body 에 없으면")
        class Context_without_name {

            @Test
            @DisplayName("BadRequest 를 반환한다")
            void it_response_bad_request() throws Exception {
                Map<String, String> postRequest = new HashMap<>();
                postRequest.put("category", "COFFEE_GRINDER");
                postRequest.put("price", "1000");

                final var content = objectMapper.writeValueAsString(postRequest);
                final var request = post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("category 가 body 에 없으면")
        class Context_without_category {

            @Test
            @DisplayName("BadRequest 를 반환한다")
            void it_response_bad_request() throws Exception {
                Map<String, String> postRequest = new HashMap<>();
                postRequest.put("name", "colombia coffee");
                postRequest.put("price", "1000");

                final var content = objectMapper.writeValueAsString(postRequest);
                final var request = post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 category 요청이 들어오면")
        class Context_with_not_exist_category {

            @Test
            @DisplayName("BadRequest 를 반환한다")
            void it_response_bad_request() throws Exception {
                Map<String, String> postRequest = new HashMap<>();
                postRequest.put("name", "colombia coffee");
                postRequest.put("category", "Hello");
                postRequest.put("price", "1000");

                final var content = objectMapper.writeValueAsString(postRequest);
                final var request = post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("price 가 body 에 없으면")
        class Context_without_price {

            @Test
            @DisplayName("BadRequest 를 반환한다")
            void it_response_bad_request() throws Exception {
                Map<String, String> postRequest = new HashMap<>();
                postRequest.put("name", "colombia coffee");
                postRequest.put("category", "COFFEE_GRINDER");

                final var content = objectMapper.writeValueAsString(postRequest);
                final var request = post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("price 가 음수면")
        class Context_with_negative_price {

            @Test
            @DisplayName("BadRequest 를 반환한다")
            void it_response_bad_request() throws Exception {
                Map<String, String> postRequest = new HashMap<>();
                postRequest.put("name", "colombia coffee");
                postRequest.put("category", "COFFEE_GRINDER");
                postRequest.put("price", "-1");

                final var content = objectMapper.writeValueAsString(postRequest);
                final var request = post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("name, category, price 가 body 에 있으면")
        class Context_with_all {

            @Test
            @DisplayName("ok status 의 response 를 반환한다")
            void it_response_ok() throws Exception {
                final Product product = new Product.Builder(1L)
                        .name("colombia coffee")
                        .category(Category.COFFEE_BEAN_PACKAGE)
                        .price(1000)
                        .build();

                final Map<String, String> postRequest = new HashMap<>();
                postRequest.put("name", product.getName());
                postRequest.put("category", product.getCategory().name());
                postRequest.put("price", String.valueOf(product.getPrice()));

                when(productService.addProduct(any())).thenReturn(product);

                final var content = objectMapper.writeValueAsString(postRequest);
                final var request = post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isOk());
            }
        }
    }
}
