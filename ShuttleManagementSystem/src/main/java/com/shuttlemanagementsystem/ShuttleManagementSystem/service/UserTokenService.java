package com.shuttlemanagementsystem.ShuttleManagementSystem.service;


import com.shuttlemanagementsystem.ShuttleManagementSystem.dto.TokenResponse;
import com.shuttlemanagementsystem.ShuttleManagementSystem.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTokenService {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	public TokenResponse createToken(String userName) {
		return jwtTokenUtil.generateToken(userName);
	}
	
	public String getUserNameFromToken(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		if(jwtTokenUtil.isTokenExpired(token))
			throw new RuntimeException("Token is expired");
		return jwtTokenUtil.getUsernameFromToken(token);
	}

}
