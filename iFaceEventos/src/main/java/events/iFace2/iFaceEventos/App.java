package events.iFace2.iFaceEventos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import events.exceptionsFile.*;
import events.management.UserEventManager;
import events.model.Event;
import events.model.User;

public class App {
	public static Date now;
	private static Scanner input;
	private static Scanner scan;

	public static void main(String[] args) throws Exception {
		int choice = 0;
		int uId = 0;
		int log = 0;
		int entry = 0;

		now = new Date();
		UserEventManager uEManager = new UserEventManager();

		while (choice != 3) {
			try {
				// first menu
				System.out.print("Welcome to iFace!: ");
				System.out.print("1. Login | ");
				System.out.print("2. Register now | ");
				System.out.print("3. Close iFace\n");
				System.out.print("-------------------------------------------------");

				input = new Scanner(System.in);
				choice = input.nextInt();
				input.nextLine();

				// login
				if (choice == 1) {
					try {
						String user;
						String pass;

						System.out.print("USERNAME: ");
						user = input.nextLine();
						System.out.print("PASSWORD: ");
						pass = input.nextLine();

						if (uEManager.loginCheck(user, pass) != -1) {
							System.out.println("You are now logged in!");
							System.out.println("-------------------------------------------------");
							uId = uEManager.loginCheck(user, pass);
							log = 1;
						}
					} catch (InvalidDataException e) {
						System.err.println(e);
						log = 0;
					}
					while (entry != 5 && log == 1) {

						System.out.print("1. Create Event | ");
						System.out.print("2. Manage Event | ");
						System.out.print("3. Search Event | ");
						System.out.print("4. See Calendar | ");
						System.out.print("5. Log Out\n");
						System.out.print("-------------------------------------------------");

						scan = new Scanner(System.in);
						entry = scan.nextInt();

						// create and persists event
						if (entry == 1) {
							try {
								scan = new Scanner(System.in);

								Event event = new Event();
								User user = uEManager.getUserById(uId);

								event.setHost(user);
								System.out.print("EVENT NAME: ");
								event.setName(input.nextLine());
								System.out.print("EVENT DESCRIPTION: ");
								event.setDescription(input.nextLine());
								System.out.print("DATE (DD/MM/AAAA): ");
								event.setDate(input.nextLine());
								uEManager.verifyDate(event.getDate());
								System.out.print("HOUR (HH:mm): ");
								event.setHour(input.nextLine());
								uEManager.verifyHour(event.getHour());

								uEManager.verifyEventEmptyField(event);
								uEManager.verifyEventName(event.getName());
								uEManager.saveEvent(event);

								// associates the event and the user
								user.addManagedEvents(event);
								user.addAssociatedEvents(event);
								event.addGuests(user);

								uEManager.updateUser(user);

								System.out.println("Your event is now registered!");
								System.out.println("-------------------------------------------------");

							} catch (EventAlreadyExistsException e) {
								System.err.println(e);
							} catch (EmptyFieldException e) {
								System.err.println(e);
							}

						}
						// manage event
						else if (entry == 2) {
							int entry1 = -1;

							while (entry1 != 3) {
								User user = uEManager.getUserById(uId);

								List<Event> managedEvents = user.getManagedEvents();

								if (managedEvents.isEmpty()) {
									System.out.println("You have no managed events at this time.");
									break;
								} else {
									uEManager.seeManagedEvents(user);
									int id = input.nextInt();
									Event event = uEManager.getEventById(id);

									System.out.print("1. Add Users to the Event | ");
									System.out.print("2. Edit Event Info | ");
									System.out.print("3. Back to menu\n");
									System.out.print("-------------------------------------------------");

									input = new Scanner(System.in);
									entry1 = input.nextInt();
									input.nextLine();

									if (entry1 == 1) {
										try {
											System.out
													.print("Type the name of the User you want to add to the event: ");

											input = new Scanner(System.in);
											String userName = input.nextLine();

											User newGuest = uEManager.getUserByName(userName);

											uEManager.printUser(newGuest);

											System.out.print("Do you want to add this user?: 1. Yes | 2. No ");

											input = new Scanner(System.in);
											int accept = input.nextInt();
											input.nextLine();

											if (accept == 1) {
												uEManager.addEvents(newGuest, id);

												event.addGuests(newGuest);
												newGuest.addAssociatedEvents(event);
												
												uEManager.updateEvent(event);
												
											} else if (accept == 2) {
												break;
											}

										} catch (InputMismatchException e) {
											System.err.println("You shoud've typed a number\n");
										} catch (UserNotFoundException e) {
											System.err.println(e);
										} catch (EmptyFieldException e) {
											System.err.println(e);
										} catch (AlreadyInvitedException e) {
											System.err.println(e);
										}

									} else if (entry1 == 2) {

									} else if (entry1 == 3) {
										break;
									}
								}

							}
						}
						// search event
						else if (entry == 3) {
							try {
								scan = new Scanner(System.in);
								System.out.println("Search Event by it's name:");
								String eventName = input.nextLine();

								uEManager.printEventFound(uEManager.getEventByName(eventName));
							} catch (EventNotFoundException e) {
								System.err.println(e);
							}

						}
						// view calendar
						else if (entry == 4) {
							uEManager.viewCalendar();
							break;
						}
						// logout
						else if (entry == 5) {
							log = 0;
							break;
						}

					}
				}

				// register a new user
				else if (choice == 2) {
					try {
						scan = new Scanner(System.in);

						User user = new User();

						System.out.print("NAME: ");
						user.setName(input.nextLine());
						System.out.print("USERNAME: ");
						user.setLogin(input.nextLine());
						System.out.print("EMAIL: ");
						user.setEmail(input.nextLine());
						System.out.print("PASSWORD: ");
						user.setPassword(input.nextLine());

						uEManager.verifyUserEmptyField(user);

						uEManager.verifyUserName(user.getLogin());
						uEManager.addUser(user);

						uId = user.getId();

						System.out.println("You are now registered!");
						System.out.println("-------------------------------------------------");

					} catch (UsernameAlreadyExistsException e) {
						System.err.println(e);
					} catch (EmptyFieldException e) {
						System.err.println(e);
					}
				}

				// closing the system
				else if (choice == 3) {
					System.err.println("Bye! See you soon!");
					System.err.println("-------------------------------------------------");
					break;
				}
			} catch (InputMismatchException e) {
				System.err.println("You should've typed a number\n");
			}
		}

	}
}
