package com.codechef.smartalert.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codechef.smartalert.constant.ApplicationConstant;
import com.codechef.smartalert.dto.AppointmentDetails;
import com.codechef.smartalert.dto.Message;
import com.codechef.smartalert.model.AppointmentArea;
import com.codechef.smartalert.model.Appointments;
import com.codechef.smartalert.model.ChatHistory;
import com.codechef.smartalert.model.Times;
import com.codechef.smartalert.service.AppointmentService;
import com.codechef.smartalert.service.UserService;

@Controller
@RequestMapping("appointment/*")
public class AppointmentController {

	@Autowired
	UserService userService;

	@Autowired
	AppointmentService appointmentService;

	@RequestMapping(value = "getAppointmentDetails", method = RequestMethod.GET, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public AppointmentDetails getAppointmentDetails() throws JsonGenerationException, JsonMappingException, IOException {
		return userService.getAppointmentDetails();
	}

	@RequestMapping(value = "getTimes", method = RequestMethod.GET, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public List<Times> getTimes() throws JsonGenerationException, JsonMappingException, IOException {
		return userService.getTimes();
	}
	
	@RequestMapping(value = "getAllocatedTimes", method = RequestMethod.POST, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public List<Appointments> getAllocatedTimes(@RequestBody final Date selectedDate) throws JsonGenerationException, JsonMappingException, IOException {
		return userService.getAllocatedTimes(selectedDate);
	}
	
	@RequestMapping(value = "addNewAppointment", method = RequestMethod.POST, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public Message addNewAppointment(@RequestBody final Map<String, Object> newAppointment) throws ParseException {
		return userService.addNewAppointment(newAppointment);
	}
	
	@RequestMapping(value = "getScheduledAppointmentDetails/{userId}", method = RequestMethod.GET, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public Message getScheduledAppointmentDetails(@PathVariable final String userId) throws JsonGenerationException, JsonMappingException, IOException {
		return userService.getScheduledAppointmentDetails(userId);
	}
	
	@RequestMapping(value = "getAppointmentArea", method = RequestMethod.GET, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public List<AppointmentArea> getAppointmentArea() throws JsonGenerationException, JsonMappingException, IOException {
		return userService.getAppointmentArea();
	}
	
	@RequestMapping(value = "addSaveAreasTopics", method = RequestMethod.POST, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public Message addSaveAreasTopics(@RequestBody final Map<String, Object> appointmentArea) throws JsonGenerationException, JsonMappingException, IOException {
		return userService.addSaveAreasTopics(appointmentArea);
	}
	
	@RequestMapping(value = "getAllScheduledAppointments", method = RequestMethod.GET, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public List<Map<String, Object>> getAllScheduledAppointments() throws JsonGenerationException, JsonMappingException, IOException {
		return userService.getAllScheduledAppointments();
	}
	
	@RequestMapping(value = "getScheduledAppointments/{userId}", method = RequestMethod.GET, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public List<Map<String, Object>> getScheduledAppointments(@PathVariable final String userId) throws JsonGenerationException, JsonMappingException, IOException {
		return userService.getScheduledAppointments(userId);
	}
	
	@RequestMapping(value = "getAppoinmentDetails/{roomId}", method = RequestMethod.GET, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public List<Map<String, Object>> getAppoinmentDetails(@PathVariable final String roomId) throws JsonGenerationException, JsonMappingException, IOException {
		return userService.getAppoinmentDetails(roomId);
	}
	
	@RequestMapping(value = "saveChatHistory", method = RequestMethod.POST, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public int saveChatHistory(@RequestBody final ChatHistory chatHistory) throws JsonGenerationException, JsonMappingException, IOException {
		return userService.saveChatHistory(chatHistory);
	}
	
	@RequestMapping(value = "getChatHistory/{roomId}", method = RequestMethod.GET, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public List<ChatHistory> getChatHistory(@PathVariable final String roomId) throws JsonGenerationException, JsonMappingException, IOException {
		return userService.getChatHistory(roomId);
	}
	
	@RequestMapping(value = "saveAppointmentStatus/{appointmentId}/{appointmentStatus}", method = RequestMethod.POST, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public Message saveAppointmentStatus(@PathVariable final String appointmentId, @PathVariable final String appointmentStatus) throws JsonGenerationException, JsonMappingException, IOException {
		return userService.saveAppointmentStatus(appointmentId, appointmentStatus);
	}
}