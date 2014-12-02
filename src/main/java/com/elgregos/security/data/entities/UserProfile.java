package com.elgregos.security.data.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Description : Utilisateur authentifié
 *
 * @author Grégo
 * @date 2 mai 2013
 */
@Entity
@Table(name = "USER_PROFILE")
@Cacheable(false)
@EqualsAndHashCode(exclude = { "password", "salt" })
@ToString(exclude = { "password", "salt" })
public class UserProfile implements Serializable {

	private static final long serialVersionUID = 2523960131359741511L;

	@Id
	@Column(unique = true, nullable = false, length = 128)
	@Getter
	@Setter
	private String email;

	@Column(nullable = false, length = 128)
	@Getter
	@Setter
	private String firstname;

	@Column(nullable = false, length = 128)
	@Getter
	@Setter
	private String lastname;

	@Column(nullable = false, length = 128)
	@Getter
	@Setter
	private String password;

	@Column(nullable = false, length = 12)
	@Getter
	@Setter
	private String salt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@Getter
	@Setter
	private Date registeredOn;

	@ElementCollection(targetClass = Role.class)
	@CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "email", nullable = false), uniqueConstraints = @UniqueConstraint(columnNames = {
			"email", "role" }))
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, length = 64)
	@Getter
	@Setter(AccessLevel.NONE)
	private final List<Role> roles;

	public UserProfile() {
		this.roles = new ArrayList<>();
	}

	public void addRole(final Role role) {
		this.roles.add(role);
	}
}
