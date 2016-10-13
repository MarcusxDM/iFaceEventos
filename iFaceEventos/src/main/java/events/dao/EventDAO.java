package events.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import events.model.User;
import events.model.Event;

@Repository
@Transactional
public class EventDAO {

	/*
	 * An EntityManager will be automatically injected from entityManagerFactory
	 * setup on DatabaseConfig class.
	 */
	@PersistenceContext
	private EntityManager entityManager;
	
	UserDAO userDAO;

	public void save(Event event, User host) {
		entityManager.persist(event);
		host.addManagedEvents(event);
		host.addAssociatedEvents(event);
		event.addGuests(host);

		userDAO.update(host);
		return;
	}

	public void delete(Event event) {
		if (entityManager.contains(event))
			entityManager.remove(event);
		else
			entityManager.remove(entityManager.merge(event));
		return;
	}

	@SuppressWarnings("unchecked")
	public List<Event> getAll() {
		return entityManager.createQuery("from Event").getResultList();
	}

	public void update(Event event) {
		entityManager.merge(event);
		return;
	}
	
	public void addEvents (Event event, User guest) {		
		for (int i = 0; i < guest.associatedEvents.size(); i++) {
			if (event.getId() == guest.associatedEvents.get(i).getId()) {
				System.out.println("This user was already invited!\n");
			}
		}
	}
	
	public void addGuest(Event event, User guest){
		addEvents(event, guest);

		event.addGuests(guest);
		guest.addAssociatedEvents(event);
		
		entityManager.merge(event);
	}

	public Event getEventById(int id) {
		return entityManager.find(Event.class, id);
	}
}
