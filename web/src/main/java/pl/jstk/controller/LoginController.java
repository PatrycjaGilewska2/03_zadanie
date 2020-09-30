package pl.jstk.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;

@Controller
public class LoginController {

	@GetMapping(value = "/login")
	public String login() {
		return ViewNames.LOGIN;
	}
	
	@GetMapping(value = "/loginfailed")
	public String loginFaliled(Model model) {
		model.addAttribute(ModelConstants.ERROR, true);
		return ViewNames.LOGIN;
	}
	
	@GetMapping(value = "/logout")
	public String logout() {
		return ViewNames.LOGIN;
	}
	
	@GetMapping(value = "/403")
	public ModelAndView accesssDenied(Principal user) {
		ModelAndView model = new ModelAndView();
		String msg = "User: " + user.getName() + " have no permission to use this option.";
		model.addObject(ModelConstants.ERROR, msg);
		model.setViewName(ViewNames._403);
		return model;
	}
}
