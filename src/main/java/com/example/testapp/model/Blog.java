package com.example.testapp.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.example.testapp.util.NullAwareBeanUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
public class Blog extends BaseModel implements Serializable{

	public enum Status {
		DRAFT("DRAFT"),
		PUBLISHED("PUBLISHED"),
		DELETED("DELETED");
		
		public final String status;
		
		Status(String status) {
			this.status = status;
		}
	}
	
	private static final long serialVersionUID = 1158676050780466022L;

	@Column
	@NotNull
	private String title;
	
	@Column
	@NotNull
	private String content;
	
	@Column
	@Enumerated(EnumType.STRING)
	private Status status = Status.DRAFT;
	
	@Column(name = "view_count")
	@JsonProperty(access = Access.READ_ONLY)
	private Integer viewCount = 0;
	
	@Column(name = "like_count")
	@JsonProperty(access = Access.READ_ONLY)
	private Integer likeCount = 0;
	
	@Column(name = "comment_count")
	@JsonProperty(access = Access.READ_ONLY)
	private Integer commentCount = 0;
	
	@Column
	private Boolean promoted = true;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User owner;
	
	@ElementCollection
	@CollectionTable(name="blog_image", joinColumns=@JoinColumn(name="blog_id"))
	@Column(name="image_path")
	private List<String> imagePaths;
	
	@Transient
	private Boolean liked = false;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<String> getImagePaths() {
		return imagePaths;
	}

	public void setImagePaths(List<String> imagePaths) {
		this.imagePaths = imagePaths;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public Integer getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}


	public Boolean getLiked() {
		return liked;
	}

	public void setLiked(Boolean liked) {
		this.liked = liked;
	}

	public Boolean getPromoted() {
		return promoted;
	}

	public void setPromoted(Boolean promoted) {
		this.promoted = promoted;
	}

	public Blog publicClone() {
		Blog b = new Blog();
		b.id = this.id;
		NullAwareBeanUtils beanUtils = new NullAwareBeanUtils();
		beanUtils.copyProperties(b, this);
		b.createdAt = this.createdAt;
		b.owner = this.owner.publicClone();
		return b;
	}
}
