package events.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import events.model.User;

@Repository
@Transactional
public class UserDAO {

	/*
	 * An EntityManager will be automatically injected from entityManagerFactory
	 * setup on DatabaseConfig class.
	 */
	@PersistenceContext
	private EntityManager entityManager;

	public void save(User user) {
		entityManager.persist(user);
		return;
	}

	public void delete(User user) {
		if (entityManager.contains(user))
			entityManager.remove(user);
		else
			entityManager.remove(entityManager.merge(user));
		return;
	}

	@SuppressWarnings("unchecked")
	public List<User> getAll() {
		return entityManager.createQuery("from User").getResultList();
	}

	public User getByEmail(String email) {
		return (User) entityManager.createQuery("from User where email = :email").setParameter("email", email)
				.getSingleResult();
	}

	public User getByUsername(String username) {
		return entityManager.find(User.class, username);
	}
	
	public User login(String username, String password) {
		return (User) entityManager.createQuery("from User where username = :username and password = :password").setParameter("username", username).setParameter("password", password)
				.getSingleResult();
	}

	public void update(User user) {
		entityManager.merge(user);
		return;
	}

}
