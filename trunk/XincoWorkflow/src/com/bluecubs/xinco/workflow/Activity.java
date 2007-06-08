package com.bluecubs.xinco.workflow;

import java.sql.Timestamp;
import java.util.Properties;

/**
 * Represents an action within a node or transaction. The actual activities 
 *    to be performed.
 */
public class Activity {
    /**
     * Activity id.
     */
    private int id;

    /**
     * Activity description.
     */
    private String description;
    
    /**
     * Resource this activity is assigned to.
     */
    private Resource assignedTo = null;

    /**
     * Date and time this activity is completed.
     */
    private Timestamp completedOn = null;

    /**
     * Date and time this activity is assigned.
     */
    private Timestamp assignedOn = null;

    /**
     * Activity properties
     */
    private Properties properties = null;

    public Activity() {
    }

    /**
     * Get activity id.
     */
    public int getId() {
        return id;
    }

    /**
     * Set activity id.
     */
    public void setId(int val) {
        this.id = val;
    }

    /**
     * Get to what resource the activity is assigned.
     */
    public Resource getAssignedTo() {
        return assignedTo;
    }

    /**
     * Set resource to whom this activity is assigned.
     */
    public void setAssignedTo(Resource val) {
        this.assignedTo = val;
    }

    /**
     * Get date/time the activity is completed.
     */
    public Timestamp getCompletedOn() {
        return completedOn;
    }

    /**
     * Set date/time the activity is completed.
     */
    public void setCompletedOn(Timestamp val) {
        this.completedOn = val;
    }

    /**
     * Get date/time the activity is assigned.
     */
    public Timestamp getAssignedOn() {
        return assignedOn;
    }

    /**
     * Set date/time the activity is assigned.
     */
    public void setAssignedOn(Timestamp val) {
        this.assignedOn = val;
    }

    /**
     * Execute the activity.
     */
    public boolean execute() {
        return true;
    }

    /**
     * Get activity properties.
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Set activity properties.
     */
    public void setProperties(Properties val) {
        this.properties = val;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
