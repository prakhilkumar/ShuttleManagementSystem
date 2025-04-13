package com.shuttlemanagementsystem.ShuttleManagementSystem.repository;

import com.shuttlemanagementsystem.ShuttleManagementSystem.model.Shuttle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShuttleRepository extends JpaRepository<Shuttle, Long> {
}
