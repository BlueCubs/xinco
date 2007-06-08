package com.bluecubs.xinco.workflow;



public class Property {
    private int id;

    private int transactionId;

    private int nodeId;

    private String property;

    private int activityId;

    private String description;

    private String propertyString;

    private boolean propertyBool;

    private int propertyInt;

    public Property() {
    }

    public int getId() {
        return id;
    }

    public void setId(int val) {
        this.id = val;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int val) {
        this.transactionId = val;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int val) {
        this.nodeId = val;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String val) {
        this.property = val;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int val) {
        this.activityId = val;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String val) {
        this.description = val;
    }

    public String getPropertyString() {
        return propertyString;
    }

    public void setPropertyString(String val) {
        this.propertyString = val;
    }

    public boolean getPropertyBool() {
        return propertyBool;
    }

    public void setPropertyBool(boolean val) {
        this.propertyBool = val;
    }

    public int getPropertyInt() {
        return propertyInt;
    }

    public void setPropertyInt(int val) {
        this.propertyInt = val;
    }
}
