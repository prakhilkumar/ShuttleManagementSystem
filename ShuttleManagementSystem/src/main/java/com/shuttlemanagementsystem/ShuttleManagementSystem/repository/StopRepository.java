package com.shuttlemanagementsystem.ShuttleManagementSystem.repository;

import com.shuttlemanagementsystem.ShuttleManagementSystem.model.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopRepository extends JpaRepository<Stop, Long> {
}
