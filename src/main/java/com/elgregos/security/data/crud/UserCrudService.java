package com.elgregos.security.data.crud;

import com.elgregos.jpa.CrudServiceImlp;
import com.elgregos.security.data.entities.Role;
import com.elgregos.security.data.entities.User;

/**
 *
 * @author Grégo
 * @date 9 mai 2013
 */
public class UserCrudService extends CrudServiceImlp<User> {

	public Role[] getRoles(final String email) {
		return find(email).getRoles().toArray(new Role[] {});
	}
}
