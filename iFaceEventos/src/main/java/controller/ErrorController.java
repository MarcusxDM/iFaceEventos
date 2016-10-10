package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/error")
public class ErrorController {

	@RequestMapping("/set-error")
	@ResponseBody
	public String generateErrorPage(String error) {
		return "<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Error</title></head><body><h1>Ops, algo errado aconteceu!</h1> <h2>"
				+ error + "</h2></html>";
	}

}
 
