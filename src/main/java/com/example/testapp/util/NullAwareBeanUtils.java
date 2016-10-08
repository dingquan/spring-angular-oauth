package com.example.testapp.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class NullAwareBeanUtils extends org.apache.commons.beanutils.BeanUtilsBean2 {
	private Set<String> ignoreProperties = new HashSet<String>();
	private Set<String> nullableProperties = new HashSet<String>();
	
	public NullAwareBeanUtils() {
		super();
		// never copy Ids over
		this.ignoreProperties.add("id");
		this.ignoreProperties.add("createdAt");
		this.ignoreProperties.add("updatedAt");
		this.ignoreProperties.add("owner");
		this.ignoreProperties.add("user");
		this.ignoreProperties.add("password");
	}
	
	public NullAwareBeanUtils(String... ignoreProps) {
		super();
		for (String prop : ignoreProps) {
			this.ignoreProperties.add(prop);
		}
		// never copy Ids over
		this.ignoreProperties.add("id");
		this.ignoreProperties.add("createdAt");
		this.ignoreProperties.add("updatedAt");
		this.ignoreProperties.add("owner");
		this.ignoreProperties.add("user");
		this.ignoreProperties.add("password");
	}
	
	// set the list of properties for which nulls should be copied over
	public void setNullableProperties(String... nullableProperties) {
		this.nullableProperties.addAll(Arrays.asList(nullableProperties));
	}
	
	@Override
	public void copyProperty(Object bean, String name, Object value) {
		
		if (value == null && !nullableProperties.contains(name)) {
			return;
		}
		if (ignoreProperties.contains(name)) {
			return;
		}
		try {
			super.copyProperty(bean, name, value);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Error while copying bean properties.", e);
		}
	}

	@Override
	public void copyProperties(Object dest, Object orig) {
		try {
			super.copyProperties(dest, orig);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Error while copying bean properties.", e);
		}
	}
}
