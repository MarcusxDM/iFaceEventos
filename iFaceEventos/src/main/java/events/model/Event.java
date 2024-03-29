package events.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "event")
public class Event {
	@Id
	@GeneratedValue
	protected int id;
	@NotNull
	public String name;
	
	public String description;
	
	@NotNull
	public String location;
	
	@NotNull
	public Date date;


	@ManyToMany
	@JoinTable(name = "user_event", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "user_username"))
	public List<User> guests = new ArrayList<>();

	@ManyToOne
	protected User host;

	public Event() {

	}

	public Event(String name, String description, String location, User host, Date date) {
		this.name = name;
		this.description = description;
		this.host = host;
		this.date = date;
		this.location = location;
		
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return description;
	}

	public void setInfo(String info) {
		this.description = info;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<User> getGuests() {
		return guests;
	}

	public void setGuests(List<User> guests) {
		this.guests = guests;
	}

	public User getHost() {
		return host;
	}

	public void setHost(User host) {
		this.host = host;
	}

	public void addGuests(User user) {
		this.guests.add(user);
	}

}
