package com.easysoft.finance.repository;

import com.easysoft.finance.domain.Stock;
import com.easysoft.finance.domain.StockDailyPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;

//@RepositoryRestResource
public interface StockRepository extends CrudRepository<Stock, Long> {
    // custom query example and return a stream
    @Query("select c from Stock c where c.symbol = :symbol")
    Stock findBySymbol(@Param("symbol") String symbol);

}
