package com.codechef.smartalert.service;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codechef.smartalert.dao.AppointmentDao;
import com.codechef.smartalert.model.Languages;

@Service
public class AppointmentService {

	@Autowired
	private AppointmentDao appointmentDao;

	public List<Languages> getLanguages() throws JsonGenerationException, JsonMappingException, IOException {
		return appointmentDao.getLanguages();
	}
}
