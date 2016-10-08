package com.example.testapp.service.impl;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.stereotype.Service;

import com.example.testapp.exception.EntityNotFoundException;
import com.example.testapp.model.User;
import com.example.testapp.service.UserService;
import com.example.testapp.util.NullAwareBeanUtils;
import com.example.testapp.util.StringUtil;

@Service
public class UserServiceImpl extends BaseServiceImpl implements UserService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Value("${security.oauth2.client.client-id}")
	private String clientId;
	
	
	@Autowired
	private AuthorizationServerConfigurer authServer;
	
	@Override
	public User findById(String id) {
		User user = userRepository.findOne(id);
		return user;
	}

	@Override
	@Transactional
	public User createUser(User user) {
		log.info("Create user. email=" + user.getEmail() + ", display_name=" + user.getDisplayName());
		log.info("Create user success. email=" + user.getEmail() + ", display_name=" + user.getDisplayName());
		user = userRepository.save(user);
		return user;
	}

	@Override
	@Transactional
	public void delete(String id) {
		userRepository.delete(id);
	}


	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	@Transactional
	public User updateUser(User user) {
		log.info("Update user. user=" + StringUtil.toJson(user));
    	String id = user.getId();
		User dbUser = userRepository.findOne(id);
		if (dbUser != null) {
			NullAwareBeanUtils beanUtils = new NullAwareBeanUtils("password", "roles");
			beanUtils.copyProperties(dbUser, user);
			dbUser = userRepository.save(dbUser);
			log.info("User updated successfully. userId=" + id);
			return dbUser;
		} else {
			String error = "Error updating user. User with the id not found. userId=" + id;
			log.error(error);
			throw new EntityNotFoundException(error);
		}
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

}
