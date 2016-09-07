package management;

import models.Event;
import models.User;
import exceptionsFile.*;

import java.util.List;
import java.util.Scanner;

import javax.persistence.*;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class UserEventManager {
	final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	final SessionFactory sessionFactory = new Configuration().configure("./META-INF/hibernate.cfg.xml")
			.buildSessionFactory();
	Session session = threadLocal.get();

	// Users
	public void addUser(User instance) {
		session = sessionFactory.openSession();

		try {
			session.beginTransaction();
			session.save(instance);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	public void deleteUser(User instance) {
		session = sessionFactory.openSession();

		try {
			session.beginTransaction();
			session.delete(instance);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	public void updateUser(User instance) {
		session = sessionFactory.openSession();

		try {
			session.beginTransaction();
			session.update(instance);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	public User getUserById(int id) throws Exception {
		session = sessionFactory.openSession();

		Query query = session.createQuery("from User where id = :id");
		query.setParameter("id", id);

		User u = (User) query.uniqueResult();

		session.close();

		if (u == null) {
			throw new UserNotFoundException("This user was not found.\n");
		}
		return u;
	}

	public int loginCheck(String login, String senha) throws Exception {
		session = sessionFactory.openSession();

		Query query = session.createQuery("from User where login = :login");
		query.setParameter("login", login);

		User u = (User) query.uniqueResult();

		session.close();
		if (u == null) {
			throw new InvalidDataException("Username or Password incorrect!\n");
		}
		if (u.getPassword().equals(senha)) {
			return u.getId();
		}
		return -1;
	}

	public void verifyUserName(String login) throws Exception {
		session = sessionFactory.openSession();

		Query query = session.createQuery("from User where login = :login");
		query.setParameter("login", login);

		User u = (User) query.uniqueResult();

		session.close();

		if (u != null) {
			throw new UsernameAlreadyExistsException("This username is already taken.\n");
		}
	}

	public void verifyUserEmptyField(User user) throws Exception {
		if (user.getName().equals("") || user.getLogin().equals("") || user.getEmail().equals("")
				|| user.getPassword().equals("")) {
			throw new EmptyFieldException("You left an empty field!\n");
		}
	}

	public User getUserByName(String name) {
		/*
		 * Creates a List from all Users in DB if the given name is in the List
		 * User object is returned, if not, null returned.
		 */
		List<User> userList = null;
		int found = 0;
		User userFound = null;

		session = sessionFactory.openSession();

		try {
			session.beginTransaction();
			Query query = session.createQuery("from User");
			userList = query.list();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
		for (User u : userList) {
			if (u.getName().equals(name)) {
				found = 1;
				userFound = u;
				System.out.println("User Found.\n");
			}
		}
		return userFound;
	}
	
	// events
	public void saveEvent(Event newEvent) {
		/*
		 * Saves Event object in Database.
		 */
		session = sessionFactory.openSession();

		try {
			session.beginTransaction();
			session.save(newEvent);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}	
		
	}
	
	public void deleteEvent(Event instance) {
		session = sessionFactory.openSession();

		try {
			session.beginTransaction();
			session.delete(instance);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	public void updateEvent(Event instance) {
		session = sessionFactory.openSession();

		try {
			session.beginTransaction();
			session.update(instance);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}
	
	public Event getEventById(int id) throws Exception {
		session = sessionFactory.openSession();

		Query query = session.createQuery("from Event where id = :id");
		query.setParameter("id", id);

		Event e = (Event) query.uniqueResult();

		session.close();

		if (e == null) {
			throw new EventNotFoundException("This event was not found.\n");
		}
		return e;
	}
	
	public void verifyEventName(String name) throws Exception {
		session = sessionFactory.openSession();

		Query query = session.createQuery("from Event where name = :name");
		query.setParameter("name", name);

		Event e = (Event) query.uniqueResult();

		session.close();

		if (e != null) {
			throw new UsernameAlreadyExistsException("This event name is already taken.\n");
		}
	}
	
	public void verifyEventEmptyField(Event event) throws Exception {
		if (event.getName().equals("") || event.getDescription().equals("") || event.getDate().equals("")) {
			throw new EmptyFieldException("You left an empty field!\n");
		}
	}
	
	public Event getEventByName(String name) {
		List<Event> eventList = null;
		int found = 0;
		Event eventFound = null;

		session = sessionFactory.openSession();

		try {
			session.beginTransaction();
			Query query = session.createQuery("from Event");
			eventList = query.list();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
		for (Event e : eventList) {
			if (e.getName().equals(name)) {
				found = 1;
				eventFound = e;
				System.out.println("User Found.\n");
			}
		}
		return eventFound;
	}
	
//	private static void createEvent() {
//		/*
//		 * Creates Event object with desired information. 
//		 */
//		int numGuests = 0;
//		User guestUser = null;
//		Event newEvent;
//		String name, description, guestName;
//		int entry;
//		Scanner intScan = new Scanner(System.in);
//		Scanner stringScan = new Scanner(System.in);
//			
//		System.out.println("#######################\nQual sera o nome do seu Evento?\n");			
//		name = stringScan.nextLine();
//				
//		System.out.println("Digite informações de seu novo Evento:\n");
//		description = stringScan.nextLine();
//			
//		newEvent = new Event(name, description, mainUser, now);
//			
//		System.out.println("Digite quantos convidados deseja adicionar no momento:\n");
//		description = intScan.nextLine();
//			
//		for (int i=1; i>=numGuests; i++){
//			System.out.println("Qual o nome do "+i+"o convidado?\n");
//			guestName = stringScan.nextLine();
//			guestUser = getUserByName(guestName);
//			newEvent.getGuests().add(guestUser);
//		}
//					
//	}
	

	// view
	public static void viewCalendar() {
		// TODO Auto-generated method stub

	}

	public static void searchEvent() {
		// TODO Auto-generated method stub

	}

	public static void manageEvent() {
		// TODO Auto-generated method stub

	}
}
