package com.elgregos.security.authentication;

import java.io.Serializable;
import java.security.Principal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(doNotUseGetters = true, of = { "name" })
@ToString(doNotUseGetters = true, of = { "name" })
public class PersonalPrincipal implements Principal, Serializable {

	private static final long serialVersionUID = 1L;

	private final String name;

	public PersonalPrincipal(final String name) {
		this.name = name;
	}
}
