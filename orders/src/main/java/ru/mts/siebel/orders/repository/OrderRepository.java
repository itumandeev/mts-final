package ru.mts.siebel.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mts.siebel.orders.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
}
