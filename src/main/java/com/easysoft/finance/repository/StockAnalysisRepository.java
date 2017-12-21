package com.easysoft.finance.repository;

import com.easysoft.finance.domain.StockAnalysis;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

//@RepositoryRestResource
public interface StockAnalysisRepository extends CrudRepository<StockAnalysis, Long> {
    @Query("select c from StockAnalysis c where c.stockAnalysisDuration.id = :id")
    List<StockAnalysis> findByDuration(Long id);

    // custom query example and return a stream
//    @Query("select c from Stock c where c.symbol = :symbol")
//    Stock findBySymbol(@Param("symbol") String symbol);

}
