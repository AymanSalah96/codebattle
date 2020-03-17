package com.aymansalah.codebattle.util;

import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;

public class NullAwareBeanUtilsBeanAndIgnoreIdProperty extends BeanUtilsBean {

    @Override
    public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if(value==null || name.equalsIgnoreCase("id"))return;
        super.copyProperty(dest, name, value);
    }

}