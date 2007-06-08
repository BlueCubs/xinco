package com.bluecubs.xinco.workflow;


/**
 * Represents a workflow
 */
public class WorkflowInstance extends WorkflowTemplate {
    private int workflow_id;

    private int workflowTemplateId;

    private int id;

    public WorkflowInstance() {
    }

    public int getWorkflow_id() {
        return workflow_id;
    }

    public void setWorkflow_id(int val) {
        this.workflow_id = val;
    }

    public int getWorkflowTemplateId() {
        return workflowTemplateId;
    }

    public void setWorkflowTemplateId(int val) {
        this.workflowTemplateId = val;
    }

    public int getId() {
        return id;
    }

    public void setId(int val) {
        this.id = val;
    }
}
