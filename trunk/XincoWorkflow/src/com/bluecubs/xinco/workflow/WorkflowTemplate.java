package com.bluecubs.xinco.workflow;

import java.sql.Timestamp;
import java.util.ArrayList;

public class WorkflowTemplate {
    private Timestamp creationTime;

    private ArrayList<Node> nodes = null;

    private ArrayList<Transaction> transactions = null;

    private int template_id;

    private int id;

    private String description;

    public WorkflowTemplate() {
    }

    public boolean Build() {
        return true;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp val) {
        this.creationTime = val;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> val) {
        this.nodes = val;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> val) {
        this.transactions = val;
    }

    public int getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(int val) {
        this.template_id = val;
    }

    public int getId() {
        return id;
    }

    public void setId(int val) {
        this.id = val;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String val) {
        this.description = val;
    }
}
