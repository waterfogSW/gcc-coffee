package com.waterfogsw.gcccoffee.order;

import com.waterfogsw.gcccoffee.order.model.Email;
import com.waterfogsw.gcccoffee.order.model.OrderProduct;
import com.waterfogsw.gcccoffee.order.repository.OrderJdbcRepository;
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
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Sql(scripts = {"classpath:sql/testTableInit.sql"})
@Sql(scripts = {"classpath:sql/testTableRemove.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringJUnitConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderJdbcRepositoryTests {
    @Autowired
    OrderJdbcRepository orderJdbcRepository;

    @Autowired
    ProductJdbcRepository productJdbcRepository;

    @Autowired
    DataSource dataSource;

    @Configuration
    @ComponentScan(
            basePackages = {"com.waterfogsw.gcccoffee"}
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
    @DisplayName("datasource 확인")
    public void testHikariConnectionPool() {
        assertThat(dataSource.getClass().getName(), is("com.zaxxer.hikari.HikariDataSource"));
    }

    @Nested
    @Order(2)
    @DisplayName("insert 메서드는")
    class Describe_save {

        @Nested
        @DisplayName("order 인자 가 null 이면")
        class Context_with_null_argument {

            @Test
            @DisplayName("IllegalArgumentException 예외를 발생시킨다")
            void it_throw_error() {
                assertThrows(IllegalArgumentException.class, () -> orderJdbcRepository.insert(null));
            }
        }

        @Nested
        @DisplayName("order 가 정상적으로 저장되면")
        class Context_with_order_saved {

            @Test
            @Transactional
            @DisplayName("저장한 엔티티를 리턴한다")
            void it_return_saved_voucher() {
                final var product1 = new Product.Builder(0).name("test")
                        .category(Category.COFFEE_GRINDER)
                        .price(1000)
                        .build();

                final var product2 = new Product.Builder(0).name("test")
                        .category(Category.COFFEE_GRINDER)
                        .price(1000)
                        .build();

                productJdbcRepository.insert(product1);
                productJdbcRepository.insert(product2);

                final var selected1 = productJdbcRepository.selectById(1);
                final var selected2 = productJdbcRepository.selectById(2);

                assertThat(selected1.isPresent(), is(true));
                assertThat(selected2.isPresent(), is(true));

                final var orderProduct1 = new OrderProduct(1, Category.COFFEE_GRINDER, 1000, 1);
                final var orderProduct2 = new OrderProduct(2, Category.COFFEE_GRINDER, 1000, 1);
                final var orderProducts = new ArrayList<>(Arrays.asList(orderProduct1, orderProduct2));
                final var order = new com.waterfogsw.gcccoffee.order.model
                        .Order(new Email("test@naver.com"), "풍덕천동", "111-111", orderProducts);
                final var insertedProduct = orderJdbcRepository.insert(order);

                assertThat(insertedProduct.getId(), is(1L));
            }
        }
    }
}
