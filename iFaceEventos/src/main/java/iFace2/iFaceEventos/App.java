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
import management.UserEventManager;

public class App {
	public static Date now;

	public static void main(String[] args) {
		now = new Date();
		UserEventManager uEManager = new UserEventManager();
		
		
		System.out.println("1 - Criar Evento");
		System.out.println("2 - Gerenciar Evento");
		System.out.println("3 - Pesquisar Evento");
		System.out.println("4 - Ver Calendario");
		System.out.println("5 - Sair");

		Scanner integerScan = new Scanner(System.in);
		int entry = integerScan.nextInt();

		switch (entry) {
		case 1:
			// do something
			break;
		case 2:
			uEManager.manageEvent();
			break;
		case 3:
			uEManager.searchEvent();
			break;
		case 4:
			uEManager.viewCalendar();
			break;
		case 5:
			break;
		}
	
		
		}
		

	
	

	




}
