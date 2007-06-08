package com.bluecubs.xinco.workflow;

import java.util.ArrayList;

/**
 * <font face="">Represents a node in the workflow.</font>
 */
public class Node {
    /**
     * Node id
     */
    private int id;

    /**
     * Node description
     */
    private String description;

    /**
     * Node properties.
     */
    private ArrayList<Property> properties;

    /**
     * Node related activities
     */
    private ArrayList<Activity> activities;

    public Node() {
    }

    /**
     * Retrieve node id
     */
    public int getId() {
        return id;
    }

    /**
     * Set node id
     */
    public void setId(int val) {
        this.id = val;
    }

    /**
     * Get node description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set node description
     */
    public void setDescription(String val) {
        this.description = val;
    }

    /**
     * Get node properties
     */
    public ArrayList<Property> getProperties() {
        return properties;
    }

    /**
     * Set node properties
     */
    public void setProperties(ArrayList<Property> val) {
        this.properties = val;
    }

    /**
     * Get node activities
     */
    public ArrayList<Activity> getActivities() {
        return activities;
    }

    /**
     * Set node activities
     */
    public void setActivities(ArrayList<Activity> val) {
        this.activities = val;
    }
}
