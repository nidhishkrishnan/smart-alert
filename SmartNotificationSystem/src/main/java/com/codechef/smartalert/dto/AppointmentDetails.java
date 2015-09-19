package com.codechef.smartalert.dto;

import java.util.List;

import com.codechef.smartalert.model.AppointmentArea;
import com.codechef.smartalert.model.Appointments;
import com.codechef.smartalert.model.Languages;

public class AppointmentDetails {
	public List<AppointmentArea> appointmentArea;
	public List<Languages> languages;
	public List<Appointments> appointments;

	public AppointmentDetails(final List<AppointmentArea> appointmentArea,final List<Languages> languages, final List<Appointments> appointments) {
		this.appointmentArea = appointmentArea;
		this.languages = languages;
		this.appointments = appointments;
	}

	public void setAppointmentArea(List<AppointmentArea> appointmentArea) {
		this.appointmentArea = appointmentArea;
	}

	public void setLanguages(List<Languages> languages) {
		this.languages = languages;
	}

	public void setAppointments(List<Appointments> appointments) {
		this.appointments = appointments;
	}

	public List<AppointmentArea> getAppointmentArea() {
		return appointmentArea;
	}

	public List<Languages> getLanguages() {
		return languages;
	}

	public List<Appointments> getAppointments() {
		return appointments;
	}
}
