package com.example.testapp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.testapp.model.User;

public interface UserService {

	public User findById(String id);
	
	public User createUser(User user);

	public User updateUser(User user);
	
	public void delete(String id);
	

	public User findByEmail(String email);
	
	public User findByUsername(String username);

}
