package com.questionbank.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;

import com.questionbank.entity.User;
import com.questionbank.model.ResponseBody;

public interface UserService {

	ResponseBody userLogin(User userLogin, Authentication authentication, ModelMap models);

	Object getBaseUrl(HttpServletRequest request);

}
