package com.questionbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.questionbank.entity.User;
import com.questionbank.repository.UserRepository;

@Controller
public class AdminPageController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping({ "/user/list", "/admin/user" })
	public String listUser() {
		return "user-list";
	}

	@GetMapping("/user/list2")
	public String listUser2(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "user-list2";
	}

	@GetMapping("/user/add")
	public String showFormUser(Model model) {
		model.addAttribute("user", new User());
		return "user-add";
	}

	@PostMapping("/user/add")
	public String addUser(Model model, @ModelAttribute("user") User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword().trim()));
		userRepository.save(user);
		model.addAttribute("user", new User());
		return "login";
	}

}
