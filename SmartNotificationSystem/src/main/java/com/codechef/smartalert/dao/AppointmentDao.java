package com.codechef.smartalert.dao;

import java.io.IOException;
import java.util.List;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.codechef.smartalert.model.AppointmentArea;
import com.codechef.smartalert.model.Appointments;
import com.codechef.smartalert.model.Languages;
import com.codechef.smartalert.model.Times;

@Service
@Repository
public class AppointmentDao extends AbstractDao {

	@SuppressWarnings("unchecked")
	public List<AppointmentArea> getAppointmentArea(Session session) {
		final Criteria criteria = session.createCriteria(AppointmentArea.class);
		return (List<AppointmentArea>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Languages> getLanguages() {
		final Criteria criteria = getSession().createCriteria(Languages.class);
		final List<Languages> data = (List<Languages>)criteria.list();
		try {
			new ObjectMapper().writeValueAsString(data);
		} catch (JsonGenerationException jsonGenerationException) {
			jsonGenerationException.printStackTrace();
		} catch (JsonMappingException jsonMappingException) {
			jsonMappingException.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		return (List<Languages>)criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Times> getTimes(Session session) {
		final Criteria criteria = session.createCriteria(Times.class);
		return (List<Times>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Appointments> getAppointments(Session session) {
		final Criteria criteria = session.createCriteria(Appointments.class);
		return (List<Appointments>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<AppointmentArea> getAppointmentArea() throws JsonGenerationException, JsonMappingException, IOException {
		final Criteria criteria = getSession().createCriteria(AppointmentArea.class);
		final List<AppointmentArea> data = (List<AppointmentArea>)criteria.list();
		new ObjectMapper().writeValueAsString(data);
		return (List<AppointmentArea>)criteria.list();
	}
}
