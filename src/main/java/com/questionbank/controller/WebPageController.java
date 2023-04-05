package com.questionbank.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.questionbank.entity.User;
import com.questionbank.model.ResponseBody;
import com.questionbank.service.UserService;

@Controller
public class WebPageController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping("/")
	public String index() {
		return "dashboard";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	// Login User
	@PostMapping("/user-login")
	public String userLogin(@ModelAttribute("userLogin") User userLogin, ModelMap model, HttpServletRequest request,
			HttpSession session) {
		Authentication authentication = null;
		try {
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					userLogin.getEmail().trim(), userLogin.getPassword().trim()));
		} catch (Exception e) {
			model.addAttribute("message", "Bad Credentials!");
		}
		try {
			ResponseBody userSignupResponse = userService.userLogin(userLogin, authentication, model);
			model.addAttribute("baseUrl", userService.getBaseUrl(request));
			String massageString = userSignupResponse.getMessage();
			if (massageString.contains("Bad Credentials")) {
				model.addAttribute("message", "Alert :" + massageString);
			} else {
// 				String username = userService.getname(userLogin);
				String userEmail = userLogin.getEmail();
				session.setAttribute("loggedIn", true);
// 				session.setAttribute("username", username);
// 				session.setAttribute("baseUrl", userSignupService.getBaseUrl(request));
				session.setAttribute("userEmail", userEmail);
				session.setAttribute("role", "student");
				session.setAttribute("jwtToken", model.getAttribute("jwtToken"));
				// Redirect to dashboard with token
				String token = (String) model.getAttribute("jwtToken");
				if (token != null) {
					return "dashboard";
				} else {
					return "login";
				}
			}
		} catch (Exception e) {
			model.addAttribute("message", "Bad Credentials!");
		}
		return "login";
	}

	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@RequestMapping("/contact")
	public String contact() {
		return "contact";
	}

	@RequestMapping("/about")
	public String about() {
		return "about";
	}
}
