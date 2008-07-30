/*
 * Copyright 2004 Carlos Sanchez.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.oness.common.all;

import java.io.Serializable;
import java.text.DateFormat;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * Base class for objects, implementing toString, equals and hashCode using
 * commons-lang.
 * </p>
 * 
 * <p>
 * Also implements swallow clone using commons-beanutils.
 * </p>
 * 
 * <p>
 * Note that there are security and performance constraints with this
 * implementation, specifically they may fail under a security manager. Check
 * commons-lang documentation for more info.
 * </p>
 * 
 * @author Carlos Sanchez
 * @version $Revision: 1.7 $
 */
public abstract class BaseObject implements Serializable, Cloneable {

    protected final transient Log log = LogFactory.getLog(getClass());

    /**
     * This method delegates to ReflectionToStringBuilder.
     * 
     * @see java.lang.Object#toString()
     * @see ReflectionToStringBuilder
     * @see DateFormat#getDateTimeInstance()
     */
    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this).toString();
    }

    /**
     * This method delegates to EqualsBuilder.reflectionEquals()
     * 
     * @see java.lang.Object#equals(Object)
     * @see EqualsBuilder#reflectionEquals(Object, Object)
     */
    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    /**
     * This method delegates to HashCodeBuilder.reflectionHashCode()
     * 
     * @see java.lang.Object#hashCode()
     * @see HashCodeBuilder#reflectionHashCode(Object)
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Swallow cloning. This method delegates to BeanUtils.cloneBean(). Returns
     * <code>null</code> on any exception
     * 
     * @see java.lang.Object#clone()
     * @see BeanUtils#cloneBean(Object)
     * @see PropertyUtils#copyProperties(Object, Object)
     */
    @Override
    public Object clone() {
        try {
            return BeanUtils.cloneBean(this);
        } catch (Exception e) {
            log.error("Exception cloning bean: " + this + "\n" + "Exception was " + e + ": " + e.getLocalizedMessage());
            return null;
        }
    }
}