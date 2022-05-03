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
import static org.junit.jupiter.api.Assertions.assertThrows;


@Sql(scripts = {"classpath:sql/testTableInit.sql"})
@Sql(scripts= {"classpath:sql/testTableRemove.sql"}, executionPhase= Sql.ExecutionPhase.AFTER_TEST_METHOD)
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
            @DisplayName("저장한 엔티티를 리턴한다")
            void it_return_saved_voucher() {
                final var product = new Product.Builder(0)
                        .name("test1")
                        .category(Category.COFFEE_BEAN_PACKAGE)
                        .price(1000)
                        .description("test")
                        .build();

                final var insertedProduct = productJdbcRepository.insert(product);
                assertThat(insertedProduct.getId(), is(1L));
            }
        }
    }
}
