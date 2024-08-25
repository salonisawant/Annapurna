package com.annapurna.dao;

import java.sql.SQLException;
import java.util.List;

import com.annapurna.model.Subscription;

public interface SubscriptionDAO {
	Subscription getSubscriptionById(int id) throws SQLException;
    List<Subscription> getSubscriptionsByUserId(int userId) throws SQLException;
    void addSubscription(Subscription subscription) throws SQLException;
    void updateSubscription(Subscription subscription) throws SQLException;
    void deleteSubscription(int id) throws SQLException;
    List<Subscription> getActiveSubscriptions() throws SQLException;
}
