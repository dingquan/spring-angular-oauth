package com.example.testapp.controller;


import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.testapp.model.Blog;
import com.example.testapp.model.User;
import com.example.testapp.security.AuthenticatedUser;
import com.example.testapp.service.BlogService;
import com.example.testapp.service.UserService;
import com.example.testapp.util.StringUtil;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BlogService blogService;
	


    @RequestMapping(value = "", method = RequestMethod.POST)
//    @PreAuthorize("isAnonymous()")
    public ResponseEntity<User> createUser(@RequestBody @Valid final User user) {

    	log.info("create user. user=" + StringUtil.toJson(user));
    
    	User savedUser = userService.createUser(user);
    	log.info("User created successfully. userId=" + user.getId());
    	return ResponseEntity.ok(savedUser);
    }
    
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<User> getMyAccount(@AuthenticationPrincipal AuthenticatedUser currentUser) {
		log.info("getMyAccount. currentUser=" + StringUtil.nullSafeString(currentUser.getId()));
    	User user = userService.findById(currentUser.getId());
    	if (user == null) {
    		throw new EntityNotFoundException("User not found. userId=" + StringUtil.nullSafeString(currentUser.getId()));
    	}  else {
    		return ResponseEntity.ok(user);
    	}
    }


    @PreAuthorize("(isFullyAuthenticated() and (authentication.principal.id == #user.id))")
    @RequestMapping(value = "/me", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@AuthenticationPrincipal AuthenticatedUser currentUser, @RequestBody @Valid final User user) {
    	String userId = currentUser.getId();
    	log.info("update user with id=" + StringUtil.nullSafeString(userId));
    	User savedUser = userService.updateUser(user);
    	return ResponseEntity.ok(savedUser);
    }
    
    
    @PreAuthorize("(isFullyAuthenticated() and (authentication.principal.id == #user.id))")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
    	log.info("delete user with id=" + id);
    	try {
    		userService.delete(id);
    		log.info("User deleted successfully. userId=" + id);
    		return ResponseEntity.ok().body(null);
    	} catch (EmptyResultDataAccessException e) {
    		String error = "Delete user failed. user not found. userId=" + id;
    		log.error(error);
    		throw new EntityNotFoundException(error);
    	}
    }
    
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> getUserById(@PathVariable String id) {
		log.info("get user with id=" + id);
		User user = userService.findById(id);
		if (user != null) {
			log.info("User fetched successfully. userId=" + user.getId());
			return ResponseEntity.ok(user);
		} else {
			String error = "Error fetching user. User not found. userId=" + id;
			log.error(error);
			throw new EntityNotFoundException(error);
		}
	}

	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/me/blogs", method = RequestMethod.GET)
	public ResponseEntity<Page<Blog>> getMyBlogs(@AuthenticationPrincipal AuthenticatedUser currentUser,
												@PageableDefault(size = 20) Pageable page) {
		String userId = currentUser.getId();
		log.info("Get blogs for user. userId=" + StringUtil.nullSafeString(userId));
		
		Page<Blog> blogs = blogService.findMyBlogs(page);
		log.info("My blogs fetched successfully");
		
		return ResponseEntity.ok(blogs);
	}
	


}
