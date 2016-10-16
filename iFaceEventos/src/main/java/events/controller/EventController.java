package events.controller;

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

@Controller
@RequestMapping("/event")
public class EventController {

	@Autowired
	private EventDAO eventDAO;
	
	private UserDAO userDAO;
	
	@RequestMapping(value="/menu", method = RequestMethod.GET)
	public String menu() {
		return "redirect:/event/menu-event.html";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(String name, String description, String hostUsername, Date date) {

		Event event = null;
		User host = null;
		host = userDAO.getByUsername(hostUsername);
		
		if (name == null || name.isEmpty())
			return "redirect:/error/set-error?error=Empty name!";

		event = new Event(name, description, host , date);

		try {
			eventDAO.save(event, host);
		} catch (Exception ex) {
			return "redirect:/error/set-error?error=Error creating the Event: " + ex.toString();
		}

		return "redirect:/index.html";

	}

	@RequestMapping("/delete")
	public String delete(int eventId) {

		Event event = eventDAO.getEventById(eventId);
		
		if (event == null)
			return "redirect:/error/set-error?error=Event not found!";

		try {
			eventDAO.delete(event);
		} catch (Exception ex) {
			return "redirect:/error/set-error?error=Error deleting user: " + ex.toString();
		}

		return "redirect:/user/home.html";
	}

	@RequestMapping("/set-description")
	public String updateEventDescription(int eventId, String description) {

		Event event = eventDAO.getEventById(eventId);

		if (event == null)
			return "redirect:/error/set-error?error=Event not found!";


		event.setDescription(description);

		try {
			eventDAO.update(event);
		} catch (Exception ex) {
			return "redirect:/error/set-error?error=Error updating Description: " + ex.toString();
		}
		return "redirect:/user/home";
	}

	@RequestMapping("/set-name")
	public String updateEventName(int eventId, String name) {
		Event event = eventDAO.getEventById(eventId);

		if (event == null)
			return "redirect:/error/set-error?error=Event not found!";

		event.setName(name);

		try {
			eventDAO.update(event);
		} catch (Exception ex) {
			return "redirect:/error/set-error?error=Error updating Description: " + ex.toString();
		}
		return "redirect:/user/home";
	}

	/*
	 * returns a JSON containing the event attributes
	 * http://localhost:8080/event/get?eventid=0
	 */
	@RequestMapping("/get")
	@ResponseBody
	public Event get(@RequestParam(value = "eventid", defaultValue = "") int id) {
		return eventDAO.getEventById(id);
	}

	/*
	 * returns a JSON containing the user managed events list
	 * http://localhost:8080/event/get-by-hostusername?hostusername=marcusxdm
	 */
	@RequestMapping(value = "/get-by-hostusername")
	@ResponseBody
	public List<Event> getByHost(@RequestParam(value = "hostusername", defaultValue = "")String hostUsername) {
		return userDAO.getByUsername(hostUsername).getManagedEvents();
	}
}