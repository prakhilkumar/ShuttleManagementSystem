package com.shuttlemanagementsystem.ShuttleManagementSystem.repository;

import com.shuttlemanagementsystem.ShuttleManagementSystem.model.Route;
import com.shuttlemanagementsystem.ShuttleManagementSystem.model.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {
    List<RouteStop> findByRouteOrderBySequence(Route route);
}
