package com.example.testapp.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class AuthenticatedUser extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 7433324124354381923L;
	
	private String id;
	private String email;
	
	public AuthenticatedUser(String id, String email, String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = id;
		this.email = email;
	}
	
	public AuthenticatedUser(String id, String email, String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, true, true, true, true, authorities);
		this.id = id;
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
