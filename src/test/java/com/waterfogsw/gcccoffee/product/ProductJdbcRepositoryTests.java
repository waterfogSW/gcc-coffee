package com.waterfogsw.gcccoffee.product;

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
import static org.mockito.Mockito.mock;


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

    NamedParameterJdbcTemplate jdbcTemplate = mock(NamedParameterJdbcTemplate.class);

    @Test
    @Order(1)
    @DisplayName("datasource 확인")
    public void testHikariConnectionPool() {
        assertThat(dataSource.getClass().getName(), is("com.zaxxer.hikari.HikariDataSource"));
    }

    @Nested
    @Order(2)
    @DisplayName("insert 메서드는")
    class Describe_save {

        @Nested
        @DisplayName("product 인자 가 null 이면")
        class Context_with_null_argument {

            @Test
            @DisplayName("IllegalArgumentException 예외를 발생시킨다")
            void it_throw_error() {
                assertThrows(IllegalArgumentException.class, () -> productJdbcRepository.insert(null));
            }
        }

        @Nested
        @DisplayName("product 가 정상적으로 저장되면")
        class Context_with_voucher_saved {

            @Test
            @Transactional
            @DisplayName("예외가 발생하지 않는다")
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
    @DisplayName("selectAll 메서드는")
    class Describe_findAll {

        @Nested
        @DisplayName("호출되면")
        class Context_with_call {

            @Test
            @Transactional
            @DisplayName("저장된 모든 상품 리스트를 반환한다")
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
    @DisplayName("selectById 메서드는")
    class Describe_selectById {

        @Nested
        @DisplayName("존재하는 엔티티를 조회하면")
        class Context_with_exist_Entity {

            @Test
            @Transactional
            @DisplayName("해당 엔티티를 반환한다")
            void it_return_entity() {
                final var product = new Product(0, "product1", Category.COFFEE_GRINDER, 10000, "");

                productJdbcRepository.insert(product);

                final var selectProduct = productJdbcRepository.selectById(1L);
                assertThat(selectProduct.isPresent(), is(true));
            }
        }

        @Nested
        @DisplayName("존재하지않는 엔티티를 조회하면")
        class Context_with_not_exist_Entity {

            @Test
            @Transactional
            @DisplayName("Optional.empty 를 반환한다")
            void it_return_entity() {
                final var selectProduct = productJdbcRepository.selectById(1L);
                assertThat(selectProduct.isEmpty(), is(true));
            }
        }
    }

    @Nested
    @Order(5)
    class deleteById_메서드는 {

        @Nested
        class 호출되면 {

            @Test
            @Transactional
            void 해당_id_값의_레코드를_삭제한다() {
                final var product = new Product(0, "product1", Category.COFFEE_GRINDER, 10000, "");

                productJdbcRepository.insert(product);

                final var findProduct = productJdbcRepository.selectById(1);
                assertThat(findProduct.isPresent(), is(true));

                productJdbcRepository.deleteById(1);
                final var findProductAfterDelete = productJdbcRepository.selectById(1);
                assertThat(findProductAfterDelete.isEmpty(), is(true));
            }
        }
    }
}
