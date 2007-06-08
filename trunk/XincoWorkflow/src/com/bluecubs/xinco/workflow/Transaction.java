package com.bluecubs.xinco.workflow;

import java.util.ArrayList;

/**
 * Represents a transition between nodes.
 */
public class Transaction {
    /**
     * Transaction id
     */
    private int id;

    /**
     * Transaction description
     */
    private String description = null;

    /**
     * Node from which the transaction starts.
     */
    private Node from = null;

    /**
     * Node to which the transaction connects.
     */
    private Node to = null;

    /**
     * Activities needed to be completed for this transaction to be completed.
     */
    private ArrayList<Activity> activities = null;

    /**
     * Transaction properties
     */
    private ArrayList<Property> properties = null;

    public Transaction() {
    }

    /**
     * Get transaction id
     */
    public int getId() {
        return id;
    }

    /**
     * Set transaction id
     */
    public void setId(int val) {
        this.id = val;
    }

    /**
     * Get transaction description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set transaction description
     */
    public void setDescription(String val) {
        this.description = val;
    }

    /**
     * Get node from whihc the transaction is started
     */
    public Node getFrom() {
        return from;
    }

    /**
     * Set node from which transaction is started.
     */
    public void setFrom(Node val) {
        this.from = val;
    }

    /**
     * Get node to which the transaction connects after completing it's actions 
     *    if any.
     */
    public Node getTo() {
        return to;
    }

    /**
     * Set node to which the transaction connects after completing it's actions 
     *    if any.
     */
    public void setTo(Node val) {
        this.to = val;
    }

    /**
     * Get activities to be performed.
     */
    public ArrayList<Activity> getActivities() {
        return activities;
    }

    /**
     * Set activities to be performed.
     */
    public void setActivities(ArrayList<Activity> val) {
        this.activities = val;
    }

    /**
     * Get transaction properties
     */
    public ArrayList<Property> getProperties() {
        return properties;
    }

    /**
     * Set transaction properties.
     */
    public void setProperties(ArrayList<Property> val) {
        this.properties = val;
    }
}
