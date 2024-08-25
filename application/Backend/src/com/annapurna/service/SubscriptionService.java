package com.annapurna.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.annapurna.dao.SubscriptionDAO;
import com.annapurna.model.Subscription;

public class SubscriptionService {
    private SubscriptionDAO subscriptionDAO;

    public SubscriptionService(SubscriptionDAO subscriptionDAO) {
        this.subscriptionDAO = subscriptionDAO;
    }

    public void createSubscription(int userId, int productId, String plan, Date startDate, Date endDate, int duration, int days, String days_str, int addressId) throws SQLException {
        Subscription subscription = new Subscription();
        subscription.setUserId(userId);
        subscription.setProductId(productId);
        subscription.setSubscriptionPlan(plan);
        subscription.setSubscriptionDate(startDate);
        subscription.setSubscriptionEndDate(endDate);
        subscription.setNumberOfWeeks(duration);
        subscription.setNumberOfDays(days);
        subscription.setDaysOfWeek(days_str);
        subscription.setAddressId(addressId);
        // Set other fields as needed

        subscriptionDAO.addSubscription(subscription);
    }

    public void cancelSubscription(int subscriptionId) throws SQLException {
        subscriptionDAO.deleteSubscription(subscriptionId);
    }

    public List<Subscription> getActiveSubscriptions() throws SQLException {
        return subscriptionDAO.getActiveSubscriptions();
    }

	public List<Subscription> getSubscriptionsByUserId(int id) throws SQLException {
		// TODO Auto-generated method stub
		return subscriptionDAO.getSubscriptionsByUserId(id);
	}

    
}