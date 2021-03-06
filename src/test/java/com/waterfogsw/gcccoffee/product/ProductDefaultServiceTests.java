package com.waterfogsw.gcccoffee.product;

import com.waterfogsw.gcccoffee.common.exception.ResourceNotFound;
import com.waterfogsw.gcccoffee.product.model.Category;
import com.waterfogsw.gcccoffee.product.model.Product;
import com.waterfogsw.gcccoffee.product.repository.ProductRepository;
import com.waterfogsw.gcccoffee.product.service.ProductDefaultService;
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
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductDefaultServiceTests {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductDefaultService productService;

    @Nested
    @DisplayName("saveProduct 메서드는")
    class Describe_addProduct {

        @Nested
        @DisplayName("product 가 null 인 경우")
        class Context_withNullProduct {

            @Test
            @DisplayName("IllegalArgumentException 을 발생시킨다")
            void it_throw_IllegalArgumentException() {
                assertThrows(IllegalArgumentException.class, () -> productService.saveProduct(null));
            }
        }

        @Nested
        @DisplayName("product 가 null 이 아닌 경우")
        class Context_with_call {

            @Test
            @DisplayName("repository 의 insert 메서드를 호출한다")
            void it_throw_IllegalArgumentException() {
                final var product = new Product.Builder(0)
                        .name("colombia")
                        .category(Category.COFFEE_BEAN_PACKAGE)
                        .price(1000)
                        .build();

                productService.saveProduct(product);
                verify(productRepository).insert(any());
            }
        }
    }

    @Nested
    @DisplayName("findAllProduct 메서드는")
    class Describe_findAllProduct {

        @Nested
        @DisplayName("호출 된 경우")
        class Context_withCall {

            @Test
            @DisplayName("저장된 모든 제품 List 를 반환한다")
            void it_returnAllProductList() {
                final var product1 = new Product(1L, "product1", Category.COFFEE_GRINDER, 10000, "");
                final var product2 = new Product(2L, "product2", Category.COFFEE_GRINDER, 12000, "");
                final List<Product> products = new ArrayList<>(Arrays.asList(product1, product2));

                when(productRepository.selectAll()).thenReturn(products);

                final var findProducts = productService.findAllProduct();
                assertThat(findProducts.size(), is(2));
            }
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class Describe_findById {

        @Nested
        @DisplayName("존재하는 엔티티에 대한 요청인 경우")
        class Context_withExistingEntity {

            @Test
            @DisplayName("해당 엔티티를 반환한다")
            void it_product_list() {
                final var product = new Product.Builder(1L)
                        .name("test")
                        .category(Category.COFFEE_GRINDER)
                        .price(1000)
                        .build();

                when(productRepository.selectById(anyLong())).thenReturn(Optional.of(product));

                final var findProducts = productService.findById(1L);
                assertThat(findProducts, samePropertyValuesAs(product));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 엔티티에 대한 요청인 경우")
        class Context_withNotExistingEntity {

            @Test
            @DisplayName("ResourceNotFound 에러를 발생시킨다")
            void it_product_list() {
                when(productRepository.selectById(anyLong())).thenReturn(Optional.empty());
                assertThrows(ResourceNotFound.class, () -> productService.findById(1L));
            }
        }
    }

    @Nested
    @DisplayName("removeProduct 메서드는")
    class Describe_removeProduct {

        @Nested
        @DisplayName("호출된 경우")
        class Context_withCall {

            @Test
            @DisplayName("repository 의 deleteById 메서드를_호출한다")
            void It_call_deleteById() {
                productService.removeProduct(1);
                verify(productRepository).deleteById(anyLong());
            }
        }
    }
}
