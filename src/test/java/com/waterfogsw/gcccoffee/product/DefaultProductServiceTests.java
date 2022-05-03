package com.waterfogsw.gcccoffee.product;

import com.waterfogsw.gcccoffee.product.model.Category;
import com.waterfogsw.gcccoffee.product.model.Product;
import com.waterfogsw.gcccoffee.product.repository.ProductRepository;
import com.waterfogsw.gcccoffee.product.service.DefaultProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultProductServiceTests {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    DefaultProductService productService;

    @Nested
    @DisplayName("addProduct 메서드는")
    class Describe_addProduct {

        @Nested
        @DisplayName("product 인자가 null 이면")
        class Context_with_null {

            @Test
            @DisplayName("IllegalArgumentException 을 발생시킨다")
            void it_throw_IllegalArgumentException() {
                assertThrows(IllegalArgumentException.class, () -> productService.addProduct(null));
            }
        }

        @Nested
        @DisplayName("정상적으로 호출되면")
        class Context_with_call {

            @Test
            @DisplayName("저장한 값을 반환한다")
            void it_throw_IllegalArgumentException() {
                final var product = new Product.Builder(0)
                        .name("colombia")
                        .category(Category.COFFEE_BEAN_PACKAGE)
                        .price(1000)
                        .build();

                when(productRepository.insert(any())).thenReturn(product);

                final var addedProduct = productService.addProduct(product);

                assertThat(addedProduct.getName(), is(product.getName()));
                assertThat(addedProduct.getCategory(), is(product.getCategory()));
                assertThat(addedProduct.getDescription(), is(product.getDescription()));
                assertThat(addedProduct.getCreatedAt(), is(product.getCreatedAt()));
                assertThat(addedProduct.getUpdatedAt(), is(product.getUpdatedAt()));
            }
        }
    }
    
    @Nested
    @DisplayName("findAllProduct 메서드는")
    class Describe_findAllProduct {
    
        @Nested
        @DisplayName("호출되면")
        class Context_with_call {
            
            @Test
            @DisplayName("저정된 모든 제품 List 를 반환한다")
            void it_product_list() {
                final var product1 = new Product(1L, "product1", Category.COFFEE_GRINDER, 10000, "");
                final var product2 = new Product(2L, "product2", Category.COFFEE_GRINDER, 12000, "");
                final List<Product> products = new ArrayList<>(Arrays.asList(product1, product2));

                when(productRepository.selectAll()).thenReturn(products);

                final var findProducts = productService.findAllProduct();
                assertThat(findProducts.size(), is(2));
            }
        }
    }
}
