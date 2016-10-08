package com.example.testapp.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.testapp.model.User;
import com.example.testapp.service.UserService;

public class DatabaseUserServiceDetails implements UserDetailsService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;
	
	@Override
	/** We use email address to login. So username for oauth is really the email address here */
//	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//		log.info("find user by email: " + email);
//		User user = userService.findByEmail(email);
//		if (user == null) {
//			log.info("user not found with email=" + email);
//			throw new UsernameNotFoundException("User with given credential doesn't exist");
//		}
//		List<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
//
//		log.info("User found with email=" + email + ", firstName=" + user.getFirstName() + ", lastName=" + user);
//		return new AuthenticatedUser(user.getId(), user.getEmail(), user.getEmail(), user.getPassword(), auth);
//	}
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("find user by email: " + username);
		User user = userService.findByUsername(username);
		if (user == null) {
			log.info("user not found with username=" + username);
			throw new UsernameNotFoundException("User with given credential doesn't exist");
		}
		List<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();

		log.info("User found with email=" + username + ", firstName=" + user.getFirstName() + ", lastName=" + user);
		return new AuthenticatedUser(user.getId(), user.getEmail(), user.getEmail(), user.getPassword(), auth);
	}

}
