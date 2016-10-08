package com.example.testapp.service;

import com.example.testapp.model.User;

public interface BaseService {

	public User getCurrentUserIfExists();
	
	public User getCurrentUser();
}
