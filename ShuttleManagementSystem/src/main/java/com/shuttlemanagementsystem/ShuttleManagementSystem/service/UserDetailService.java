package com.shuttlemanagementsystem.ShuttleManagementSystem.service;


import com.shuttlemanagementsystem.ShuttleManagementSystem.model.User;
import com.shuttlemanagementsystem.ShuttleManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		final User user = userRepo.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        UserDetails userDetails = new UserDetails(user);
        return userDetails;
	}
	
	
}
