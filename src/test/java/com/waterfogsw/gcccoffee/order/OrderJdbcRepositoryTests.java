package com.waterfogsw.gcccoffee.order;

import com.waterfogsw.gcccoffee.order.model.OrderProduct;
import com.waterfogsw.gcccoffee.order.repository.OrderJdbcRepository;
import com.waterfogsw.gcccoffee.product.model.Category;
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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderJdbcRepositoryTests {
    @Autowired
    OrderJdbcRepository orderJdbcRepository;

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
    class Describe_insert {

        @Nested
        @DisplayName("order 인자 가 null 인 경우")
        class Context_withNullArgument {

            @Test
            @DisplayName("IllegalArgumentException 예외를 발생시킨다")
            void it_throw_error() {
                assertThrows(IllegalArgumentException.class, () -> orderJdbcRepository.insert(null));
            }
        }

        @Nested
        @DisplayName("order 가 정상적으로 저장된 경우")
        class Context_withOrderSaved {

            @Test
            @Sql(scripts = {"classpath:sql/testTableInit.sql", "classpath:sql/productSample.sql"})
            @Sql(scripts = {"classpath:sql/testTableRemove.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
            @Transactional
            @DisplayName("예외가 발생하지 않는다")
            void it_returnSavedVoucher() {
                final var orderProduct1 = new OrderProduct(1, Category.COFFEE_GRINDER, 1000, 1);
                final var orderProduct2 = new OrderProduct(2, Category.COFFEE_GRINDER, 1000, 1);
                final var orderProducts = new ArrayList<>(Arrays.asList(orderProduct1, orderProduct2));
                final var order = new com.waterfogsw.gcccoffee.order.model
                        .Order("test@naver.com", "풍덕천동", "111-111", orderProducts);

                assertDoesNotThrow(() -> orderJdbcRepository.insert(order));
            }
        }
    }
}
