package events.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import events.dao.UserDAO;
import events.model.User;
import events.dao.EventDAO;
import events.model.Event;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/event")
public class EventController {

	@Autowired
	private EventDAO eventDAO;
	
	private UserDAO userDAO = new UserDAO();

	@RequestMapping(value="/menu", method = RequestMethod.GET)
	public String menu() {
		return "redirect:/event/menu-event.html";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(HttpSession session, String name, String description, String location, String date, String time) {
		
		User host = (User) session.getAttribute("user");
		if (host == null){
			return "redirect:/index.html";
		}
		Date formatedDate = null;
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		String dateString = date + ' ' + time;
		try {
			formatedDate = dt.parse(dateString);
		} catch (Exception e) {
			System.out.println(e);
		}
		Event event = null;

		event = new Event(name, description, location, host , formatedDate);
		try {
			eventDAO.save(event, host);
		} catch (Exception ex) {
			return "redirect:/error/set-error?error=Error creating the Event: " + ex.toString();
		}

		return "redirect:/event/menu-event.html";

	}
	
	@RequestMapping("/profile-host")
	public String profileHost(HttpSession session, @RequestParam(value = "eventId")int eventId) {

		User host = (User) session.getAttribute("user");
		if(host == null) {
			return "redirect:/index.html";
		}
		Event event = eventDAO.getEventById(eventId);
		if(host.getUsername().equals(event.getHost().getUsername())) {
			return "redirect:/event/profile-event-host.html#"+ eventId;
		} else {
			return "redirect:/event/profile-event.html#" + eventId;
		}

	}
	
	@RequestMapping("/profile-guest")
	public String profileEvent(HttpSession session, @RequestParam(value = "eventId")int eventId) {
		
		User host = (User) session.getAttribute("user");
		Event event = eventDAO.getEventById(eventId);
		
		return "redirect:/event/profile-event.html#" + eventId;
	}

	@RequestMapping("/event-edit")
	public String editEvent(@RequestParam(value = "eventId")int eventId) {
		System.out.println(eventId);
		return "redirect:/event/edit-event.html#" + eventId;
	}
	
	@RequestMapping("/edit")
	public String edit(HttpSession session, String guestUsername, int eventId, String name, String description, String location, String date, String time) {
		
		User host = (User) session.getAttribute("user");
		Event event = eventDAO.getEventById(eventId);
		User newGuest = null;

		Date formatedDate = null;
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		String dateString = date + ' ' + time;
		try {
			formatedDate = dt.parse(dateString);
		} catch (Exception e) {
			System.out.println(e);
		}

		if (host == null)
			return "redirect:/error/set-error?error=You are not logged in!";
		if (event == null)
			return "redirect:/error/set-error?error=Event not found!";

		if (guestUsername.length() > 0) {
			try {
				eventDAO.addGuest(event, guestUsername);
			} catch (Exception e) {
				return "redirect:/error/set-error?error=User not found!";
			}
		}

		// Any blank form won't get edited (Previous event's information will be intact)
		if (name != null)
			event.setName(name);
		if (description != null)
			event.setDescription(description);
		if (location != null)
			event.setLocation(location);
		if (date != null)
			event.setDate(formatedDate);
		
		try {
			eventDAO.update(event);
		} catch (Exception ex) {
			return "redirect:/error/set-error?error=Error updating Event: " + ex.toString();
		}
		return "redirect:/event/menu-event.html";
	}

	@RequestMapping("/delete")
	public String delete(HttpSession session, int eventId) {

		Event event = eventDAO.getEventById(eventId);
		
		if (event == null)
			return "redirect:/error/set-error?error=Event not found!";

		try {
			eventDAO.delete(event);
		} catch (Exception ex) {
			return "redirect:/error/set-error?error=Error deleting user: " + ex.toString();
		}

		return "redirect:/event/menu-event.html";
	}
	
	/*
	 * returns a JSON containing the event attributes
	 * http://localhost:8080/event/getEventLikeName?eventName=eventName
	 */
	@RequestMapping("/getEventLikeName")
	@ResponseBody
	public List<Event> getEventLikeName(@RequestParam(value = "eventName", defaultValue = "")String eventName) throws Exception{
		return eventDAO.getEventByName(eventName);
	}

	/*
	 * returns a JSON containing the event attributes
	 * http://localhost:8080/event/get?eventId=0
	 */
	@RequestMapping("/get")
	@ResponseBody
	public Event get(@RequestParam(value = "eventId", defaultValue = "") int id) {
		return eventDAO.getEventById(id);
	}

	@RequestMapping("/get-all")
	@ResponseBody
	public List<Event> getAll() {
		return eventDAO.getAll();
	}

	/*
	 * returns a JSON containing the user managed events list
	 * http://localhost:8080/event/get-managed?session=session
	 */
	@RequestMapping(value = "/get-managed")
	@ResponseBody
	public List<Event> getManaged(@RequestParam(value = "session", defaultValue = "")HttpSession session) {
		User host = (User) session.getAttribute("user");
		if (host == null)
			return null;
		return host.getManagedEvents();
	}
	
	
	/*
	 * returns a JSON containing the user managed events list
	 * http://localhost:8080/event/get-associated?session=session
	 */
	@RequestMapping(value = "/get-associated")
	@ResponseBody
	public List<Event> getAssociated(HttpSession session) {
		User host = (User) session.getAttribute("user");
		if (host == null)
			return null;
		return host.getAssociatedEvents();
	}
}