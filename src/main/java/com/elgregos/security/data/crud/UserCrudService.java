package com.elgregos.security.data.crud;

import javax.enterprise.context.Dependent;

import com.elgregos.jpa.CrudServiceImlp;
import com.elgregos.security.data.entities.Role;
import com.elgregos.security.data.entities.UserProfile;

/**
 *
 * @author Grégo
 * @date 9 mai 2013
 */
@Dependent
public class UserCrudService extends CrudServiceImlp<UserProfile> {

	public Role[] getRoles(final String email) {
		return find(email).getRoles().toArray(new Role[] {});
	}
}
