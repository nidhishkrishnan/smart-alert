package com.codechef.smartalert.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codechef.smartalert.constant.ApplicationConstant;
import com.codechef.smartalert.constant.KandyApiConstant;
import com.codechef.smartalert.dao.UsersDao;
import com.codechef.smartalert.dto.AppointmentDetails;
import com.codechef.smartalert.dto.Message;
import com.codechef.smartalert.model.AppointmentArea;
import com.codechef.smartalert.model.Appointments;
import com.codechef.smartalert.model.ChatHistory;
import com.codechef.smartalert.model.Employees;
import com.codechef.smartalert.model.Languages;
import com.codechef.smartalert.model.Times;
import com.codechef.smartalert.model.Users;
import com.codechef.smartalert.util.MailUtil;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UsersDao usersDao;

	@Autowired
	private Message message;

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private KandyRestService kandyRestService;
	
	@Autowired
	public MailUtil mailUtil;

	public Users findUser(Users loginUser) throws JsonGenerationException, JsonMappingException, IOException {
		final Users user = usersDao.findUser(loginUser.getUsername(), loginUser.getPassword());
		if(user.getStatus().equalsIgnoreCase(ApplicationConstant.NON_VALIDATE_STATUS)) {
			final Map<?, ?> domainAccessToken = (Map<?,?>) kandyRestService.getDomainTokenAccess().get("result");
			request.getSession().setAttribute("ACCESS_TOKEN", domainAccessToken.get("domain_access_token").toString());
			kandyRestService.sendValidationViaSms(user.getUserDetails().getMobile());
		}
		return user;
	}
	
	
	

	public Message authenticateUser(final String username, final String password) throws JsonGenerationException, JsonMappingException, IOException {
		final Users user = usersDao.findUser(username, password);
		message.setData(user);
		message.setApiKey(KandyApiConstant.API_KEY);
		if (user == null) {
			message.setMessage("Invalid Username or Password!!!");
			request.getSession().setAttribute("user", user);
			return message;
		} 
		else if(user.getStatus().equalsIgnoreCase(ApplicationConstant.NON_VALIDATE_STATUS)) {
			final Map<?, ?> domainAccessToken = (Map<?,?>) kandyRestService.getDomainTokenAccess().get("result");
			request.getSession().setAttribute("ACCESS_TOKEN", domainAccessToken.get("domain_access_token").toString());
			kandyRestService.sendValidationViaSms(user.getUserDetails().getMobile());
			message.setMessage("Successfully Verified!!!");
			message.setMessage("Successs");
			request.getSession().setAttribute("user", user);
			return message;
		}
		else {
			message.setMessage("Successfully Logined!!!");
			message.setMessage("Success");
			final Map<?, ?> domainAccessToken = (Map<?,?>) kandyRestService.getDomainTokenAccess().get("result");
			request.getSession().setAttribute("ACCESS_TOKEN", domainAccessToken.get("domain_access_token").toString());
			request.getSession().setAttribute("user", user);
			return message;
		}
	}

	public Message isAuthenticateUser() {
		message.setApiKey(KandyApiConstant.API_KEY);
		message.setData(request.getSession().getAttribute("user"));
		return message;
	}

	public boolean logout() {
		request.getSession().invalidate();
		return true;
	}

	public Message createUserAccount(final Users newUser) throws JsonGenerationException, JsonMappingException, IOException {
		newUser.setStatus(ApplicationConstant.NON_VALIDATE_STATUS);
		newUser.setUserType(ApplicationConstant.USER);
		final Map<?, ?> domainAccessToken = (Map<?,?>) kandyRestService.getDomainTokenAccess().get("result");
		request.getSession().setAttribute("ACCESS_TOKEN", domainAccessToken.get("domain_access_token").toString());
		final String userId = getUserId(newUser.getName().substring(0, 2).toUpperCase()).toLowerCase();
		final Map<String, Object> registrationResponse = registerUsersToDomain(userId, "IN");
		System.out.println("seeeeeeeeeeeeeeee-------------------->"+new ObjectMapper().writeValueAsString(registrationResponse));
		if(Integer.parseInt(registrationResponse.get("status").toString()) == 0)
		{
			System.out.println("entered-------------------->");
			final Map<?, ?> userDetails = (Map<?,?>) registrationResponse.get("result");
			newUser.setUsername(userId);
			newUser.setPassword(userDetails.get("user_password").toString());
			Map<String, Object> sms = kandyRestService.sendValidationViaSms(newUser.getUserDetails().getMobile());
			final int createdUserId = usersDao.createUserAccount(newUser);
			if (createdUserId > 0) {
				mailUtil.sendCredentialsAsEmail(newUser.getUserDetails()
						.getEmail(), userId, userDetails.get("user_password")
						.toString());
				message.setMessage("User has been registered successfully!. Login credentials has been mailed to the your email id. Please verify your mobile number");
				message.setData(createdUserId);
				return message;
			} else {
				message.setMessage("User registeration failed!!!");
				message.setData(-1);
			}
			
			
			System.out.println("sms---------------------------->"+sms);
			return message;
		}
		else {
			message.setMessage("User registeration failed!!!");
			message.setData(-1);
			return message;
		}
	}
	
	public AppointmentDetails getAppointmentDetails() throws JsonGenerationException, JsonMappingException, IOException {
		return usersDao.getAppointmentDetails();
	}
	
	public List<Times> getTimes() throws JsonGenerationException, JsonMappingException, IOException {
		return usersDao.getTimes();
	}
	
	public List<Appointments> getAllocatedTimes(final Date selectedDate) throws JsonGenerationException, JsonMappingException, IOException {
		return usersDao.getAllocatedTimes(selectedDate);
	}

	public Message addNewAppointment(final Map<String, Object> newAppointment) throws ParseException {
		try {
			final Map<String, Object> userAndAgentDetails = usersDao.addNewAppointment(newAppointment);
			if (userAndAgentDetails != null) {
				final Map<String, String> addressBookDetails = kandyRestService.configureAddressBook(userAndAgentDetails.get("username").toString(), userAndAgentDetails.get("agentUsername").toString());
				System.out.println("YOOOOOOOOOOOOOOOOOOOOOOOOOO-------------------->"+new ObjectMapper().writeValueAsString(addressBookDetails));
				message.setMessage("Appointment successfully submitted!!!");
				message.setData(true);
			} else {
				message.setMessage("Appointment submission failed!!!");
				message.setData(false);
			}
		} catch (JsonGenerationException jsonGenerationException) {
			message.setMessage("Appointment submission failed!!!");
			message.setData(false);
			jsonGenerationException.printStackTrace();
		} catch (JsonMappingException jsonMappingException) {
			message.setMessage("Appointment submission failed!!!");
			message.setData(false);
			jsonMappingException.printStackTrace();
		} catch (IOException ioException) {
			message.setMessage("Appointment submission failed!!!");
			message.setData(false);
			ioException.printStackTrace();
		}
		return message;
	}

	public Message getScheduledAppointmentDetails(final String userId) {
		List<HashMap<String, Object>> appointments = null;
		try {
			appointments = usersDao.getScheduledAppointmentDetails(userId);
		} catch (JsonGenerationException jsonGenerationException) {
			message.setMessage("No Appointments");
			message.setData(appointments);
			jsonGenerationException.printStackTrace();
		} catch (JsonMappingException jsonMappingException) {
			message.setMessage("No Appointments");
			message.setData(appointments);
			jsonMappingException.printStackTrace();
		} catch (IOException ioException) {
			message.setMessage("No Appointments");
			message.setData(appointments);
			ioException.printStackTrace();
		}
		if (appointments.size() > 0) {
			message.setMessage("User Appointments Found");
			message.setData(appointments);
		} else {
			message.setMessage("No Appointments");
			message.setData(appointments);
		}
		return message;
	}

	public void changeUserStatus(final String userId) {
		usersDao.changeUserStatus(userId);
	}

	@SuppressWarnings("unchecked")
	public Message addNewEmployees(final Employees employee) {
		final String employeeId = getEmployeeId(employee.getFirstname().substring(0, 1).toUpperCase()+employee.getLastname().substring(0, 1).toUpperCase()).toLowerCase();
		employee.setEmpid(employeeId);
		final Map<String, Object> registrationResponse = registerUsersToDomain(employeeId, "IN");
		if(registrationResponse.get("status").toString().equalsIgnoreCase("0"))
		{
			final Map<String, String> responseResult = (Map<String, String>) registrationResponse.get("result");
			final int createdEmployeeId = usersDao.addNewEmployees(employee);
			if (createdEmployeeId > 0) {
				int savedAuthenticationDetails = usersDao.saveAuthenticationDetails(employeeId, responseResult.get("user_password"), createdEmployeeId);
				if (savedAuthenticationDetails > 0) {
					mailUtil.sendCredentialsAsEmail(employee.getEmail(), employeeId, responseResult.get("user_password"));
					message.setMessage("Employee has been added successfully!. Login credentials has been mailed to the employee's email id.");
					message.setData(createdEmployeeId);
				} else {
					message.setMessage("Employee adding failed!!!");
					message.setData(createdEmployeeId);
				}
				return message;
			} else {
				message.setMessage("Employee adding failed!!!");
				message.setData(createdEmployeeId);
				return message;
			}
		} else {
			message.setMessage("Employee adding failed!!!");
			message.setData(-1);
			return message;
		}
	}
	
	
	public Map<String, Object> registerUsersToDomain(final String userId, final String countryCode) {
		String domainToken = request.getSession().getAttribute("ACCESS_TOKEN").toString();
		if(domainToken == null)
		{
			final Map<?, ?> domainAccessToken = (Map<?,?>) kandyRestService.getDomainTokenAccess().get("result");
			domainToken = domainAccessToken.get("domain_access_token").toString();
			request.getSession().setAttribute("ACCESS_TOKEN", domainToken);
		}
		return kandyRestService.registerUsersToDomain(userId, domainToken, countryCode);
	}

	private String getEmployeeId(final String prefix) {
		return prefix+usersDao.getEmployeeIndex();
	}
	
	private String getUserId(final String prefix) {
		return prefix+usersDao.getUserIndex();
	}

	public List<Employees> getAllEmployees() {
		return usersDao.getAllEmployees();
	}

	public Message saveEditedEmployees(final Employees employee) {
		final int createdEmployeeId = usersDao.saveEditedEmployees(employee);
		if (createdEmployeeId > 0) {
			message.setMessage("Employee details saved successfully!!!");
			message.setData(employee.getId());
			return message;
		} else {
			message.setMessage("Employee details saving failed!!!");
			message.setData(employee.getId());
			return message;
		}
	}
	
	public HashMap<String, Object> getAllLanguagesAndAreas() throws JsonGenerationException, JsonMappingException, IOException {
		return usersDao.getAllLanguagesAndAreas();
	}

	public Message deleteEmployee(final String empId) {
		String domainToken = request.getSession().getAttribute("ACCESS_TOKEN").toString();
		if (domainToken == null) {
			final Map<?, ?> domainAccessToken = (Map<?, ?>) kandyRestService.getDomainTokenAccess().get("result");
			domainToken = domainAccessToken.get("domain_access_token").toString();
			request.getSession().setAttribute("ACCESS_TOKEN", domainToken);
		}
		final Users user = usersDao.getUser(empId);
		if (user != null) {
			final int isUserDeleted = kandyRestService.deleteUser(user.getUsername(), user.getPassword(), domainToken);
			if (isUserDeleted > 0) {
				final int deleted = usersDao.deleteEmployee(empId);
				if (deleted > 0) {
					final int loginDeleted = usersDao.deleteLoginCredentials(empId);
					if(loginDeleted > 0) {
						message.setMessage("Employee has been deleted successfully!!!");
						message.setData(deleted);
						return message;
					}
					else {
						message.setMessage("Employee deletion failed!!!");
						message.setData(deleted);
						return message;
					}
					
				} else {
					message.setMessage("Employee deletion failed!!!");
					message.setData(deleted);
					return message;
				}
			} else {
				message.setMessage("Employee deletion failed!!!");
				message.setData(isUserDeleted);
				return message;
			}
		} else {
			message.setMessage("Employee deletion failed!!!");
			message.setData(-1);
			return message;
		}
	}

	public List<Languages> getAllLanguages() throws JsonGenerationException, JsonMappingException, IOException {
		return usersDao.getAllLanguages();
	}

	public Message addSaveLanguage(final Languages language) throws JsonGenerationException, JsonMappingException, IOException {
		final int addSaveLanguageId = usersDao.addSaveLanguage(language);
		if (addSaveLanguageId > 0) {
			message.setMessage("Language has been added successfully!!!");
			message.setData(usersDao.getAllLanguages());
			return message;
		} else {
			message.setMessage("Language adding failed!!!");
			message.setData("");
			return message;
		}
	}

	public Message deleteLanguage(final String languageId) throws JsonGenerationException, JsonMappingException, IOException {
		final int deleted = usersDao.deleteLanguage(languageId);
		if (deleted > 0) {
			message.setMessage("Language has been deleted successfully!!!");
			message.setData(usersDao.getAllLanguages());
			return message;
		} else {
			message.setMessage("Language deletion failed!!!");
			message.setData("");
			return message;
		}
	}
	
	public List<AppointmentArea> getAppointmentArea() throws JsonGenerationException, JsonMappingException, IOException {
		return usersDao.getAppointmentArea();
	}

	public Message addSaveAreasTopics(final Map<String, Object> appointmentArea) throws JsonGenerationException, JsonMappingException, IOException {
		final int addSaveAreasTopicsId = usersDao.addSaveAreasTopics(appointmentArea);
		if (addSaveAreasTopicsId > 0) {
			message.setMessage("Area and Topics has been saved successfully!!!");
			message.setData(usersDao.getAppointmentArea());
			return message;
		} else {
			message.setMessage("Area and Topics saving failed!!!");
			message.setData("");
			return message;
		}
	}
	
	public List<Map<String, Object>> getAllScheduledAppointments()  {
		try {
			return usersDao.getAllScheduledAppointments();
		} catch (JsonGenerationException jsonGenerationException) {
			jsonGenerationException.printStackTrace();
			return null;
		} catch (JsonMappingException jsonMappingException) {
			jsonMappingException.printStackTrace();
			return null;
		} catch (IOException ioException) {
			ioException.printStackTrace();
			return null;
		}
	}
	
	public Map<String, Object> getEmployeeDetails(final Integer empid) throws JsonGenerationException, JsonMappingException, IOException
	{
		final Employees employee = usersDao.getEmployee(empid);
		final Map<String, Object> employeeDetail = new HashMap<String, Object>();
		employeeDetail.put("id", employee.getId());
		employeeDetail.put("firstname", employee.getFirstname());
		employeeDetail.put("lastname", employee.getLastname());
		employeeDetail.put("gender", employee.getGender());
		employeeDetail.put("dob", employee.getDob());
		employeeDetail.put("empid", employee.getEmpid());
		employeeDetail.put("languages", employee.getLanguages());
		employeeDetail.put("areaExpert", employee.getAreaExpert());
		employeeDetail.put("addressLine1", employee.getAddressLine1());
		employeeDetail.put("addressLine2", employee.getAddressLine2());
		employeeDetail.put("pin", employee.getPin());
		employeeDetail.put("mobile", employee.getMobile());
		employeeDetail.put("homePhone", employee.getHomePhone());
		employeeDetail.put("email", employee.getEmail());
		employeeDetail.put("status", employee.getStatus());
		return employeeDetail;
	}

	public List<Map<String, Object>> getScheduledAppointments(final String userId) {
		try {
			return usersDao.getScheduledAppointments(userId);
		} catch (JsonGenerationException jsonGenerationException) {
			jsonGenerationException.printStackTrace();
			return null;
		} catch (JsonMappingException jsonMappingException) {
			jsonMappingException.printStackTrace();
			return null;
		} catch (IOException ioException) {
			ioException.printStackTrace();
			return null;
		}
	}

	public List<Map<String, Object>> getAppoinmentDetails(final String roomId) {
		try {
			return usersDao.getAppointment(roomId);
		} catch (JsonGenerationException jsonGenerationException) {
			jsonGenerationException.printStackTrace();
			return null;
		} catch (JsonMappingException jsonMappingException) {
			jsonMappingException.printStackTrace();
			return null;
		} catch (IOException ioException) {
			ioException.printStackTrace();
			return null;
		}
	}

	public int saveChatHistory(final ChatHistory chatHistory) {
		return usersDao.saveChatHistory(chatHistory);
	}

	public List<ChatHistory> getChatHistory(final String roomId) {
		return usersDao.getChatHistory(roomId);
	}

	public Message saveAppointmentStatus(String appointmentId, String appointmentStatus) {
		usersDao.saveAppointmentStatus(appointmentId, appointmentStatus);
		message.setMessage("Appointment Status Updated Successfully!!!");
		message.setData(true);
		return message;
	}
}