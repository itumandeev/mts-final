package ru.mts.siebel.assembly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mts.siebel.assembly.entity.AssemblyOpty;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssemblyOptyRepository extends JpaRepository<AssemblyOpty, String> {

    List<AssemblyOpty> findByStatus(String status);

    Optional<AssemblyOpty> findByOrderId(String id);
}
