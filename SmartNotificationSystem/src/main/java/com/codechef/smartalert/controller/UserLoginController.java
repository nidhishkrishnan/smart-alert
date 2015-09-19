package com.codechef.smartalert.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codechef.smartalert.constant.ApplicationConstant;
import com.codechef.smartalert.dto.Message;
import com.codechef.smartalert.model.UserDetails;
import com.codechef.smartalert.model.Users;
import com.codechef.smartalert.notification.service.NotificationSystem;
import com.codechef.smartalert.service.KandyRestService;
import com.codechef.smartalert.service.UserService;

@Controller
@RequestMapping("user/*")
public class UserLoginController {

	@Autowired
	UserService userService;
	
	@Autowired
	private Message message;
	
	@RequestMapping(value = "loginSubscriber/{username}/{password}", method = RequestMethod.POST, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public Message loginUser(@PathVariable final String username, @PathVariable final String password) throws JsonGenerationException, JsonMappingException, IOException {
		if(username.equals("manu") && password.equals("123456"))
		{
			message.setData(null);
			message.setMessage("success");
		}
		else
		{
			message.setData(null);
			message.setMessage("failure");	
		}
		return message;
	}
	
	@RequestMapping(value = "getNotification/{subscriberId}", method = RequestMethod.GET, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public ArrayList<String> getNotification(@PathVariable final String subscriberId) throws JsonGenerationException, JsonMappingException, IOException {		
		return NotificationSystem.getInstance().getNoti(Integer.parseInt(subscriberId));
	}
}
