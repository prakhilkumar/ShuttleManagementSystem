package com.shuttlemanagementsystem.ShuttleManagementSystem.model;


import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "start_stop_id", nullable = false)
    private Stop startStop;
    @ManyToOne
    @JoinColumn(name = "end_stop_id", nullable = false)
    private Stop endStop;

    @ManyToOne
    @JoinColumn(name = "shuttle_id", nullable = false)
    private Shuttle shuttle;


    @Column(name = "booking_time", nullable = false)
    private LocalDateTime bookingTime;

    @Column(name = "estimated_end_time")
    private LocalDateTime estimatedEndTime;


    @Column(name = "travel_date", nullable = false)
    private LocalDate travelDate;

    @Column(name = "fare", nullable = false, precision = 10, scale = 2)
    private BigDecimal fare;

    @Column(name = "points_deducted", nullable = false, precision = 10, scale = 2)
    private BigDecimal pointsDeducted;

    @Column(name = "status", nullable = false)
    private String status;

    public LocalDateTime getEstimatedEndTime() {
        return estimatedEndTime;
    }

    public void setEstimatedEndTime(LocalDateTime estimatedEndTime) {
        this.estimatedEndTime = estimatedEndTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Stop getStartStop() {
        return startStop;
    }

    public void setStartStop(Stop startStop) {
        this.startStop = startStop;
    }

    public Stop getEndStop() {
        return endStop;
    }

    public void setEndStop(Stop endStop) {
        this.endStop = endStop;
    }

    public Shuttle getShuttle() {
        return shuttle;
    }

    public void setShuttle(Shuttle shuttle) {
        this.shuttle = shuttle;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(LocalDate travelDate) {
        this.travelDate = travelDate;
    }

    public BigDecimal getFare() {
        return fare;
    }

    public void setFare(BigDecimal fare) {
        this.fare = fare;
    }

    public BigDecimal getPointsDeducted() {
        return pointsDeducted;
    }

    public void setPointsDeducted(BigDecimal pointsDeducted) {
        this.pointsDeducted = pointsDeducted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
