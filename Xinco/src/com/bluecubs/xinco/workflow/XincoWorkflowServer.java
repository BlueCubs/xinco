package com.bluecubs.xinco.workflow;

import com.bluecubs.xinco.core.server.XincoException;
import java.util.HashMap;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoWorkflowServer{

//    private HashMap<String, State> states = new HashMap<String, State>();
//    private HashMap<String, Transition> transitions = new HashMap<String, Transition>();
//    private int versionId;
//
//    public XincoWorkflowServer(String id) {
//        super(id);
//    }
//
//    public void addState(String name, int i) {
//        if (!states.containsKey(name)) {
//            states.put(name, registerPlace(name, i));
//        }
//    }
//
//    public void addState(String name) {
//        if (!states.containsKey(name)) {
//            states.put(name, registerPlace(name));
//        }
//    }
//
//    public void addTransition(String name, String resource) {
//        if (!transitions.containsKey(name)) {
//            transitions.put(name, registerTransition(name, resource));
//        }
//    }
//
//    public void linkStates(String left, String right, String transition,
//            //JavaScript integer or boolean expression
//            String expressionOut, String expressionIn) throws XincoException {
//        if (states.containsKey(left) && states.containsKey(right)) {
//            if (transitions.containsKey(transition)) {
//                transitions.get(transition).output(states.get(left), expressionOut);
//                transitions.get(transition).input(states.get(right), expressionIn);
//            } else {
//                throw new XincoException("Transition " + transition
//                        + " does no longer exist!");
//            }
//        } else {
//            throw new XincoException("State " + (states.containsKey(left) ? right : left)
//                    + " does no longer exist!");
//        }
//    }
//
//    public void initialize(HashMap attributes) throws XincoException {
//        try {
//            buildTemplate(attributes);
//        } catch (BossaException ex) {
//            throw new XincoException("initialization exception:\n" + ex.getLocalizedMessage());
//        }
//    }
//
//    /**
//     * @return the versionId
//     */
//    public int getVersionId() {
//        return versionId;
//    }
//
//    private void load(){
//
//    }
//
//    private void store(){
//
//    }
}
