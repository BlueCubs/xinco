//package com.bluecubs.xinco.core.server.vaadin;
//
//import com.bluecubs.xinco.core.server.service.XincoCoreNode;
//import com.vaadin.data.Property;
//
///**
// *
// * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
// */
//public class XincoCoreNodeProperty implements Property {
//
//    private XincoCoreNode node = null;
//
//    public XincoCoreNodeProperty(XincoCoreNode node) {
//        this.node = node;
//    }
//
//    @Override
//    public String toString() {
//        return node == null ? "Empty" :node.getDesignation();
//    }
//
//    public Object getValue() {
//        return node;
//    }
//
//    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
//        if (newValue instanceof XincoCoreNode) {
//            if (isReadOnly()) {
//                throw new ReadOnlyException();
//            }
//            node = (XincoCoreNode) newValue;
//        } else {
//            // Don't know how to convert any other types
//            throw new ConversionException();
//        }
//    }
//
//    public Class<?> getType() {
//        return XincoCoreNode.class;
//    }
//
//    public boolean isReadOnly() {
//        return true;
//    }
//
//    public void setReadOnly(boolean newStatus) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//}
