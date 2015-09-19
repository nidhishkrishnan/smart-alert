package com.codechef.smartalert.dto;

public class AppointmentAreaDetails {

	private Integer areaId;
	private String name;

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AppointmentAreaDetails(Integer areaId, String name) {
		super();
		this.areaId = areaId;
		this.name = name;
	}
}
