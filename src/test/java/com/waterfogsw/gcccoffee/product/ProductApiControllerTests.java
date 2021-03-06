package com.waterfogsw.gcccoffee.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.waterfogsw.gcccoffee.common.exception.ControllerAdvisor;
import com.waterfogsw.gcccoffee.product.controller.api.ProductApiController;
import com.waterfogsw.gcccoffee.product.model.Category;
import com.waterfogsw.gcccoffee.product.model.Product;
import com.waterfogsw.gcccoffee.product.service.ProductService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductApiControllerTests {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String url = "/api/v1/products";

    @BeforeAll
    public static void beforeAll() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Mock
    ProductService productService;

    @InjectMocks
    ProductApiController productApiController;

    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(productApiController)
                .setControllerAdvice(ControllerAdvisor.class)
                .build();
    }

    @Nested
    @DisplayName("productAdd ????????????")
    class Describe_productAdd {

        @Nested
        @DisplayName("name ??? ?????? ??????")
        class Context_withoutName {

            @Test
            @DisplayName("BadRequest ??? ????????????")
            void it_responseBadRequest() throws Exception {
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
        @DisplayName("category ??? ?????? ??????")
        class Context_withoutCategory {

            @Test
            @DisplayName("BadRequest ??? ????????????")
            void it_responseBadRequest() throws Exception {
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
        @DisplayName("???????????? ?????? category ??? ??????")
        class Context_withNotExistingCategory {

            @Test
            @DisplayName("BadRequest ??? ????????????")
            void it_responseBadRequest() throws Exception {
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
        @DisplayName("price ??? ?????? ??????")
        class Context_withoutPrice {

            @Test
            @DisplayName("BadRequest ??? ????????????")
            void it_responseBadRequest() throws Exception {
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
        @DisplayName("price ??? ????????? ?????? ??????")
        class Context_withPriceNotPositive {

            @Test
            @DisplayName("BadRequest ??? ????????????")
            void it_responseBadRequest() throws Exception {
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
        @DisplayName("?????? ???????????? ????????? ??????")
        class Context_withAllValid {

            @Test
            @DisplayName("created response ??? ????????????")
            void it_ResponseCreated() throws Exception {
                final Product product = new Product.Builder(1L)
                        .name("colombia coffee")
                        .category(Category.COFFEE_BEAN_PACKAGE)
                        .price(1000)
                        .build();

                final Map<String, String> postRequest = new HashMap<>();
                postRequest.put("name", product.getName());
                postRequest.put("category", product.getCategory().name());
                postRequest.put("price", String.valueOf(product.getPrice()));

                final var content = objectMapper.writeValueAsString(postRequest);
                final var request = post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isCreated());
            }
        }
    }

    @Nested
    @DisplayName("productList ????????????")
    class Describe_productList {

        @Nested
        @DisplayName("????????? ?????? ??????")
        class Context_withNoArgument {

            @Test
            @DisplayName("?????? ?????? ???????????? ????????????")
            void it_returnAllProducts() throws Exception {
                final var product1 = new Product(1L, "product1", Category.COFFEE_GRINDER, 10000, "");
                final var product2 = new Product(2L, "product2", Category.COFFEE_GRINDER, 12000, "");
                final List<Product> products = new ArrayList<>(Arrays.asList(product1, product2));

                when(productService.findAllProduct()).thenReturn(products);

                final var request = get(url);
                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isOk());

                final var resultContent = resultActions.andReturn()
                        .getResponse()
                        .getContentAsString();

                final var expectedContent = MessageFormat.format("[{0},{1}]",
                        objectMapper.writeValueAsString(product1),
                        objectMapper.writeValueAsString(product2));

                assertThat(resultContent, is(expectedContent));
            }
        }

        @Nested
        @DisplayName("sort=PRICE_ASC ??? ??????")
        class Context_withPriceAsc {

            @Test
            @DisplayName("????????????????????? ????????? ?????? ???????????? ????????????")
            void it_returnProductsSortedByPriceASC() throws Exception {
                final var product1 = new Product(1L, "product1", Category.COFFEE_GRINDER, 11000, "");
                final var product2 = new Product(2L, "product2", Category.COFFEE_GRINDER, 15000, "");
                final var product3 = new Product(2L, "product2", Category.COFFEE_GRINDER, 12000, "");
                final List<Product> products = new ArrayList<>(Arrays.asList(product1, product2, product3));

                when(productService.findAllProduct()).thenReturn(products);

                final var request = get(url + "?sort=PRICE_ASC");
                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isOk());

                final var resultContent = resultActions.andReturn()
                        .getResponse()
                        .getContentAsString();

                final var expectedContent = MessageFormat.format("[{0},{1},{2}]",
                        objectMapper.writeValueAsString(product1),
                        objectMapper.writeValueAsString(product3),
                        objectMapper.writeValueAsString(product2));

                assertThat(resultContent, is(expectedContent));
            }
        }

        @Nested
        @DisplayName("sort=PRICE_DESC ??? ??????")
        class Context_withPriceDesc {

            @Test
            @DisplayName("???????????????????????? ????????? ?????? ???????????? ????????????")
            void it_returnProductsSortedByPriceDESC() throws Exception {
                final var product1 = new Product(1L, "product1", Category.COFFEE_GRINDER, 11000, "");
                final var product2 = new Product(2L, "product2", Category.COFFEE_GRINDER, 15000, "");
                final var product3 = new Product(2L, "product2", Category.COFFEE_GRINDER, 12000, "");
                final List<Product> products = new ArrayList<>(Arrays.asList(product1, product2, product3));

                when(productService.findAllProduct()).thenReturn(products);

                final var request = get(url + "?sort=PRICE_DESC");
                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isOk());

                final var resultContent = resultActions.andReturn()
                        .getResponse()
                        .getContentAsString();

                final var expectedContent = MessageFormat.format("[{0},{1},{2}]",
                        objectMapper.writeValueAsString(product2),
                        objectMapper.writeValueAsString(product3),
                        objectMapper.writeValueAsString(product1));

                assertThat(resultContent, is(expectedContent));
            }
        }

        @Nested
        @DisplayName("sort=DATE_ASC ??? ??????")
        class Context_withDateASC {

            @Test
            @DisplayName("?????? ???????????? ????????? ?????? ???????????? ????????????")
            void it_returnProductsSortedByDateAsc() throws Exception {
                final var date1 = LocalDateTime.of(2022, 2, 4, 20, 20);
                final var date2 = LocalDateTime.of(2022, 1, 2, 20, 20);
                final var date3 = LocalDateTime.of(2022, 3, 8, 20, 20);

                final var product1 = new Product(1L, "product1", Category.COFFEE_GRINDER, 11000, "", date1, date1);
                final var product2 = new Product(2L, "product2", Category.COFFEE_GRINDER, 15000, "", date2, date2);
                final var product3 = new Product(2L, "product2", Category.COFFEE_GRINDER, 12000, "", date3, date3);
                final List<Product> products = new ArrayList<>(Arrays.asList(product1, product2, product3));

                when(productService.findAllProduct()).thenReturn(products);

                final var request = get(url + "?sort=DATE_ASC");
                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isOk());

                final var resultContent = resultActions.andReturn()
                        .getResponse()
                        .getContentAsString();

                final var expectedContent = MessageFormat.format("[{0},{1},{2}]",
                        objectMapper.writeValueAsString(product2),
                        objectMapper.writeValueAsString(product1),
                        objectMapper.writeValueAsString(product3));

                assertThat(resultContent, is(expectedContent));
            }
        }

        @Nested
        @DisplayName("sort=DATE_DESC ??? ??????")
        class Context_withDateDesc {

            @Test
            @DisplayName("?????? ???????????? ????????? ?????? ???????????? ????????????")
            void it_returnProductsSortedByDateDesc() throws Exception {
                final var date1 = LocalDateTime.of(2022, 2, 4, 20, 20);
                final var date2 = LocalDateTime.of(2022, 1, 2, 20, 20);
                final var date3 = LocalDateTime.of(2022, 3, 8, 20, 20);

                final var product1 = new Product(1L, "product1", Category.COFFEE_GRINDER, 11000, "", date1, date1);
                final var product2 = new Product(2L, "product2", Category.COFFEE_GRINDER, 15000, "", date2, date2);
                final var product3 = new Product(2L, "product2", Category.COFFEE_GRINDER, 12000, "", date3, date3);
                final List<Product> products = new ArrayList<>(Arrays.asList(product1, product2, product3));

                when(productService.findAllProduct()).thenReturn(products);

                final var request = get(url + "?sort=DATE_DESC");
                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isOk());

                final var resultContent = resultActions.andReturn()
                        .getResponse()
                        .getContentAsString();

                final var expectedContent = MessageFormat.format("[{0},{1},{2}]",
                        objectMapper.writeValueAsString(product3),
                        objectMapper.writeValueAsString(product1),
                        objectMapper.writeValueAsString(product2));

                assertThat(resultContent, is(expectedContent));
            }
        }

        @Nested
        @DisplayName("category ??????????????? ?????? ???????????? ??????")
        class Context_withExistingCategory {

            @ParameterizedTest
            @EnumSource(Category.class)
            @DisplayName("?????? ??????????????? ????????? ???????????? ")
            void it_returnAllCategoryProduct(Category category) throws Exception {

                final var product1 = new Product(1L, "product1", Category.COFFEE_GRINDER, 11000, "");
                final var product2 = new Product(2L, "product2", Category.COFFEE_BEAN_PACKAGE, 15000, "");
                final List<Product> products = new ArrayList<>(Arrays.asList(product1, product2));
                final Map<Category, Product> categoryProductMap = new HashMap<>() {{
                    put(Category.COFFEE_GRINDER, product1);
                    put(Category.COFFEE_BEAN_PACKAGE, product2);
                }};

                when(productService.findAllProduct()).thenReturn(products);

                final var request = get(url + "?category=" + category.name());
                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isOk());

                final var resultContent = resultActions.andReturn()
                        .getResponse()
                        .getContentAsString();

                final var expectedContent = MessageFormat.format("[{0}]",
                        objectMapper.writeValueAsString(categoryProductMap.get(category)));
                assertThat(resultContent, is(expectedContent));
            }
        }

        @Nested
        @DisplayName("category ??????????????? ?????? ???????????? ?????? ??????")
        class Context_withNotExistingCategory {

            @ParameterizedTest
            @EnumSource(Category.class)
            @DisplayName("?????? ??????????????? ????????? ???????????? ")
            void it_returnAllCategoryProduct(Category category) throws Exception {
                final var invalidCategory = "hello";
                final var request = get(url + "?category=" + invalidCategory);
                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("productDetail ????????????")
    class Describe_productDetail {

        @Nested
        @DisplayName("id ?????? 0????????? ??????")
        class Context_with_below_zero {

            @ParameterizedTest
            @ValueSource(longs = {-1, 0})
            @DisplayName("BadRequest ??? ????????????")
            void it_returns(long id) throws Exception {

                final var request = get(url + "/" + id);
                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("id ?????? ????????? ??????")
        class Context_with_over_zero {

            @ParameterizedTest
            @ValueSource(longs = {1, 100})
            @DisplayName("Ok ??? ????????????")
            void it_returns(long id) throws Exception {
                final var findProduct = new Product(1L, "product1", Category.COFFEE_GRINDER, 11000, "");
                when(productService.findById(anyLong())).thenReturn(findProduct);

                final var request = get(url + "/" + id);
                final var resultActions = mockMvc.perform(request);

                resultActions.andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("productModify ????????????")
    class Describe_productModify {

        @Nested
        @DisplayName("????????? ?????????????????? ???????????? ??????")
        class Context_with_ValidData {

            @Test
            @DisplayName("Ok ????????? ????????????")
            void It_ResponseOk() throws Exception {
                final var existProduct = new Product(1L, "product1", Category.COFFEE_GRINDER, 11000, "");
                when(productService.findById(anyLong())).thenReturn(existProduct);

                Map<String, String> putRequest = new HashMap<>();
                putRequest.put("name", "colombia coffee");
                putRequest.put("category", "COFFEE_BEAN_PACKAGE");
                putRequest.put("price", "1000");

                final var content = objectMapper.writeValueAsString(putRequest);
                final var request = put(url + "/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("name ??? ???????????? ?????? ??????")
        class Context_with_NullName {

            @Test
            @DisplayName("BadRequest ????????? ????????????")
            void It_ResponseBadRequest() throws Exception {
                Map<String, String> putRequest = new HashMap<>();
                putRequest.put("category", "COFFEE_BEAN_PACKAGE");
                putRequest.put("price", "1000");

                final var content = objectMapper.writeValueAsString(putRequest);
                final var request = put(url + "/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("category ??? ???????????? ?????? ??????")
        class Context_with_NullCategory {

            @Test
            @DisplayName("BadRequest ????????? ????????????")
            void It_ResponseBadRequest() throws Exception {
                Map<String, String> putRequest = new HashMap<>();
                putRequest.put("name", "kenya coffee bean");
                putRequest.put("price", "1000");

                final var content = objectMapper.writeValueAsString(putRequest);
                final var request = put(url + "/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("?????? category ??? ???????????? ?????? ??????")
        class Context_with_NotExistingCategory {

            @Test
            @DisplayName("BadRequest ????????? ????????????")
            void It_ResponseBadRequest() throws Exception {
                Map<String, String> putRequest = new HashMap<>();
                putRequest.put("name", "kenya coffee bean");
                putRequest.put("category", "hello world");
                putRequest.put("price", "1000");

                final var content = objectMapper.writeValueAsString(putRequest);
                final var request = put(url + "/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("price ??? ????????? ?????? ??????")
        class Context_with_NotPositivePrice {

            @ParameterizedTest
            @ValueSource(ints = {-1, 0})
            @DisplayName("BadRequest ????????? ????????????")
            void It_ResponseBadRequest(int price) throws Exception {
                Map<String, String> putRequest = new HashMap<>();
                putRequest.put("name", "kenya coffee bean");
                putRequest.put("category", "COFFEE_BEAN_PACKAGE");
                putRequest.put("price", String.valueOf(price));

                final var content = objectMapper.writeValueAsString(putRequest);
                final var request = put(url + "/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("price ??? ???????????? ?????? ??????")
        class Context_with_NullPrice {

            @Test
            @DisplayName("BadRequest ????????? ????????????")
            void It_ResponseBadRequest() throws Exception {
                Map<String, String> putRequest = new HashMap<>();
                putRequest.put("name", "kenya coffee bean");
                putRequest.put("category", "COFFEE_BEAN_PACKAGE");

                final var content = objectMapper.writeValueAsString(putRequest);
                final var request = put(url + "/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("id ?????? ????????? ????????????")
        class Context_with_NotPositiveId {

            @ParameterizedTest
            @ValueSource(ints = {-1, 0})
            @DisplayName("BadRequest ????????? ????????????")
            void It_ResponseBadRequest(int id) throws Exception {
                Map<String, String> putRequest = new HashMap<>();
                putRequest.put("name", "colombia coffee");
                putRequest.put("category", "COFFEE_BEAN_PACKAGE");
                putRequest.put("price", "1000");

                final var content = objectMapper.writeValueAsString(putRequest);
                final var request = put(url + "/" + id)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON);

                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("productRemove ????????????")
    class Describe_productRemove {

        @Nested
        @DisplayName("Id ??? ????????? ??????")
        class Context_withPositiveId {

            @Test
            @DisplayName("service ??? removeProduct ????????????_????????????")
            void It_call_removeProduct() throws Exception {
                final var request = delete(url + "/" + 1);
                final var resultActions = mockMvc.perform(request);
                verify(productService).removeProduct(anyLong());
            }
        }

        @Nested
        @DisplayName("??????????????? 0????????? ??????")
        class Context_with_argUnderZero {

            @ParameterizedTest
            @ValueSource(longs = {-1, 0})
            @DisplayName("BadRequest ????????? ??????")
            void It_call_removeProduct(long id) throws Exception {
                final var request = delete(url + "/" + id);
                final var resultActions = mockMvc.perform(request);
                resultActions.andExpect(status().isBadRequest());
            }
        }
    }
}
