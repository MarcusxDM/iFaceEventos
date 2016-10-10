package events.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import events.dao.UserDAO;
import events.model.User;

@Controller
public class SessionController {
	@Autowired
	private UserDAO userDAO;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	String login(HttpSession session, String username, String password) {
		
		try {
			User user = userDAO.login(username, password);

			if (user == null) {
				return "redirect:/";
			} else {
				session.setAttribute("user", user);
			}

			return "redirect:/user/home";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "redirect:/";
	}
	
	@GetMapping("/logout")
	String logout(HttpSession session) {
		try {
			session.invalidate();
			return "redirect:/";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "redirect:/";
	}
	
}