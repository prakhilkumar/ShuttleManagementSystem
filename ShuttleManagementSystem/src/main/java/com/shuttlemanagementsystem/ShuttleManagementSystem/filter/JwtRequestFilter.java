package com.shuttlemanagementsystem.ShuttleManagementSystem.filter;


import com.shuttlemanagementsystem.ShuttleManagementSystem.service.UserDetailService;
import com.shuttlemanagementsystem.ShuttleManagementSystem.service.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailService customerDetailService;
	@Autowired
	private UserTokenService userTokenService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		try {
			final String requestTokenHeader = request.getParameter("authToken");
			System.out.println("########## requestTokenHeader ######### ");
			System.out.println(requestTokenHeader);
			Enumeration<String> headers = request.getHeaderNames();
//			while(headers.hasMoreElements()) {
//				String headerName = headers.nextElement();
//				String value = request.getHeader(headerName);
//				System.out.println(headerName +" : "+value);
//			}
			String username = requestTokenHeader!=null ? userTokenService.getUserNameFromToken(requestTokenHeader) : null;
			if (username != null) {
				UserDetails userDetails = this.customerDetailService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
				.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

			}
		}catch(Exception exp) {
			exp.printStackTrace();
		}
		chain.doFilter(request, response);
	}

}
