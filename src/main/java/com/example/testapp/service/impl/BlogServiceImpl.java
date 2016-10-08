package com.example.testapp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.testapp.dao.BlogRepository;
import com.example.testapp.exception.BadRequestException;
import com.example.testapp.model.Blog;
import com.example.testapp.model.User;
import com.example.testapp.service.BlogService;
import com.example.testapp.util.NullAwareBeanUtils;

@Service
public class BlogServiceImpl extends BaseServiceImpl implements BlogService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private BlogRepository blogRepository;
	
	@Override
	public Page<Blog> findAllPublishedBlogs(Pageable page) {
		Page<Blog> blogs = blogRepository.findByStatusOrderByCreatedAtDesc(Blog.Status.PUBLISHED, page);
		List<Blog> results = removeSentitiveInfo(blogs.getContent());
		return new PageImpl<Blog>(results, page, blogs.getTotalElements());
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
		if (blog.getId() != null && !id.equals(blog.getId())) {
			throw new BadRequestException("Failed to update blog. IDs not matching between the request and the body");
		}
		Blog dbBlog = blogRepository.findOne(id);
		if (dbBlog == null) {
			String error = "Failed to update blog. Blog doesn't exist. id=" + id;
			log.error(error);
			throw new BadRequestException(error);
		}
		
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
		List<Blog> results = removeSentitiveInfo(blogs.getContent());
		return new PageImpl<Blog>(results, page, blogs.getTotalElements());
	}

	@Override
	@Transactional
	public Blog changeStatus(String id, Blog.Status status) {
		Blog blog = blogRepository.findOne(id);
		if (blog == null) {
			String error = "Failed to update blog. Blog doesn't exist. blogId=" + id;
			log.error(error);
			throw new BadRequestException(error);
		}
		if (Blog.Status.PUBLISHED.equals(status)) {
			Set<ConstraintViolation<Blog>> errors = validator.validate(blog);
			if (errors.size() > 0) {
				log.error("Error publishing blog. blogId=" + id + ", error=" + errors.toString());
				throw new BadRequestException(errors.toString());
			}
		}
		blog.setStatus(status);
		return blogRepository.save(blog);
	}

	private List<Blog> removeSentitiveInfo(List<Blog> blogs) {
		List<Blog> results = new ArrayList<Blog>();
		for (Blog blog : blogs) {
			results.add(blog.publicClone());
		}
		return results;
	}

	@Override
	public Page<Blog> findPromotedBlogs(Pageable page) {
		log.info("Find recommended blogs");
		Page<Blog> blogs = blogRepository.findByPromotedAndStatusOrderByCreatedAtDesc(true, Blog.Status.PUBLISHED, page);
		log.info("Find recommended blogs succeeded. numBlogs=" + blogs.getSize());
		return blogs;
	}
}
