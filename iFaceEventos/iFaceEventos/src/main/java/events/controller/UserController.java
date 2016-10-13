package events.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import events.dao.UserDAO;
import events.model.User;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserDAO userDAO;

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", Pattern.CASE_INSENSITIVE);

	private static boolean validateEmail(String email) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
		return matcher.find();
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(String name,  String password,  String email, String username) {

		User user = null;
		if (name == null || name.isEmpty())
			return "redirect:/error/set-error?error=Empty name!";
		
		if (userDAO.getByUsername(username) != null)
			return "redirect:/error/set-error?error=User already exists!";

		if (!validateEmail(email))
			return "redirect:/error/set-error?error=Invalid email!";

		if (password == null || password.isEmpty())
			return "redirect:/error/set-error?error=Empty password!";

		if (password.length() < 8)
			return "redirect:/error/set-error?error=Password too short! 8 characters minimum.";

		user = new User(name, password, email, username, null, null);

		try {
			userDAO.save(user);
		} catch (Exception ex) {
			return "redirect:/error/set-error?error=Error creating the user: " + ex.toString();
		}

		return "redirect:/index.html";

	}

	@RequestMapping("/delete")
	public String delete(String username, String password) {

		User user = userDAO.getByUsername(username);

		if (user == null)
			return "redirect:/error/set-error?error=User not found!";

		if (!user.getPassword().equals(password))
			return "redirect:/error/set-error?error=Confirmation password doesn't match";

		try {
			userDAO.delete(user);
		} catch (Exception ex) {
			return "redirect:/error/set-error?error=Error deleting user: " + ex.toString();
		}

		return "redirect:/index.html";
	}

	@RequestMapping("/set-email")
	public String updateEmail(String username, String email, String password) {

		User user = userDAO.getByUsername(username);

		if (user == null)
			return "redirect:/error/set-error?error=User not found!";

		if (!validateEmail(email))
			return "redirect:/error/set-error?error=Invalid email!";

		if (password == null || password.isEmpty())
			return "redirect:/error/set-error?error=Empty password";

		if (user.getEmail().equals(email))
			return "redirect:/error/set-error?error=Email address already in use!";

		if (!user.getPassword().equals(password))
			return "redirect:/error/set-error?error=Incorrect password";

		user.setEmail(email);

		try {
			userDAO.update(user);
		} catch (Exception ex) {
			return "redirect:/error/set-error?error=Error creating the user: " + ex.toString();
		}
		return "redirect:/user/home";
	}

	@RequestMapping("/set-password")
	public String updatePassword(String username, String newPassword, String password) {
		User user = userDAO.getByUsername(username);

		if (user == null)
			return "redirect:/error/set-error?error=User not found!";

		if (newPassword == null || newPassword.isEmpty())
			return "redirect:/error/set-error?error=Empty new password!";

		if (user.getPassword().equals(newPassword))
			return "redirect:/error/set-error?error=Password already in use";

		if (newPassword.length() < 8)
			return "redirect:/error/set-error?error=Password too short! 8 characters minimum.";

		if (password == null || password.isEmpty())
			return "redirect:/error/set-error?error=Empty confirmation password!";

		if (!user.getPassword().equals(password))
			return "redirect:/error/set-error?error=Confirmation password doesn't match";

		try {
			user.setPassword(newPassword);
			userDAO.update(user);
		} catch (Exception ex) {
			return "redirect:/error/set-error?error=Error creating the user: " + ex.toString();
		}

		return "redirect:/logout";
	}

	/*
	 * returns a JSON containing the user attributes
	 * http://localhost:8080/user/get?username=anthony
	 */
	@RequestMapping("/get")
	@ResponseBody
	public User get(@RequestParam(value = "username", defaultValue = "") String username) {
		return userDAO.getByUsername(username);
	}

	/*
	 * returns a JSON containing the user attributes
	 * http://localhost:8080/user/get-by-email?email=anthony@jatoba.com
	 */
	@RequestMapping(value = "/get-by-email")
	@ResponseBody
	public User getByEmail(String email) {
		return userDAO.getByEmail(email);
	}

	/*
	 * returns a JSON containing the user attributes
	 */
	@RequestMapping(value = "/login")
	@ResponseBody
	public User login(String username, String password) {
		return userDAO.login(username, password);
	}

	/*###################### Example how to use Sessions ######################### 
	* To use session, the method must receive a HttpSession object
	*/
	@RequestMapping("/home")
	@ResponseBody
	public String home(HttpSession session) {
		// Getting the session object
		User user = (User) session.getAttribute("user");

		// If the object doesn't exists (User isn't logged in)
		if (user == null) {
			return "<center><h1>IFACE</h1></center>" + "<center><h4>You aren't logged in</h4><center>";

		// Else the object exists (User is logged in)
		} else {
			return "<center><h1>IFACE</h1></center>" + "<center><h4>You are logged</h4><center>" + "<center> Username: " + user.getUsername() + " -- Email: " + user.getEmail() + "</center>" + "<center><a href=/logout> Logout </a> <a href=delete-form.html> Delete account </a>"
					+ "<a href=set-email-form.html> Update email </a> <a href=set-password-form.html> Update password </a></center>";
		}

	}
}
