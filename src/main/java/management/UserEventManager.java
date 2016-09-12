package management;

import models.Event;
import models.User;
import exceptionsFile.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	final SessionFactory sessionFactory = new Configuration().configure("./META-INF/hibernate.cfg.xml")
			.buildSessionFactory();
	Session session = threadLocal.get();
	private static Scanner reader = new Scanner(System.in);

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
	
	public static boolean verifyHour (String hour) {
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

	public Event searchEvent(String name) {
		/*
		 * Search Events objects in Database
		 */

		session = sessionFactory.openSession();

		session.beginTransaction();
		Event event = null;
		try{
			List<Event> events = session.createQuery("SELECT event FROM Event event WHERE event.name like %:name%").setParameter("name", name).list();
			if(events.size() > 1) {
				System.out.println("We found these events with similar names please choose one by id:");
				for (Event e : events) {
					System.out.println("Id: " + e.getId() + " Name: " + e.getName());
				}
				Scanner scan = new Scanner(System.in);
				int id = scan.nextInt();
				try {
					event = getEventById(id);
				}
				catch (Exception e) {
					System.out.println("Event not Found!!");
				}
			} else {
				event = events.get(0);
			}
//			event = (Event)session.createQuery("SELECT event FROM Event event WHERE event.name= :name").setParameter("name", name).list();
			System.out.println("Name: " + event.getName());
			System.out.println("Description: " + event.getDescription());
			System.out.println("Host: " + event.getHost());
			System.out.println("Date: " + event.getDate());
			System.out.println("Hour: " + event.getHour());
			if(event.getGuests() != null) {
				List<User> guests = event.getGuests();
				System.out.println("Guests: ");
				for(User user : guests) {
					System.out.println(user.getName());
				}
			}
		}
		catch(IndexOutOfBoundsException e){
			System.out.println("Event not found");
		}
		session.close();
		if (event == null) {
			System.out.println("DEBUG Event not found");
			return null;
		} else {
			System.out.println("DEBUG Event found");
			return event;
		}


	}
	
	public User searchUser(String name) {
		/*
		 * Search Events objects in Database
		 */
		
		session = sessionFactory.openSession();
		
		session.beginTransaction();
		User user = null;
		try{
		user = (User)session.createQuery("SELECT user FROM User user WHERE user.name= :name").setParameter("name", name).list().get(0);
		}
		catch(IndexOutOfBoundsException e){
			System.out.println("Event not found");
		}
		session.close();
		if (user == null) {
			System.out.println("DEBUG User not found");
			return null;
		} else {
			System.out.println("DEBUG User found");
			return user;
		}
		

	}
	
	public void manageEventMenu(Event event){
		System.out.println("----------- Manage Event Menu -----------");
		System.out.println("1. Add an user");
		System.out.println("2. Edit event");
		
		try{
			manageEventControl(event);
		}catch(InputMismatchException e){
			System.out.println("Invalid input. Please, try again");
			reader.nextLine();
		}
		
	}

	public void manageEventControl(Event event) {
		/*
		 * Edit objects informations in Database
		 */
		
		int choice = reader.nextInt();
		
		switch(choice){
		
		case 1:
			//add an user
			inviteUser(event);
			break;
		case 2:
			//Edit event
			editEventMenu(event);
			break;
		}

	}
	
	public void editEventMenu (Event event) {
		System.out.println("----------- Edit Event Menu -----------");
		System.out.println("What do you want to change?");
		System.out.println("1. Name");
		System.out.println("2. Description");
		System.out.println("3. Date");
		System.out.println("4. Hour");
		try{
			editEventControl(event);
		}catch(InputMismatchException e){
			System.out.println("Invalid input. Please, try again");
			reader.nextLine();
		}
		
	}
	
	public void editEventControl (Event event){
		/*
		 * Edit objects informations in Database
		 */
		
		int choice2 = reader.nextInt();
		
		switch(choice2){
		
		case 1:
			System.out.println("Change event's name:");
			String name = reader.next();
			event.setName(name);
			break;
		case 2:
			System.out.println("Change event's description:");
			String description = reader.next();
			event.setInfo(description);
			break;
		case 3:
			System.out.println("Change event's date:");
			String date = reader.next();
			event.setDate(date);
			break;
		case 4:
			System.out.println("Change event's hour:");
			String hour = reader.next();
			event.setHour(hour);
			break;
			
		default:
			System.out.println("Invalid input. Please, try again");
		}
		session = sessionFactory.openSession();
		try{
			session.beginTransaction();
			session.update(event);
			session.getTransaction().commit();
		} catch(HibernateException e){
			System.out.println("Error while updating event");
		}finally{
			session.close();
			}	
		}
	
	public void inviteUser (Event event){
		System.out.println("Put the user's name: ");
		String name = reader.next();
		
		User user = searchUser(name);
		
		if(user == null){
			System.out.println("User not found");
		} else{
			user.addAssociatedEvents(event);
			event.addGuests(user);
			updateUser(user);
			updateEvent(event);
		}
	}
	
	public void SeeManagedEvents(User user){
		List<Event> managedEvents = user.getManagedEvents();
		if(managedEvents.isEmpty()){
			System.out.println("You have no managed events at this time.");
			return;
		}
		for(Event event : managedEvents){
			System.out.println(event.getId() + " " + event.getName());
		}
	}
	
	
		
}
