package com.questionbank.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.questionbank.entity.User;
import com.questionbank.model.AppConstants;
import com.questionbank.model.ResponseBody;
import com.questionbank.repository.UserRepository;
import com.questionbank.security.JwtUtil;
import com.questionbank.service.UserService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public ResponseBody userLogin(User userLogin, Authentication authentication, ModelMap model) {
		String token = "Error";
		String message = "";
		String email = userLogin.getEmail();
		// Check if the authentication object is not null and is authenticated
		if (authentication != null && authentication.isAuthenticated()) {
			UserDetails userDetails = loadUserByUsername(email);
			token = jwtUtil.generateToken(userDetails);
			model.addAttribute("jwtToken", token);
			System.out.println("Generated token is " + token);
			message = "User Login Successfully !!";
			User userData = repository.findByEmail(userLogin.getEmail());
			checkAccessMenus(userData.getPk(), model, repository);
			return new ResponseBody(AppConstants.SUCCESS, message);
		} else {
			message = "Bad Credentials !!";
			return new ResponseBody(AppConstants.FAILURE, message);
		}
	}

	private void checkAccessMenus(Long pk, ModelMap model, UserRepository repository2) {
		List<Map<String, Object>> checkMenuLinks = repository2.checkMenuLinks(pk);
		model.put("menuList", checkMenuLinks);
		log.info("data" + checkMenuLinks.toString());
		model.put("subMenuList", repository2.checkSubMenuLinks(pk));
	}

	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info("this is email {}", email);
		User user = repository.findByEmail(email);
		return new org.springframework.security.core.userdetails.User((user.getEmail()), user.getPassword(),
				new ArrayList<>());
	}

	@Override
	public String getBaseUrl(HttpServletRequest request) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String contextPath = request.getContextPath();
		String baseurl = scheme + "://" + serverName + ":" + serverPort + contextPath;
		return baseurl;
	}

}
