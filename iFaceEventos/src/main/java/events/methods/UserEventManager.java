package events.methods;

import events.exceptionsFile.*;
import events.model.Event;
import events.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import javax.persistence.*;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class UserEventManager {
	private static Scanner intReader = new Scanner(System.in);
	private static Scanner stringReader = new Scanner(System.in);
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

	public User getUserById(int id) {
		session = sessionFactory.openSession();

		Query query = session.createQuery("from User where id = :id");
		query.setParameter("id", id);

		User u = (User) query.uniqueResult();

		session.close();

//		if (u == null) {
//			throw new UserNotFoundException("This user was not found.\n");
//		}
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
//			return u.getId();
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

//	public void verifyUserEmptyField(User user) throws Exception {
//		if (user.getName().equals("") || user.getLogin().equals("") || user.getEmail().equals("")
//				|| user.getPassword().equals("")) {
//			throw new EmptyFieldException("You left an empty field!\n");
//		}
//	}

	public User getUserByName(String name) throws Exception {
		session = sessionFactory.openSession();

		Query query = session.createQuery("from User where name = :name");
		query.setParameter("name", name);

		User u = (User) query.uniqueResult();

		session.close();

		if (u == null) {
			throw new UserNotFoundException("This user was not found.\n");
		}
		return u;
	}
	
	public void printUser(User user){
		System.out.print("User: ");
		System.out.println(user.getUsername());
		System.out.print("Name: ");
		System.out.println(user.getName());

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

	public Event getEventByName(String name) throws Exception {
		session = sessionFactory.openSession();

		Query query = session.createQuery("from Event where name = :name");
		query.setParameter("name", name);

		Event eventFound = (Event) query.uniqueResult();

		session.close();

		if (eventFound == null) {
			throw new EventNotFoundException("This event was not found.\n");
		}
		return eventFound;
	}

	public static boolean verifyDate(String date) {
		SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy");
		dtf.setLenient(false);
		try {
			dtf.parse(date);
		} catch (ParseException e) {
			System.err.println("Invalid Format.");
			return false;
		}
		return true;
	}

	public static boolean verifyHour(String hour) {
		try {
			Calendar data = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			sdf.setLenient(false);
			data.setTime(sdf.parse(hour));
			sdf.parse(hour);
		} catch (ParseException e) {
			System.err.println("Invalid Format.");
			return false;
		}
		return true;
	}

	// view
	public static void viewCalendar() {
		// TODO Auto-generated method stub

	}

	public void editEventMenu(Event event) {
		System.out.println("----------- Edit Event Menu -----------");
		System.out.println("What do you want to change?");
		System.out.println("1. Name");
		System.out.println("2. Description");
		System.out.println("3. Date");
		System.out.println("4. Hour");
		
		try {
			editEventControl(event);
		} catch (InputMismatchException e) {
			System.out.println("Invalid input. Please, try again");
			intReader.nextInt();
		}

	}

	public void editEventControl(Event event) {
		int choice2 = intReader.nextInt();

		switch (choice2) {

		case 1:
			System.out.println("Change event's name:");
			String name = stringReader.nextLine();
			event.setName(name);
			break;
		case 2:
			System.out.println("Change event's description:");
			String description = stringReader.nextLine();
			event.setInfo(description);
			break;
		case 3:
			System.out.println("Change event's date:");
			String date = stringReader.nextLine();
//			event.setDate(date);
			break;
		case 4:
			System.out.println("Change event's hour:");
			String hour = stringReader.nextLine();
//			event.setHour(hour);
			break;

		default:
			System.out.println("Invalid input. Please, try again");
		}
		session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.update(event);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			System.out.println("Error while updating event");
		} finally {
			session.close();
		}
	}

	public User inviteUser(Event event) throws Exception {
		System.out.println("Search user by name: ");
		String name = stringReader.nextLine();

		User user = getUserByName(name);

		if (user == null) {
			System.out.println("User not found");
			return null;
		} else {
			return user;
		}
	}
	
	public void printEventFound (Event eventFound)throws Exception {
		System.out.print("Event name: ");
		System.out.println(eventFound.getName());
		System.out.print("Description: ");
		System.out.println(eventFound.getInfo());
		System.out.print("ID: ");
		System.out.println(eventFound.getId());
	}

	public void seeManagedEvents(User user) {
		List<Event> managedEvents = user.getManagedEvents();
		if (managedEvents.isEmpty()) {
			System.out.println("You have no managed events at this time.");
			return;
		} else {
			System.out.println("These are the events you are currently managing: ");
			for (Event event : managedEvents) {
				System.out.print("Event name: ");
				System.out.println(event.getName());
				System.out.print("Description: ");
				System.out.println(event.getInfo());
				System.out.print("ID: ");
				System.out.println(event.getId());
				System.out.print("\n");
			}
			System.out.print("Type the ID of the event you want: ");
		}
	}
	
	public void addEvents (User user, int eId) throws Exception {		
		for (int i = 0; i < user.associatedEvents.size(); i++) {
			if (eId == user.associatedEvents.get(i).getId()) {
				throw new AlreadyInvitedException("This user was already invited!\n");
			}
		}
		for (int i = 0; i < user.managedEvents.size(); i++) {
			if (eId == user.managedEvents.get(i).getId()) {
				throw new AlreadyInvitedException("This user was already invited!\n");
			}
		}
	}
	
}
