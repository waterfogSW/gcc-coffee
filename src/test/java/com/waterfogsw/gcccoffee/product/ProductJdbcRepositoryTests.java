package com.waterfogsw.gcccoffee.product;

import com.waterfogsw.gcccoffee.common.exception.ResourceNotFound;
import com.waterfogsw.gcccoffee.product.model.Category;
import com.waterfogsw.gcccoffee.product.model.Product;
import com.waterfogsw.gcccoffee.product.repository.ProductJdbcRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Sql(scripts = {"classpath:sql/testTableInit.sql"})
@Sql(scripts = {"classpath:sql/testTableRemove.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringJUnitConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductJdbcRepositoryTests {
    @Autowired
    ProductJdbcRepository productJdbcRepository;

    @Autowired
    DataSource dataSource;

    @Configuration
    @ComponentScan(
            basePackages = {"com.waterfogsw.gcccoffee.product"}
    )
    static class Config {
        @Bean
        public DataSource dataSource() {
            return DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost:3306/test_gcc_coffee")
                    .username("root")
                    .password("02709580")
                    .type(HikariDataSource.class)
                    .build();
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }

        @Bean
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
            return new NamedParameterJdbcTemplate(jdbcTemplate);
        }

        @Bean
        public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }

        @Bean
        public TransactionTemplate transactionTemplate(PlatformTransactionManager platformTransactionManager) {
            return new TransactionTemplate(platformTransactionManager);
        }
    }

    @Test
    @Order(1)
    @DisplayName("datasource ??????")
    public void testHikariConnectionPool() {
        assertThat(dataSource.getClass().getName(), is("com.zaxxer.hikari.HikariDataSource"));
    }

    @Nested
    @Order(2)
    @DisplayName("insert ????????????")
    class Describe_save {

        @Nested
        @DisplayName("product ?????? ??? null ??? ??????")
        class Context_with_null_argument {

            @Test
            @DisplayName("IllegalArgumentException ????????? ???????????????")
            void it_throw_error() {
                assertThrows(IllegalArgumentException.class, () -> productJdbcRepository.insert(null));
            }
        }

        @Nested
        @DisplayName("product ??? ??????????????? ????????? ??????")
        class Context_with_voucher_saved {

            @Test
            @Transactional
            @DisplayName("????????? ???????????? ?????????")
            void it_return_saved_voucher() {
                final var product = new Product.Builder(0)
                        .name("test1")
                        .category(Category.COFFEE_BEAN_PACKAGE)
                        .price(1000)
                        .description("test")
                        .build();

                assertDoesNotThrow(() -> productJdbcRepository.insert(product));
            }
        }
    }

    @Nested
    @Order(3)
    @DisplayName("selectAll ????????????")
    class Describe_findAll {

        @Nested
        @DisplayName("????????????")
        class Context_with_call {

            @Test
            @Transactional
            @DisplayName("????????? ?????? ?????? ???????????? ????????????")
            void it_return_products() {
                final var product1 = new Product(0, "product1", Category.COFFEE_GRINDER, 10000, "");
                final var product2 = new Product(0, "product2", Category.COFFEE_GRINDER, 12000, "");

                productJdbcRepository.insert(product1);
                productJdbcRepository.insert(product2);

                final var selectProducts = productJdbcRepository.selectAll();
                assertThat(selectProducts.size(), is(2));
            }
        }
    }

    @Nested
    @Order(4)
    @DisplayName("selectById ????????????")
    class Describe_selectById {

        @Nested
        @DisplayName("???????????? ????????? ??? id ?????? ??????")
        class Context_with_exist_Entity {

            @Test
            @Transactional
            @DisplayName("?????? ???????????? ????????????")
            void it_return_entity() {
                final var product = new Product(0, "product1", Category.COFFEE_GRINDER, 10000, "");

                productJdbcRepository.insert(product);

                final var selectProduct = productJdbcRepository.selectById(1L);
                assertThat(selectProduct.isPresent(), is(true));
            }
        }

        @Nested
        @DisplayName("???????????? ?????? ????????? ??? id ?????? ??????")
        class Context_with_not_exist_Entity {

            @Test
            @Transactional
            @DisplayName("Optional.empty ??? ????????????")
            void it_return_entity() {
                final var selectProduct = productJdbcRepository.selectById(1L);
                assertThat(selectProduct.isEmpty(), is(true));
            }
        }
    }

    @Nested
    @Order(5)
    @DisplayName("deleteById ????????????")
    class describe_deleteById {

        @Nested
        @DisplayName("????????? ???????????? ???????????? id ?????? ??????")
        class context_withExistingEntity {

            @Test
            @Transactional
            @DisplayName("???????????? delete ?????? 1??? ????????????, ?????? ???????????? ?????????")
            void it_deleteEntity() {
                final var product = new Product(0, "product1", Category.COFFEE_GRINDER, 10000, "");

                productJdbcRepository.insert(product);

                final var findProduct = productJdbcRepository.selectById(1);
                assertThat(findProduct.isPresent(), is(true));

                productJdbcRepository.deleteById(1);

                final var findProductAfterDelete = productJdbcRepository.selectById(1);
                assertThat(findProductAfterDelete.isEmpty(), is(true));
            }
        }

        @Nested
        @DisplayName("????????? ???????????? ?????? ???????????? id ?????? ??????")
        class context_withNotExistingEntity {

            @Test
            @Transactional
            @DisplayName("ResourceNotFound ????????? ????????????")
            void it_throwResourceNotFound() {
                assertThrows(ResourceNotFound.class, () -> productJdbcRepository.deleteById(1));
            }
        }
    }
}
