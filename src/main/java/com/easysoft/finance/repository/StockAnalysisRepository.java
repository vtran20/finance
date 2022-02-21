package com.easysoft.finance.repository;

import com.easysoft.finance.domain.StockAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockAnalysisRepository extends JpaRepository<StockAnalysis, Long> {
    @Query("select c from StockAnalysis c where c.stockAnalysisDuration.id = :id")
    List<StockAnalysis> findByDuration(@Param("id") Long id);

    @Query("select c from StockAnalysis c where c.stockAnalysisDuration.id = :id and c.symbol = :symbol")
    List<StockAnalysis> findByDurationBySymbol(@Param("id") Long id, @Param("symbol") String symbol);

    // custom query example and return a stream
//    @Query("select c from Stock c where c.symbol = :symbol")
//    Stock findBySymbol(@Param("symbol") String symbol);

}
