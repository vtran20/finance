package com.easysoft.finance.repository;

import com.easysoft.finance.domain.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

}
