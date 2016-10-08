package com.example.testapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.testapp.model.Blog;
import com.example.testapp.model.Blog.Status;

public interface BlogService {

	public Page<Blog> findAllPublishedBlogs(Pageable page);
	
	public Blog createBlog(Blog blog);
	
	public void deleteBlog(String blogId);
	
	public Blog getBlog(String id);
	
	public Blog updateBlog(String id, Blog blog);
	
	public Page<Blog> findMyBlogs(Pageable page);

	public Blog changeStatus(String id, Status status);

}
