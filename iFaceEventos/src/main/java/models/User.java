/**
 * 
 */
package models;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;







import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Vinicius
 *
 */
@Entity
public class User {
	@Id
	@GeneratedValue
	private long id;
	@Column
	private String password;
	@Column
	private String login;
	@Column
	private String name;
	@Column
	private String email;
	@Column
	private int age;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "guests")
	public List<Event> associatedEvents = new ArrayList<Event>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "host")
	public List<Event> managedEvents = new ArrayList<Event>();
	
	public User(){}
	
	public User(String name, String password, String email, String login, int age) {
		this.login = login;
		this.name = name;
		this.password = password;
		this.email = email;
		this.age = age;
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

	public String getLogin() {
		return login;
	}


	public void setLogin(String login) {
		this.login = login;
	}


	public void setAge(int age) {
		this.age = age;
	}


	/*public int calculateAge(LocalDate birth, LocalDate now) {
	    if ((birth != null) && (now != null)) {
	        return Period.between(birth, now).getYears();
	    } else {
	        return 0;
	    }
	}*/

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public int getAge() {
		return age;
	}

}