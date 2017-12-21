package com.easysoft.finance.repository;

import com.easysoft.finance.domain.StockAnalysisDuration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

//@RepositoryRestResource
public interface StockAnalysisDurationRepository extends CrudRepository<StockAnalysisDuration, Long> {
    @Query("select c from StockAnalysisDuration c where c.startDate = :startDate and c.endDate = :endDate")
    StockAnalysisDuration findByDuration(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    // custom query example and return a stream
//    @Query("select c from Stock c where c.symbol = :symbol")
//    Stock findBySymbol(@Param("symbol") String symbol);

}
