package iFace2.iFaceEventos;

import java.time.LocalDate;



import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import models.Event;
import models.User;

/**
 * created by Vinicius
 *
 */
public class App {
	public static User mainUser;
	public static Date now;
	public static long mainUserId = 1;
	public static final SessionFactory sessionFactory = new Configuration()
			.configure("META-INF/hibernate.cfg.xml").buildSessionFactory();;

	public static void main(String[] args) throws Exception {
		now = new Date();
		
		
		//mainUser = new User("Dono", "senha", "email", "login", 20);
		
		userMainScreen();
		
		}
	
	

	private static void userMainScreen(){
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			mainUser =  (User) session.get(User.class, mainUserId);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
		
		System.out.println("#########  Bem vindo, " + mainUser.getName()
				+ "! O que deseja fazer?   ########\n");
		
		System.out.println("1 - Criar Evento");
		System.out.println("2 - Gerenciar Evento");
		System.out.println("3 - Pesquisar Evento");
		System.out.println("4 - Ver Calendario");
		System.out.println("5 - Sair");

		Scanner integerScan = new Scanner(System.in);
		int entry = integerScan.nextInt();

		switch (entry) {
		case 1:
			createEvent();
			break;
		case 2:
			manageEvent();
			break;
		case 3:
			searchEvent();
			break;
		case 4:
			viewCalendar();
			break;
		case 5:
			System.exit(0);
		}
	}

	private static void viewCalendar() {
		// TODO Auto-generated method stub
		
	}

	private static void searchEvent() {
		// TODO Auto-generated method stub
		
	}

	private static void manageEvent() {
		// TODO Auto-generated method stub
		
	}
	public static User getUserByName(String name){
		/*
		 * Creates a List from all Users in DB
		 * if the given name is in the List
		 * User object is returned, if not, null
		 * returned.
		 */
		List<User> userList = null;
		int found = 0;
		User userFound = null;
		Session session = sessionFactory.openSession();
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
				System.out.println("Usuario Encontrado.\n");
				}
			}
		return userFound;
		
	}

	private static void createEvent() {
		/*
		 * Creates Event object with desired information. 
		 */
		int numGuests = 0;
		User guestUser = null;
		Event newEvent;
		String name, description, guestName;
		int entry;
		Scanner intScan = new Scanner(System.in);
		Scanner stringScan = new Scanner(System.in);
			
		System.out.println("#######################\nQual sera o nome do seu Evento?\n");			
		name = stringScan.nextLine();
				
		System.out.println("Digite informações de seu novo Evento:\n");
		description = stringScan.nextLine();
			
		newEvent = new Event(name, description, mainUser, now);
			
		System.out.println("Digite quantos convidados deseja adicionar no momento:\n");
		description = intScan.nextLine();
			
		for (int i=1; i>=numGuests; i++){
			System.out.println("Qual o nome do "+i+"o convidado?\n");
			guestName = stringScan.nextLine();
			guestUser = getUserByName(guestName);
			newEvent.getGuests().add(guestUser);
		}
			
			
		saveEvent(newEvent);
		System.out.println("Evento criada!\n\n\n");
					
	}



	private static void saveEvent(Event newEvent) {
		/*
		 * Saves Event object in Database.
		 */
		Session session = sessionFactory.openSession();

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
}
