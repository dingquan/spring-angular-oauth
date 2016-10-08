package com.example.testapp.service.impl;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.testapp.dao.BlogRepository;
import com.example.testapp.model.Blog;
import com.example.testapp.model.User;
import com.example.testapp.service.BlogService;
import com.example.testapp.util.NullAwareBeanUtils;

@Service
public class BlogServiceImpl extends BaseServiceImpl implements BlogService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	
	@Autowired
	private BlogRepository blogRepository;
	
	@Override
	public Page<Blog> findAllPublishedBlogs(Pageable page) {
		Page<Blog> blogs = blogRepository.findByStatusOrderByCreatedAtDesc(Blog.Status.PUBLISHED, page);
		return blogs;
	}

	@Override
	@Transactional
	public Blog createBlog(Blog blog) {
		User currentUser = getCurrentUser();

		blog.setOwner(currentUser);
		return blogRepository.save(blog);
	}

	@Override
	@Transactional
	public void deleteBlog(String id) {
		Blog blog = blogRepository.findOne(id);
		if (blog == null) {
			throw new EntityNotFoundException("Blog with id doesn't exist. blogId=" + id);
		}
		
		blogRepository.delete(blog);
	}

	@Override
	@Transactional
	public Blog getBlog(String id) {
		Blog blog = blogRepository.findOne(id);
		if (blog == null) {
			throw new EntityNotFoundException("Blog with given id not found. blogId=" + id);
		}
		
		blog.setViewCount(blog.getViewCount() + 1);
		blog = blogRepository.save(blog);
		
		User currentUser = getCurrentUserIfExists();
		return blog.publicClone();
	}

	@Override
	@Transactional
	public Blog updateBlog(String id, Blog blog) {

		Blog dbBlog = blogRepository.findOne(id);
		
		User currentUser = getCurrentUser();
		
		NullAwareBeanUtils beanUtils = new NullAwareBeanUtils("viewCount", "likeCount", "commentCount", "status");
		beanUtils.copyProperties(dbBlog, blog);
		dbBlog = blogRepository.save(dbBlog);
		
		return dbBlog;
	}

	@Override
	public Page<Blog> findMyBlogs(Pageable page) {
		User user = getCurrentUser();
		Page<Blog> blogs = blogRepository.findByOwnerOrderByCreatedAtDesc(user, page);
		return blogs;
	}

	@Override
	@Transactional
	public Blog changeStatus(String id, Blog.Status status) {
		Blog blog = blogRepository.findOne(id);
		blog.setStatus(status);
		return blogRepository.save(blog);
	}

}
