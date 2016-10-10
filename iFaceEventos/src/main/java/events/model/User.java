package events.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class User {
	@Id
	@GeneratedValue
	protected int id;
	public String password;
	public String login;
	public String name;
	public String email;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "guests")
	public List<Event> associatedEvents = new ArrayList<Event>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "host")
	public List<Event> managedEvents = new ArrayList<Event>();

	public User() {

	}

	public User(String name, String password, String email, String login, List<Event> associatedEvents,
			List<Event> managedEvents) {
		super();
		this.login = login;
		this.name = name;
		this.password = password;
		this.email = email;
		this.associatedEvents = associatedEvents;
		this.managedEvents = managedEvents;
	}

	public List<Event> getAssociatedEvents() {
		return associatedEvents;
	}

	public void setAssociatedEvents(List<Event> associatedEvents) {
		this.associatedEvents = associatedEvents;
	}

	public List<Event> getManagedEvents() {
		return managedEvents;
	}

	public void setManagedEvents(List<Event> managedEvents) {
		this.managedEvents = managedEvents;
	}

	public void addManagedEvents(Event event) {
		this.managedEvents.add(event);
	}

	public void removeManagedEvents(Event event) {
		this.managedEvents.remove(event);
	}

	public void addAssociatedEvents(Event event) {
		this.associatedEvents.add(event);
	}

	public void removeAssociatedEvents(Event event) {
		this.associatedEvents.remove(event);
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/*
	 * public int calculateAge(LocalDate birth, LocalDate now) { if ((birth !=
	 * null) && (now != null)) { return Period.between(birth, now).getYears(); }
	 * else { return 0; } }
	 */

}
//
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//@Entity
//@Table(name = "user")
//public class User {
//
//	@Id
//	private String username;
//	
//	private String email;
//	private String password;
//
//	public User() {
//
//	}
//
//	public User(String username, String email, String password) {
//		this.username = username;
//		this.email = email;
//		this.password = password;
//	}
//
//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((username == null) ? 0 : username.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		User other = (User) obj;
//		if (username == null) {
//			if (other.username != null)
//				return false;
//		} else if (!username.equals(other.username))
//			return false;
//		return true;
//	}
//
//	@Override
//	public String toString() {
//		return "User [username=" + username + ", email=" + email + ", password=" + password + "]";
//	}
//
//}