package com.example.testapp.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.testapp.model.Blog;
import com.example.testapp.model.User;
import com.example.testapp.model.Blog.Status;

public interface BlogRepository extends PagingAndSortingRepository<Blog, String> {

	public Page<Blog> findByStatusOrderByCreatedAtDesc(Status status, Pageable page);
	
	public Page<Blog> findByOwnerOrderByCreatedAtDesc(User owner, Pageable page);
	
	public Page<Blog> findByPromotedAndStatusOrderByCreatedAtDesc(Boolean promoted, Blog.Status status, Pageable page);
}
