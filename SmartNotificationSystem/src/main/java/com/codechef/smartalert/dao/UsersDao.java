package com.codechef.smartalert.dao;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.codechef.smartalert.constant.ApplicationConstant;
import com.codechef.smartalert.dto.AppointmentDetails;
import com.codechef.smartalert.model.AppointmentArea;
import com.codechef.smartalert.model.AppointmentAreaTopics;
import com.codechef.smartalert.model.Appointments;
import com.codechef.smartalert.model.ChatHistory;
import com.codechef.smartalert.model.Employees;
import com.codechef.smartalert.model.Languages;
import com.codechef.smartalert.model.Times;
import com.codechef.smartalert.model.Users;

@Service
@Repository
public class UsersDao extends AbstractDao {

	final ObjectMapper objectMapper = new ObjectMapper();
	
	public Users findUser(final String username, final String password) throws JsonGenerationException, JsonMappingException, IOException {
		Criteria criteria = getSession().createCriteria(Users.class);
		criteria.add(Restrictions.eq("username", username));
		criteria.add(Restrictions.eq("password", password));
		criteria.add(Restrictions.or(Restrictions.eq("status", ApplicationConstant.ACTIVE_STATUS), Restrictions.eq("status", ApplicationConstant.NON_VALIDATE_STATUS)));
		final Users data = (Users) criteria.uniqueResult();
		objectMapper.writeValueAsString(data);
		return (Users) criteria.uniqueResult();
	}
	
	public Employees getEmployee(final int empId) throws JsonGenerationException, JsonMappingException, IOException {
		return (Employees) getSession().load(Employees.class, empId);
	}

	public int createUserAccount(final Users newUser) {
		return save(newUser);
	}
	
	public AppointmentDetails getAppointmentDetails() throws JsonGenerationException, JsonMappingException, IOException {
		final List<AppointmentArea> appointmentArea = getAppointmentArea();
		final List<Languages> languages = getLanguages();
		final List<Appointments> appointments = getAppointments();
		return new AppointmentDetails(appointmentArea, languages, appointments);
	}
	
	@SuppressWarnings("unchecked")
	public List<AppointmentArea> getAppointmentArea() throws JsonGenerationException, JsonMappingException, IOException {
		final Criteria criteria = getSession().createCriteria(AppointmentArea.class);
		final List<AppointmentArea> data = (List<AppointmentArea>)criteria.list();
		objectMapper.writeValueAsString(data);
		return (List<AppointmentArea>)criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Languages> getLanguages() throws JsonGenerationException, JsonMappingException, IOException {
		final Criteria criteria = getSession().createCriteria(Languages.class);
		return (List<Languages>)criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Times> getTimes() throws JsonGenerationException, JsonMappingException, IOException {
		final Criteria criteria = getSession().createCriteria(Times.class);
		return (List<Times>)criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Appointments> getAppointments() throws JsonGenerationException, JsonMappingException, IOException {
		final Criteria criteria = getSession().createCriteria(Appointments.class);
		final List<Appointments> data = (List<Appointments>)criteria.list();
		objectMapper.writeValueAsString(data);
		return (List<Appointments>)criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Appointments> getAllocatedTimes(final Date selectedDate) throws JsonGenerationException, JsonMappingException, IOException {
		final Criteria criteria = getSession().createCriteria(Appointments.class);
		criteria.add(Restrictions.eq("date", selectedDate));
		criteria.setProjection(Projections.property("timeId"));
		final List<Appointments> data = (List<Appointments>)criteria.list();
		objectMapper.writeValueAsString(data);
		return (List<Appointments>)criteria.list();
	}

	public Map<String, Object> addNewAppointment(Map<String, Object> newAppointment) throws ParseException, JsonGenerationException, JsonMappingException, IOException {
	  final Map<String, Object> userAndAgentDetails = new HashMap<String, Object>();
	  final Date date =	new Date();
	  final Date appointmentDate = new Date(Long.parseLong(newAppointment.get("date").toString()));
	  final int timeId = Integer.parseInt(newAppointment.get("time").toString());
	  final int languageId = Integer.parseInt(newAppointment.get("language").toString());
	 
	  final AppointmentArea appointmentArea = (AppointmentArea) getSession().load(AppointmentArea.class, Integer.parseInt(newAppointment.get("areaId").toString()));
	  final Map<String, Object> agentIdAndTranslationDetails = getAgentIdAndTranslationDetails(appointmentDate, appointmentArea.getAreaId(), timeId, languageId);
	  final Users users = (Users) getSession().load(Users.class, Integer.parseInt(newAppointment.get("userId").toString()));
	 
	  final Appointments appointments = new Appointments();
	  appointments.setAppointmentArea(appointmentArea);
	  appointments.setUsers(users);
	  appointments.setDate(appointmentDate);
	  appointments.setLanguage(languageId);
	  appointments.setTimeId(timeId);
	  appointments.setTranslationRequired(Integer.parseInt(agentIdAndTranslationDetails.get("translationRequired").toString()));
	  appointments.setBankAgentId(Integer.parseInt(agentIdAndTranslationDetails.get("empId").toString()));
	  appointments.setTopics(newAppointment.get("topics").toString());
	  appointments.setStatus(ApplicationConstant.ACTIVE_STATUS);
	  appointments.setCreatedDate(date);
	  appointments.setModifiedDate(date);
	  appointments.setAgentStatus("Offline");
	  final int success = save(appointments);
	  if (success > 0) {
		userAndAgentDetails.put("username", users.getUsername());
		userAndAgentDetails.put("agentUsername", agentIdAndTranslationDetails.get("empUsername"));
	  } else {
			return null;
	  }
	  return userAndAgentDetails;
	}

	public List<HashMap<String, Object>> getScheduledAppointmentDetails(final String userId) throws JsonGenerationException, JsonMappingException, IOException {
		final List<HashMap<String, Object>> scheduledAppointmentDetails=new ArrayList<HashMap<String, Object>>();
		final List<Appointments> appointments = getAllocatedAppointments(userId);
        for(int index = 0; index < appointments.size(); index++) {
           final HashMap<String, Object> appointment = new HashMap<String, Object>();
           final Times times = (Times) getSession().load(Times.class, appointments.get(index).getTimeId());
           final Languages languages = (Languages) getSession().load(Languages.class, appointments.get(index).getLanguage());
           appointment.put("id", appointments.get(index).getId());
           appointment.put("timeValue", times.getTimeValue());
           appointment.put("areaName", appointments.get(index).getAppointmentArea().getName());
           appointment.put("languageName", languages.getLanguageName());
           appointment.put("date", appointments.get(index).getDate().getTime());
           final Employees agent = (Employees) getSession().load(Employees.class, appointments.get(index).getBankAgentId());
           appointment.put("agentId", agent.getEmpid());
           final List<Integer> topics = objectMapper.readValue(appointments.get(index).getTopics().toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class));//appointments.get(index).getTopics();
           final List<String> areaTopics = new ArrayList<String>();
           for(int topicsIndex = 0; topicsIndex < topics.size(); topicsIndex++) {
        	   final AppointmentAreaTopics appointmentAreaTopics = (AppointmentAreaTopics) getSession().load(AppointmentAreaTopics.class, topics.get(topicsIndex));
        	   areaTopics.add(appointmentAreaTopics.getTopicName());
           }
           appointment.put("areaTopic", areaTopics);
           scheduledAppointmentDetails.add(appointment);
         }
		
		return scheduledAppointmentDetails;
	}

	@SuppressWarnings("unchecked")
	private List<Appointments> getAllocatedAppointments(final String userId) throws JsonGenerationException, JsonMappingException, IOException {
		final Criteria criteria = getSession().createCriteria(Appointments.class);
		final Users user = (Users) getSession().load(Users.class, Integer.parseInt(userId));
		criteria.add(Restrictions.eq("users", user));
		criteria.add(Restrictions.eq("status", ApplicationConstant.ACTIVE_STATUS));
		final List<Appointments> data = (List<Appointments>)criteria.list();
		objectMapper.writeValueAsString(data);
		return (List<Appointments>)criteria.list();
	}

	public void changeUserStatus(final String userId) {
		 final Users users = (Users) getSession().load(Users.class, Integer.parseInt(userId));
		 users.setStatus("active");
		 saveOrUpdate(users);
	}

	public int addNewEmployees(final Employees employee) {
		return save(employee);
	}

	@SuppressWarnings("unchecked")
	public List<Employees> getAllEmployees() {
		final Criteria criteria = getSession().createCriteria(Employees.class);
		return (List<Employees>)criteria.list();
	}

	public int saveEditedEmployees(Employees employee) {
		try {
			saveOrUpdate(employee);
			return 1;
		} catch (Exception exception) {
			exception.printStackTrace();
			return -1;
		}
	}
	
	public HashMap<String, Object> getAllLanguagesAndAreas() throws JsonGenerationException, JsonMappingException, IOException {
		final List<HashMap<String, Object>> appointmentArea = getOnlyAppointmentArea();
		final List<Languages> languages = getLanguages();
		final HashMap<String, Object> allLanguagesAndAreas = new HashMap<String, Object>();
		allLanguagesAndAreas.put("appointmentArea", appointmentArea);
		allLanguagesAndAreas.put("languages", languages);
		return allLanguagesAndAreas;
	}

	@SuppressWarnings("unchecked")
	private List<HashMap<String, Object>> getOnlyAppointmentArea() throws JsonGenerationException, JsonMappingException, IOException {
		final Criteria criteria = getSession().createCriteria(AppointmentArea.class);
		
		final List<AppointmentArea> areas = (List<AppointmentArea>)criteria.list();
		final List<HashMap<String, Object>> allAppointmentArea = new ArrayList<HashMap<String,Object>>();
		 for(int areasIndex = 0; areasIndex < areas.size(); areasIndex++) {
			 final HashMap<String, Object> appointmentAreasObject = new HashMap<String, Object>();
			 appointmentAreasObject.put("areaId", areas.get(areasIndex).getAreaId());
			 appointmentAreasObject.put("name", areas.get(areasIndex).getName());
			 allAppointmentArea.add(appointmentAreasObject);
         }
		objectMapper.writeValueAsString(areas);
		return allAppointmentArea;
	}

	public int deleteEmployee(final String empId) {
		try {
		 final Employees employees = (Employees) getSession().createCriteria(Employees.class).add(Restrictions.eq("id", Integer.parseInt(empId))).uniqueResult();
		 return delete(employees);
		} catch(Exception exception) {
			exception.printStackTrace();
			return 0;
		}
	}

	public List<Languages> getAllLanguages() throws JsonGenerationException, JsonMappingException, IOException {
		return getLanguages();
	}

	public int addSaveLanguage(final Languages language) {
		try {
		  saveOrUpdate(language);
		 return 1;
		} catch(Exception exception) {
			exception.printStackTrace();
			return 0;
		}
	}

	public int deleteLanguage(final String languageId) {
		try {
		 final Languages languages = (Languages) getSession().createCriteria(Languages.class).add(Restrictions.eq("id", Integer.parseInt(languageId))).uniqueResult();
		 delete(languages);
		 return 1;
		} catch(Exception exception) {
			exception.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public int addSaveAreasTopics(final Map<String, Object> appointmentArea) {
		try {
			final AppointmentArea area = new AppointmentArea();
			final Date date = new Date();
			area.setName(appointmentArea.get("name").toString());
			area.setDescription(appointmentArea.get("description").toString());
			area.setCreatedDate(date);
			area.setModifiedDate(date);
			final List<HashMap<String, Object>> topics = (List<HashMap<String, Object>>) appointmentArea.get("appointmentAreaTopics");
			final int areaId = save(area);
			final AppointmentArea areaObject = (AppointmentArea) getSession().load(AppointmentArea.class, areaId);
			for (int topicsIndex = 0; topicsIndex < topics.size(); topicsIndex++) {
				final AppointmentAreaTopics topic = new AppointmentAreaTopics();
				topic.setAppointmentArea(areaObject);
				topic.setTopicName(topics.get(topicsIndex).get("topicName").toString());
				topic.setCreatedDate(date);
				topic.setModifiedDate(date);
				save(topic);
			}
			return 1;
		} catch (Exception exception) {
			exception.printStackTrace();
			return 0;
		}
	}

	public int getEmployeeIndex() {
		final String mysqlAutoincrementQuery = "SELECT AUTO_INCREMENT as id FROM information_schema.tables WHERE table_name = :mysqlTableName AND table_schema = DATABASE()";
		return (Integer) getSession().createSQLQuery(mysqlAutoincrementQuery).addScalar("id", IntegerType.INSTANCE).setParameter("mysqlTableName", "Employees").uniqueResult();
	}
	
	public int getUserIndex() {
		final String mysqlAutoincrementQuery = "SELECT AUTO_INCREMENT as id FROM information_schema.tables WHERE table_name = :mysqlTableName AND table_schema = DATABASE()";
		return (Integer) getSession().createSQLQuery(mysqlAutoincrementQuery).addScalar("id", IntegerType.INSTANCE).setParameter("mysqlTableName", "Users").uniqueResult();
	}

	public int saveAuthenticationDetails(final String username, final String password, final int userId) {
		Users users = new Users();
		users.setAccountType(String.valueOf(userId));
		users.setUserType("staff");
		users.setUsername(username);
		users.setPassword(password);
		users.setName(username);
		users.setStatus(ApplicationConstant.ACTIVE_STATUS);
		return save(users);
	}

	public Users getUser(final String empId) {
		try {
		 return (Users) getSession().createCriteria(Users.class).add(Restrictions.eq("accountType", empId)).uniqueResult();
		} catch(Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}

	public int deleteLoginCredentials(final String empId) {
		try {
		 final Users users = (Users) getSession().createCriteria(Users.class).add(Restrictions.eq("accountType", empId)).uniqueResult();
		 delete(users);
		 return 1;
		} catch(Exception exception) {
			exception.printStackTrace();
			return 0;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getAgentIdAndTranslationDetails(final Date appointmentDate, final int areaId, final int timeId, final int languageId) throws JsonGenerationException, JsonMappingException, IOException
	{
		final Query query = getSession().createQuery("FROM Employees as emp WHERE status=:status AND emp.id NOT IN (SELECT bankAgentId FROM Appointments WHERE date !=:date AND timeId!=:timeId AND appointmentArea.areaId!=:areaId)");
        final Map<String, Object> employeeDetails = new HashMap<String, Object>();
		query.setParameter("status", "active");  
        query.setParameter("date", appointmentDate);
        query.setParameter("timeId", timeId);
        query.setParameter("areaId", areaId);
		final List<Employees> employeeList = query.list();
		int empId = 0;
	    String empUsername = null;
        for(int employeeIndex= 0; employeeIndex < employeeList.size(); employeeIndex++) {
        	final Employees employee = employeeList.get(employeeIndex);
        	final List<Languages> languages = objectMapper.readValue(employee.getLanguages(), objectMapper.getTypeFactory().constructCollectionType(List.class, Languages.class));
			for (int languageIndex = 0; languageIndex < languages.size(); languageIndex++) {
				if (languages.get(languageIndex).getId() == languageId) {
					empId = employee.getId();
					empUsername = employee.getEmpid();
					languageIndex = languages.size();
					employeeIndex = employeeList.size();
				}
			}
        }
		if (empId == 0 && employeeList.size() > 0) {
			final Employees employee = employeeList.get(0);
			employeeDetails.put("empUsername", employee.getEmpid());
			employeeDetails.put("empId", employee.getId());
			employeeDetails.put("translationRequired", 1); // 1 - Yes, 0 - No
		} else {
			employeeDetails.put("empUsername", empUsername);
			employeeDetails.put("empId", empId);
			employeeDetails.put("translationRequired", 1); // 1 - Yes, 0 - No
		}
		return employeeDetails;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAllScheduledAppointments() throws JsonGenerationException, JsonMappingException, IOException
	{
		final Criteria criteria = getSession().createCriteria(Appointments.class);
		final List<Map<String, Object>> fullAppointmentDetails = new ArrayList<Map<String, Object>>();
		Map<String, Object> appointmentDetails = new HashMap<String, Object>();
		final List<Appointments> appointments = (List<Appointments>)criteria.list();
	    for(int i=0; i<appointments.size();i++)
	    {
	    	 appointmentDetails = new HashMap<String, Object>();
	    	 final Languages languages = (Languages) getSession().load(Languages.class, appointments.get(i).getLanguage());
	    	 final Employees employees = (Employees) getSession().load(Employees.class, appointments.get(i).getBankAgentId());
	    	 final Times time = (Times) getSession().load(Times.class, appointments.get(i).getTimeId());
	    	
	    	 appointmentDetails.put("appointmentDate", appointments.get(i).getDate());
	    	 appointmentDetails.put("transalation", appointments.get(i).getTranslationRequired());
	    	 appointmentDetails.put("bookedDate", appointments.get(i).getCreatedDate());
	    	 appointmentDetails.put("agentEmpid", employees.getEmpid());
	    	 appointmentDetails.put("agentName", employees.getFirstname()+" "+employees.getLastname());
	    	 appointmentDetails.put("areaName", appointments.get(i).getAppointmentArea().getName());
	    	 appointmentDetails.put("appointmentTime", time.getTimeValue());
	    	 appointmentDetails.put("language", languages.getLanguageName());
	    	 appointmentDetails.put("userName", appointments.get(i).getUsers().getName());
	    	 
			final List<Integer> allTopics = objectMapper.readValue(appointments.get(i).getTopics(), objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class));
			String allTopicNames = "";
			for (int topicIndex = 0; topicIndex < allTopics.size(); topicIndex++) {
				final AppointmentAreaTopics topics = (AppointmentAreaTopics) getSession().load(AppointmentAreaTopics.class,allTopics.get(topicIndex));
				allTopicNames += topics.getTopicName();
				if (topicIndex + 1 != allTopics.size()) {
					allTopicNames += ", ";
				}
			}
			appointmentDetails.put("topics", allTopicNames);
			fullAppointmentDetails.add(appointmentDetails);
	    }
	    return fullAppointmentDetails;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getScheduledAppointments(final String bankAgentId) throws JsonParseException, JsonMappingException, IOException {
		Criteria criteria = getSession().createCriteria(Appointments.class);
		criteria.add(Restrictions.eq("bankAgentId", Integer.parseInt(bankAgentId)));
		criteria.add(Restrictions.eq("status", ApplicationConstant.ACTIVE_STATUS));

		final List<Map<String, Object>> fullAppointmentDetails = new ArrayList<Map<String, Object>>();
		Map<String, Object> appointmentDetails = new HashMap<String, Object>();
		final List<Appointments> appointments = (List<Appointments>)criteria.list();
	    for(int i=0; i<appointments.size();i++)
	    {
	    	 appointmentDetails = new HashMap<String, Object>();
	    	 final Languages languages = (Languages) getSession().load(Languages.class, appointments.get(i).getLanguage());
	    	 final Employees employees = (Employees) getSession().load(Employees.class, appointments.get(i).getBankAgentId());
	    	 final Times time = (Times) getSession().load(Times.class, appointments.get(i).getTimeId());
	    	 appointmentDetails.put("appointmentId", appointments.get(i).getId());
	    	 appointmentDetails.put("appointmentDate", appointments.get(i).getDate());
	    	 appointmentDetails.put("transalation", appointments.get(i).getTranslationRequired());
	    	 appointmentDetails.put("bookedDate", appointments.get(i).getCreatedDate());
	    	 appointmentDetails.put("agentEmpid", employees.getEmpid());
	    	 appointmentDetails.put("agentName", employees.getFirstname()+" "+employees.getLastname());
	    	 appointmentDetails.put("areaName", appointments.get(i).getAppointmentArea().getName());
	    	 appointmentDetails.put("appointmentTime", time.getTimeValue());
	    	 appointmentDetails.put("language", languages.getLanguageName());
	    	 appointmentDetails.put("userName", appointments.get(i).getUsers().getName());
	    	 appointmentDetails.put("agentStatus", appointments.get(i).getAgentStatus());
	    	 
			final List<Integer> allTopics = objectMapper.readValue(appointments.get(i).getTopics(), objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class));
			String allTopicNames = "";
			for (int topicIndex = 0; topicIndex < allTopics.size(); topicIndex++) {
				final AppointmentAreaTopics topics = (AppointmentAreaTopics) getSession().load(AppointmentAreaTopics.class,allTopics.get(topicIndex));
				allTopicNames += topics.getTopicName();
				if (topicIndex + 1 != allTopics.size()) {
					allTopicNames += ", ";
				}
			}
			appointmentDetails.put("topics", allTopicNames);
			fullAppointmentDetails.add(appointmentDetails);
	    }
	    return fullAppointmentDetails;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAppointment(final String roomId) throws JsonParseException, JsonMappingException, IOException {
		Criteria criteria = getSession().createCriteria(Appointments.class);
		criteria.add(Restrictions.eq("id", Integer.parseInt(roomId)));
		criteria.add(Restrictions.eq("status", ApplicationConstant.ACTIVE_STATUS));

		final List<Map<String, Object>> fullAppointmentDetails = new ArrayList<Map<String, Object>>();
		Map<String, Object> appointmentDetails = new HashMap<String, Object>();
		final List<Appointments> appointments = (List<Appointments>)criteria.list();
	    for(int i=0; i<appointments.size();i++)
	    {
	    	 appointmentDetails = new HashMap<String, Object>();
	    	 final Languages languages = (Languages) getSession().load(Languages.class, appointments.get(i).getLanguage());
	    	 final Employees employees = (Employees) getSession().load(Employees.class, appointments.get(i).getBankAgentId());
	    	 final Times time = (Times) getSession().load(Times.class, appointments.get(i).getTimeId());
	    	 appointmentDetails.put("appointmentId", appointments.get(i).getId());
	    	 appointmentDetails.put("appointmentDate", appointments.get(i).getDate());
	    	 appointmentDetails.put("transalation", appointments.get(i).getTranslationRequired());
	    	 appointmentDetails.put("bookedDate", appointments.get(i).getCreatedDate());
	    	 appointmentDetails.put("agentEmpid", employees.getEmpid());
	    	 appointmentDetails.put("agentEmailId", employees.getEmpid()+"@smartbank.techmahindra.com");
	    	 appointmentDetails.put("userEmailId", appointments.get(i).getUsers().getUsername()+"@smartbank.techmahindra.com");
	    	 appointmentDetails.put("agentName", employees.getFirstname()+" "+employees.getLastname());
	    	 appointmentDetails.put("areaName", appointments.get(i).getAppointmentArea().getName());
	    	 appointmentDetails.put("appointmentTime", time.getTimeValue());
	    	 appointmentDetails.put("language", languages.getLanguageName());
	    	 appointmentDetails.put("userName", appointments.get(i).getUsers().getName());
	    	 
			final List<Integer> allTopics = objectMapper.readValue(appointments.get(i).getTopics(), objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class));
			String allTopicNames = "";
			for (int topicIndex = 0; topicIndex < allTopics.size(); topicIndex++) {
				final AppointmentAreaTopics topics = (AppointmentAreaTopics) getSession().load(AppointmentAreaTopics.class,allTopics.get(topicIndex));
				allTopicNames += topics.getTopicName();
				if (topicIndex + 1 != allTopics.size()) {
					allTopicNames += ", ";
				}
			}
			appointmentDetails.put("topics", allTopicNames);
			fullAppointmentDetails.add(appointmentDetails);
	    }
	    return fullAppointmentDetails;
	}

	public int saveChatHistory(final ChatHistory chatHistory) {
		return save(chatHistory);
	}

	@SuppressWarnings("unchecked")
	public List<ChatHistory> getChatHistory(final String roomId) {
		final Criteria criteria = getSession().createCriteria(ChatHistory.class);
		criteria.add(Restrictions.eq("appointmentId", Integer.parseInt(roomId)));
		return (List<ChatHistory>)criteria.list();
	}

	public int saveAppointmentStatus(String appointmentId, String appointmentStatus) {
		try {
			 final Appointments appointments = (Appointments) getSession().load(Appointments.class, Integer.parseInt(appointmentId));
			 appointments.setStatus(appointmentStatus);
			 saveOrUpdate(appointmentStatus);
			 return 1;
			} catch(Exception exception) {
				exception.printStackTrace();
				return 0;
			}
	}

}
