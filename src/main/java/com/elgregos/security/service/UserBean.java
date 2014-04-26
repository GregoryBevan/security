/**
 * 
 */
package com.elgregos.security.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.elgregos.security.entities.User;

/**
 * Description :
 * 
 * @author Grégo
 * @date 9 mai 2013
 */
@Stateless
public class UserBean {

	@PersistenceContext
	private EntityManager entityManager;

	public void createUser(User user) {
		this.entityManager.persist(user);
	}
}
