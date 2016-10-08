package com.example.testapp.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.testapp.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, String>{
	
	public User findByEmail(String email);
	
	public User findByUsername(String username);
	
	public User findOneByResetPwdToken(String token);
}
