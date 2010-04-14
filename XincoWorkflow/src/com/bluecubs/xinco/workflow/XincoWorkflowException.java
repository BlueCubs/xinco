package com.bluecubs.xinco.workflow;

import java.util.List;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoWorkflowException extends Error {

    String xinco_message = "";
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of <code>XincoWorkflowException</code> without detail message.
     */
    public XincoWorkflowException() {
        super();
        xinco_message = "";
    }

    /**
     * Constructs an instance of <code>XincoWorkflowException</code> with the specified detail message.
     * @param s the detail message.
     */
    public XincoWorkflowException(String s) {
        super(s);
        xinco_message = s;
    }

    XincoWorkflowException(List<String> messages) {
        for (String s : messages) {
            xinco_message += s + "\n";
        }
    }

    @Override
    public String toString() {
        return xinco_message;
    }
}
