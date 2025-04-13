package com.shuttlemanagementsystem.ShuttleManagementSystem.service;

import com.shuttlemanagementsystem.ShuttleManagementSystem.dto.StopCreationDTO;
import com.shuttlemanagementsystem.ShuttleManagementSystem.model.Stop;
import com.shuttlemanagementsystem.ShuttleManagementSystem.model.User;
import com.shuttlemanagementsystem.ShuttleManagementSystem.repository.StopRepository;
import com.shuttlemanagementsystem.ShuttleManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StopService {
    public final StopRepository stopRepository;
    @Autowired
    public StopService(StopRepository stopRepository){
        this.stopRepository=stopRepository;
    }

     public Stop createStop(StopCreationDTO dto){
        Stop stop=new Stop();
        stop.setName(dto.getName());
        stop.setAddress(dto.getAddress());
        stop.setLatitude(dto.getLatitude());
        stop.setLongitude(dto.getLongitude());
        stopRepository.save(stop);
        return stop;
     }

}
