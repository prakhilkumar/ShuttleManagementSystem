package com.shuttlemanagementsystem.ShuttleManagementSystem.repository;

import com.shuttlemanagementsystem.ShuttleManagementSystem.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
}
