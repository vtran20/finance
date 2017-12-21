package com.easysoft.finance.repository;

import com.easysoft.finance.domain.StockDailyPrice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface StockDailyPriceRepository extends CrudRepository<StockDailyPrice, Long> {

    // custom query example and return a stream
    @Query("select c from StockDailyPrice c where c.symbol = :symbol and c.date = :date")
    StockDailyPrice findBySymbolAnDate(@Param("symbol") String symbol, @Param("date") Date date);

    @Query("select c from StockDailyPrice c where c.symbol = :symbol and c.date >= :startDate and c.date <= :endDate order by c.date desc ")
    List<StockDailyPrice> findBySymbolAnDate(@Param("symbol") String symbol, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select c.price from StockDailyPrice c where c.symbol = :symbol and c.date >= :startDate and c.date <= :endDate order by c.date asc ")
    List<Double> findPriceBySymbolAndDate(@Param("symbol") String symbol, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

//    @Query("select c.price from StockDailyPrice c where c.symbol = :symbol order by c.date desc LIMIT :topNumber")
//    List<Double> findPriceBySymbolAndDate(@Param("symbol") String symbol, @Param("topNumber") int topNumber);

}
