package com.compremelhor.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table( name = "freight_type")
public class FreightType extends EntityModel {
	private static final long serialVersionUID = 1L;
	
	@Column(name = "ride_value")
	@NotNull
	private Double rideValue;
	
	@Column(name = "type_name")
	@NotNull
	private String typeName;
	
	@Column
	private Boolean scheduled;
	
	@Column(name="availability_schedule_work_days")
	private Integer availabilityScheduleWorkDays;
	
	@Column(name=" delay_work_days")
	private Integer delayWorkdays;
	
	@Column(name = "description")
	@NotNull @Size(max = 200)
	private String description;
	
	@ManyToOne
	@NotNull
	private Partner partner;
	
	public Double getRideValue() {
		return rideValue;
	}

	public void setRideValue(Double rideValue) {
		this.rideValue = rideValue;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public boolean isScheduled() {
		return scheduled;
	}

	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}

	public int getDelayWorkdays() {
		return delayWorkdays;
	}

	public void setDelayWorkdays(int delayWorkdays) {
		this.delayWorkdays = delayWorkdays;
	}

	public Integer getAvailabilityScheduleWorkDays() {
		return availabilityScheduleWorkDays;
	}

	public void setAvailabilityScheduleWorkDays(Integer availabilityScheduleWorkDays) {
		this.availabilityScheduleWorkDays = availabilityScheduleWorkDays;
	}
}
