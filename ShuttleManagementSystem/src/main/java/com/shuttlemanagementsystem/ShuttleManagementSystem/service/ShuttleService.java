package com.shuttlemanagementsystem.ShuttleManagementSystem.service;

import com.shuttlemanagementsystem.ShuttleManagementSystem.dto.ShuttleCreationDTO;
import com.shuttlemanagementsystem.ShuttleManagementSystem.model.Shuttle;
import com.shuttlemanagementsystem.ShuttleManagementSystem.repository.ShuttleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShuttleService {
    public final ShuttleRepository shuttleRepository;
    @Autowired
    public ShuttleService(ShuttleRepository shuttleRepository){
        this.shuttleRepository=shuttleRepository;
    }

    public Shuttle createShuttle(ShuttleCreationDTO dto){
        Shuttle shuttle=new Shuttle();
        shuttle.setCapacity(dto.getCapacity());
        shuttle.setAvgSpeed(dto.getAvgSpeed());
        shuttle.setCurrentLatitude(dto.getCurrentLatitude());
        shuttle.setCurrentLongitude(dto.getCurrentLongitude());
        shuttle.setVehicleNumber(dto.getVehicleNumber());
        shuttle.setCurrentStatus(dto.getCurrentStatus());
        shuttleRepository.save(shuttle);

        return shuttle;
    }
}
