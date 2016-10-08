package com.example.testapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.testapp.dao.UserRepository;
import com.example.testapp.exception.UserNotLoggedInException;
import com.example.testapp.model.User;
import com.example.testapp.security.AuthenticatedUser;
import com.example.testapp.service.BaseService;

@Component
public class BaseServiceImpl implements BaseService{

	@Autowired
	UserRepository userRepository;

	@Override
	public User getCurrentUserIfExists() {
		User user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof AuthenticatedUser) {
				String id = ((AuthenticatedUser) principal).getId();
				user = userRepository.findOne(id);
			}
		}
		return user;
	}

	@Override
	public User getCurrentUser() {
		User user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof AuthenticatedUser) {
				String id = ((AuthenticatedUser) principal).getId();
				user = userRepository.findOne(id);
			}
		}
		if (user == null) {
			throw new UserNotLoggedInException("User not logged in");
		}
		return user;
	}
	
}
