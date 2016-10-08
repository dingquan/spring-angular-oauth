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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.example.testapp.exception.BadRequestException;
import com.example.testapp.model.Blog;
import com.example.testapp.model.Blog.Status;
import com.example.testapp.security.AuthenticatedUser;
import com.example.testapp.service.BlogService;
import com.example.testapp.util.StringUtil;

@RestController
@RequestMapping(value = "/api/blogs")
public class BlogController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BlogService blogService;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder, WebRequest webRequest) {
		String autoSave = webRequest.getParameter("auto_save");
		// skip bean validation if the request has auto_save parameter set to true
		// to allow user save intermediate/unfinished data
	    if ("true".equalsIgnoreCase(autoSave)) {
	    	binder.setValidator(null);
	    }
	    if (binder.getTarget() instanceof Blog) {
	    	Blog blog = (Blog) binder.getTarget();
			// skip bean validation if the deal status is DRAFT
			// to allow user save intermediate/unfinished data
	    	if (Blog.Status.DRAFT.equals(blog.getStatus())) {
	    		binder.setValidator(null);
	    	}
	    }
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Page<Blog>> getBlogs(@PageableDefault(size = 20) Pageable page) {
		log.info("get all blogs");
		Page<Blog> blogs = blogService.findAllPublishedBlogs(page);
		log.info("Get all blogs successfull. numBlogs=" + blogs.getSize());
		return ResponseEntity.ok(blogs);
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Blog> createBlog(@AuthenticationPrincipal AuthenticatedUser currentUser,
											@RequestBody @Valid final Blog blog) {
		log.info("Create blog. title=" + blog.getTitle() + ", userId=" + StringUtil.nullSafeString(currentUser.getId()));
		if (blog.getId() != null) {
			String error = "Setting id property is not allowed when creating blogs";
			log.error(error);
			throw new BadRequestException(error);
		} else {
			Blog savedBlog = blogService.createBlog(blog);
			log.info("Blog saved successfully. blogId=" + blog.getId());
			return ResponseEntity.ok(savedBlog);
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Blog> getBlogById(@PathVariable String id) {
		log.info("Get blog for id=" + id);
		Blog blog = blogService.getBlog(id);
		log.info("Blog fetched successfully. blogId=" + id);
		return ResponseEntity.ok(blog);
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Blog> updateBlog(@PathVariable String id,
											@RequestBody @Valid final Blog blog) {
		log.info("Update blog. id=" + id);
		Blog savedBlog = blogService.updateBlog(id, blog);
		log.info("Blog updated successfully. id=" + id);
		return ResponseEntity.ok(savedBlog);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("isAuthenticated()")
	public void deleteBlog(@PathVariable String id) {
		log.info("delete blog for id=" + id);
		try {
			blogService.deleteBlog(id);
			log.info("Blog deleted successfully. blogId=" + id);
		} catch (EmptyResultDataAccessException e) {
			String error = "Error deleting blog. Blog with given id doesn't exist. blogId=" + id;
			log.error(error);
			throw new EntityNotFoundException(error);
		}
	}

	
    @RequestMapping(value = "/{id}/publish", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Blog> publishBlog(@PathVariable String id) {
    	log.info("publish Blog for id=" + id);
		Blog savedBlog = blogService.changeStatus(id, Status.PUBLISHED);
		log.info("Blog published successfully. blogId=" + id);
		return ResponseEntity.ok(savedBlog);
    }
    

}
