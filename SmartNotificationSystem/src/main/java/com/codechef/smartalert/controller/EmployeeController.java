package com.codechef.smartalert.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

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
import com.codechef.smartalert.dto.Message;
import com.codechef.smartalert.model.Employees;
import com.codechef.smartalert.model.Languages;
import com.codechef.smartalert.service.AppointmentService;
import com.codechef.smartalert.service.UserService;

@Controller
@RequestMapping("employee/*")
public class EmployeeController {

	@Autowired
	UserService userService;

	@Autowired
	AppointmentService appointmentService;
	
	@RequestMapping(value = "addNewEmployees", method = RequestMethod.POST, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public Message addNewEmployees(@RequestBody final Employees employee) throws ParseException {
		return userService.addNewEmployees(employee);
	}
	
	@RequestMapping(value = "saveEditedEmployees", method = RequestMethod.POST, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public Message saveEditedEmployees(@RequestBody final Employees employee) throws ParseException {
		return userService.saveEditedEmployees(employee);
	}
	
	@RequestMapping(value = "getAllEmployees", method = RequestMethod.GET, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public List<Employees> getAllEmployees() throws ParseException {
		return userService.getAllEmployees();
	}
	
	@RequestMapping(value = "getAllLanguages", method = RequestMethod.GET, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public List<Languages> getAllLanguages() throws ParseException, JsonGenerationException, JsonMappingException, IOException {
		return userService.getAllLanguages();
	}
	
	@RequestMapping(value = "getAllLanguagesAndAreas", method = RequestMethod.GET, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public HashMap<String, Object> getAllLanguagesAndAreas() throws JsonGenerationException, JsonMappingException, IOException {
		return userService.getAllLanguagesAndAreas();
	}
	
	@RequestMapping(value = "deleteEmployee/{empId}", method = RequestMethod.POST, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public Message deleteEmployee(@PathVariable final String empId) throws JsonGenerationException, JsonMappingException, IOException {
		return userService.deleteEmployee(empId);
	}
	
	@RequestMapping(value = "addSaveLanguage", method = RequestMethod.POST, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public Message addSaveLanguage(@RequestBody final Languages language) throws ParseException, JsonGenerationException, JsonMappingException, IOException {
		return userService.addSaveLanguage(language);
	}
	
	@RequestMapping(value = "deleteLanguage/{languageId}", method = RequestMethod.POST, produces = ApplicationConstant.APP_JSON)
	@ResponseBody
	public Message deleteLanguage(@PathVariable final String languageId) throws JsonGenerationException, JsonMappingException, IOException {
		return userService.deleteLanguage(languageId);
	}
}