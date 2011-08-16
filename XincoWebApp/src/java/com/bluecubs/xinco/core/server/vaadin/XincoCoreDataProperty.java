//package com.bluecubs.xinco.core.server.vaadin;
//
//import com.bluecubs.xinco.core.server.service.XincoCoreData;
//import com.vaadin.data.Property;
//
///**
// *
// * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
// */
//public class XincoCoreDataProperty implements Property {
//
//    private XincoCoreData data = null;
//
//    public XincoCoreDataProperty(XincoCoreData data) {
//        this.data = data;
//    }
//
//    public Object getValue() {
//        return data;
//    }
//
//    @Override
//    public String toString() {
//        return data == null ? "Empty" : data.getDesignation();
//    }
//
//    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
//        if (newValue instanceof XincoCoreData) {
//            if (isReadOnly()) {
//                throw new ReadOnlyException();
//            }
//            data = (XincoCoreData) newValue;
//        } else {
//            // Don't know how to convert any other types
//            throw new ConversionException();
//        }
//    }
//
//    public Class<?> getType() {
//        return XincoCoreData.class;
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
