package com.elgregos.security.authentication;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class PersonalGroup implements Group {

	private final String name;

	private final Map<String, Principal> members;

	public PersonalGroup(final String name) {
		this.name = name;
		this.members = new HashMap<>();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean addMember(final Principal user) {
		final boolean isMember = this.members.containsKey(user.getName());
		if (!isMember) {
			this.members.put(user.getName(), user);
		}
		return !isMember;
	}

	@Override
	public boolean removeMember(final Principal user) {
		return this.members.remove(user.getName()) != null;
	}

	@Override
	public boolean isMember(final Principal member) {
		return this.members.containsKey(member.getName());
	}

	@Override
	public Enumeration<? extends Principal> members() {
		return Collections.enumeration(this.members.values());
	}

}
