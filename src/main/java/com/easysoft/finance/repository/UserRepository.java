package com.easysoft.finance.repository;

import com.easysoft.finance.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(@Param("username") String username);

//    Page<Stock> findStocksByUser(Long userId, Pageable pageable);


}
