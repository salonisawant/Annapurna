package com.annapurna.model;

import java.util.Date;

public class Subscription {
	private int id;
    private int userId;
    private int productId;
    private String subscriptionPlan;
    private Date subscriptionDate;
    private Date subscriptionEndDate;
    private int numberOfWeeks;
    private int numberOfDays;
    private String daysOfWeek;
    private int addressId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getSubscriptionPlan() {
		return subscriptionPlan;
	}
	public void setSubscriptionPlan(String subscriptionPlan) {
		this.subscriptionPlan = subscriptionPlan;
	}
	public Date getSubscriptionDate() {
		return subscriptionDate;
	}
	public void setSubscriptionDate(Date subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
	}
	public Date getSubscriptionEndDate() {
		return subscriptionEndDate;
	}
	public void setSubscriptionEndDate(Date subscriptionEndDate) {
		this.subscriptionEndDate = subscriptionEndDate;
	}
	public int getNumberOfWeeks() {
		return numberOfWeeks;
	}
	public void setNumberOfWeeks(int numberOfWeeks) {
		this.numberOfWeeks = numberOfWeeks;
	}
	public int getNumberOfDays() {
		return numberOfDays;
	}
	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	public String getDaysOfWeek() {
		return daysOfWeek;
	}
	public void setDaysOfWeek(String daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}
	public int getAddressId() {
		return addressId;
	}
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
	public Subscription(int id, int userId, int productId, String subscriptionPlan, Date subscriptionDate,
			Date subscriptionEndDate, int numberOfWeeks, int numberOfDays, String daysOfWeek, int addressId) {
		super();
		this.id = id;
		this.userId = userId;
		this.productId = productId;
		this.subscriptionPlan = subscriptionPlan;
		this.subscriptionDate = subscriptionDate;
		this.subscriptionEndDate = subscriptionEndDate;
		this.numberOfWeeks = numberOfWeeks;
		this.numberOfDays = numberOfDays;
		this.daysOfWeek = daysOfWeek;
		this.addressId = addressId;
	}
	public Subscription() {
		// TODO Auto-generated constructor stub
	}


	@java.lang.Override
	public java.lang.String toString() {
		return "Subscription{" +
				"id=" + id +
				", userId=" + userId +
				", productId=" + productId +
				", subscriptionPlan='" + subscriptionPlan + '\'' +
				", subscriptionDate=" + subscriptionDate +
				", subscriptionEndDate=" + subscriptionEndDate +
				", numberOfWeeks=" + numberOfWeeks +
				", numberOfDays=" + numberOfDays +
				", daysOfWeek='" + daysOfWeek + '\'' +
				", addressId=" + addressId +
				'}';
	}
}
