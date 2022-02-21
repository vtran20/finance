package com.easysoft.finance.repository;

import com.easysoft.finance.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//https://docs.spring.io/spring-data/jpa/docs/2.4.2/reference/html/#jpa.query-methods
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    // custom query example and return a stream
    @Query("select c from Stock c where c.symbol = :symbol")
    Optional<Stock> findBySymbol(@Param("symbol") String symbol);

}

