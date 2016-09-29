package controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dao.UserDAO;
import model.User;

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
	public String create(String name, String password, String email, String login) {

		User user = null;

		if (userDAO.getByUsername(name) != null)
			return new ErrorController().generateErrorPage("User already exists!");

		if (!validateEmail(email))
			return new ErrorController().generateErrorPage("Invalid email!");

		if (password == null || password.isEmpty())
			return new ErrorController().generateErrorPage("Empty password!");

		if (password.length() < 8)
			return new ErrorController().generateErrorPage("Password too short! 8 characters minimum.");

		user = new User(name, password, email, login, null, null);

		try {
			userDAO.save(user);
		} catch (Exception ex) {
			return new ErrorController().generateErrorPage("Error creating the user: " + ex.toString());
		}

		return "redirect:/";

	}

	@RequestMapping("/delete")
	public String delete(String username, String password) {

		User user = userDAO.getByUsername(username);

		if (user == null)
			return new ErrorController().generateErrorPage("User not found!");

		if (!user.getPassword().equals(password))
			return new ErrorController().generateErrorPage("Confirmation password doesn't match");

		try {
			userDAO.delete(user);
		} catch (Exception ex) {
			return new ErrorController().generateErrorPage("Error deleting user: " + ex.toString());
		}

		return "redirect:/";
	}

	@RequestMapping("/set-email")
	public String updateEmail(String username, String email, String password) {

		User user = userDAO.getByUsername(username);

		if (user == null)
			return new ErrorController().generateErrorPage("User not found!");

		if (!validateEmail(email))
			return new ErrorController().generateErrorPage("Invalid email!");

		if (password == null || password.isEmpty())
			return new ErrorController().generateErrorPage("Empty password");

		if (user.getEmail().equals(email))
			return new ErrorController().generateErrorPage("Email address already in use!");

		if (!user.getPassword().equals(password))
			return new ErrorController().generateErrorPage("Password don't match");

		user.setEmail(email);

		try {
			userDAO.update(user);
		} catch (Exception ex) {
			return new ErrorController().generateErrorPage("Error creating the user: " + ex.toString());
		}
		return "redirect:/";
	}

	@RequestMapping("/set-password")
	public String updatePassword(String username, String newPassword, String password) {
		User user = userDAO.getByUsername(username);

		if (user == null)
			return new ErrorController().generateErrorPage("User not found!");

		if (newPassword == null || newPassword.isEmpty())
			return new ErrorController().generateErrorPage("Empty new password!");

		if (user.getPassword().equals(newPassword))
			return new ErrorController().generateErrorPage("Password already in use");

		if (newPassword.length() < 8)
			return new ErrorController().generateErrorPage("Password too short! 8 characters minimum.");

		if (password == null || password.isEmpty())
			return new ErrorController().generateErrorPage("Empty confirmation password!");

		if (!user.getPassword().equals(password))
			return new ErrorController().generateErrorPage("Confirmation password doesn't match");

		try {
			user.setPassword(newPassword);
			userDAO.update(user);
		} catch (Exception ex) {
			return new ErrorController().generateErrorPage("Error creating the user: " + ex.toString());
		}

		return "redirect:/";
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
	public String home(HttpSession session){
		//Getting the session object
		User user = (User) session.getAttribute("user");
		
		//If the object doesn't exists (User isn't logged in)
		if(user == null){
			return "You aren't logged in";
		
		//Else the object exists (User is logged in)	
		}else{
			return "Welcome " + user.getName();
		}
		
	} 
}
