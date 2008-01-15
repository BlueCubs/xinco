/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.exception;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoSettingException extends XincoException {

    /**
     * Creates a new instance of <code>XincoSettingException</code> without detail message.
     */
    public XincoSettingException() {
        super();
        xinco_message = "";
    }

    /**
     * Constructs an instance of <code>XincoSettingException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public XincoSettingException(String msg) {
        xinco_message = msg;
    }
}
