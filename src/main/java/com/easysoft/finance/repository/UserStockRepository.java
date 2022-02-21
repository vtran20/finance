package com.easysoft.finance.repository;

import com.easysoft.finance.domain.UserStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserStockRepository extends JpaRepository<UserStock, Long> {
    @Query("select c from UserStock c where c.user.id = :userId")
    List<UserStock> findByUser(@Param("userId") Long userId);

    @Query("select c from UserStock c where c.user.id = :userId and c.symbol = :symbol")
    Optional<UserStock> findByUserAndStock(@Param("userId") Long userId, @Param("symbol") String symbol);
}
