package ru.mts.siebel.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mts.siebel.orders.entity.Storage;

@Repository
public interface StorageRepository extends JpaRepository<Storage, String> {
}
