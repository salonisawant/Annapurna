package com.annapurna.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.annapurna.model.Subscription;

public class SubscriptionDAOImpl implements SubscriptionDAO {
    private Connection connection;

    public SubscriptionDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Subscription getSubscriptionById(int id) throws SQLException {
        String query = "SELECT * FROM subscriptions WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractSubscriptionFromResultSet(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Subscription> getSubscriptionsByUserId(int userId) throws SQLException {
        List<Subscription> subscriptions = new ArrayList<>();
        String query = "SELECT * FROM subscriptions WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    subscriptions.add(extractSubscriptionFromResultSet(rs));
                }
            }
        }
        return subscriptions;
    }

    @Override
    public void addSubscription(Subscription subscription) throws SQLException {
        String query = "INSERT INTO subscriptions (user_id, product_id, subscription_plan, subscription_date, subscription_end_date, number_of_weeks, number_of_days, days_of_week, address_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, subscription.getUserId());
            pstmt.setInt(2, subscription.getProductId());
            pstmt.setString(3, subscription.getSubscriptionPlan());
            pstmt.setDate(4, new java.sql.Date(subscription.getSubscriptionDate().getTime()));
            pstmt.setDate(5, new java.sql.Date(subscription.getSubscriptionEndDate().getTime()));
            pstmt.setInt(6, subscription.getNumberOfWeeks());
            pstmt.setInt(7, subscription.getNumberOfDays());
            pstmt.setString(8, subscription.getDaysOfWeek());
            pstmt.setInt(9, subscription.getAddressId());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    subscription.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void updateSubscription(Subscription subscription) throws SQLException {
        String query = "UPDATE subscriptions SET user_id = ?, product_id = ?, subscription_plan = ?, subscription_date = ?, subscription_end_date = ?, number_of_weeks = ?, number_of_days = ?, days_of_week = ?, address_id = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, subscription.getUserId());
            pstmt.setInt(2, subscription.getProductId());
            pstmt.setString(3, subscription.getSubscriptionPlan());
            pstmt.setDate(4, new java.sql.Date(subscription.getSubscriptionDate().getTime()));
            pstmt.setDate(5, new java.sql.Date(subscription.getSubscriptionEndDate().getTime()));
            pstmt.setInt(6, subscription.getNumberOfWeeks());
            pstmt.setInt(7, subscription.getNumberOfDays());
            pstmt.setString(8, subscription.getDaysOfWeek());
            pstmt.setInt(9, subscription.getAddressId());
            pstmt.setInt(10, subscription.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteSubscription(int id) throws SQLException {
        String query = "DELETE FROM subscriptions WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Subscription> getActiveSubscriptions() throws SQLException {
        List<Subscription> activeSubscriptions = new ArrayList<>();
        String query = "SELECT * FROM subscriptions WHERE subscription_end_date >= CURRENT_DATE";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                activeSubscriptions.add(extractSubscriptionFromResultSet(rs));
            }
        }
        return activeSubscriptions;
    }

    private Subscription extractSubscriptionFromResultSet(ResultSet rs) throws SQLException {
        Subscription subscription = new Subscription();
        subscription.setId(rs.getInt("id"));
        subscription.setUserId(rs.getInt("user_id"));
        subscription.setProductId(rs.getInt("product_id"));
        subscription.setSubscriptionPlan(rs.getString("subscription_plan"));
        subscription.setSubscriptionDate(rs.getDate("subscription_date"));
        subscription.setSubscriptionEndDate(rs.getDate("subscription_end_date"));
        subscription.setNumberOfWeeks(rs.getInt("number_of_weeks"));
        subscription.setNumberOfDays(rs.getInt("number_of_days"));
        subscription.setDaysOfWeek(rs.getString("days_of_week"));
        subscription.setAddressId(rs.getInt("address_id"));
        return subscription;
    }
}
