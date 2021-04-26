package com.example.demo;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;

import java.time.Duration;

import static io.r2dbc.spi.ConnectionFactoryOptions.DATABASE;
import static io.r2dbc.spi.ConnectionFactoryOptions.DRIVER;
import static io.r2dbc.spi.ConnectionFactoryOptions.HOST;
import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;
import static io.r2dbc.spi.ConnectionFactoryOptions.PORT;
import static io.r2dbc.spi.ConnectionFactoryOptions.PROTOCOL;
import static io.r2dbc.spi.ConnectionFactoryOptions.USER;

@Configuration
@EnableR2dbcRepositories
@EnableTransactionManagement
@RequiredArgsConstructor
public class R2dbcPostgreSQLConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        final ConnectionFactory connectionFactory = ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(DRIVER, "pool")
                .option(PROTOCOL, "postgresql")
                .option(PORT, 5430)
                .option(HOST, "localhost")
                .option(USER, "root")
                .option(PASSWORD, "password")
                .option(DATABASE, "books")
                .option(Option.valueOf("preparedStatementCacheQueries"), 0)
                .build());

        final ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(connectionFactory)
                .initialSize(0)
                .maxSize(20)
                .maxIdleTime(Duration.ofMillis(2500))
                .maxCreateConnectionTime(Duration.ofSeconds(10))
                .acquireRetry(3)
                .maxAcquireTime(Duration.ofSeconds(7))
                .registerJmx(true)
                .name("r2dbc")
                .build();

        return new ConnectionPool(configuration);
    }

    @Bean
    public ReactiveDataAccessStrategy reactiveDataAccessStrategy() {

        return new DefaultReactiveDataAccessStrategy(new PostgresDialect());
    }


    @Bean
    public DatabaseClient databaseClient(final ConnectionFactory connectionFactory,
                                         final ReactiveDataAccessStrategy reactiveDataAccessStrategy) {

        return DatabaseClient.builder()
                .connectionFactory(connectionFactory)
                .dataAccessStrategy(reactiveDataAccessStrategy)
                .build();
    }

    @Bean
    public R2dbcRepositoryFactory r2dbcRepositoryFactory(final DatabaseClient databaseClient,
                                                         final ReactiveDataAccessStrategy reactiveDataAccessStrategy) {

        return new R2dbcRepositoryFactory(databaseClient, reactiveDataAccessStrategy);
    }

    @Bean
    public ReactiveTransactionManager reactiveTransactionManager(final ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean
    public TransactionalOperator transactionalOperator(final ReactiveTransactionManager reactiveTransactionManager) {
        return TransactionalOperator.create(reactiveTransactionManager);
    }

    @Bean
    public BookRepository processorBetResponseRepository(final R2dbcRepositoryFactory r2dbcRepositoryFactory) {
        return r2dbcRepositoryFactory.getRepository(BookRepository.class);
    }
}
