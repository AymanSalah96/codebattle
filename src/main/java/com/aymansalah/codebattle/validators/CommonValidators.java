package com.aymansalah.codebattle.validators;

public class CommonValidators {
    
    public static boolean isNull(Object o) {
        return null == o;
    }
    
    public static boolean isBlank(String s) {
        if(isNull(s))
            return true;
        s = s.replaceAll("\\s","");
        return s.isEmpty();
    }

    public static boolean isEmpty(String s){
        if(isNull(s))
            return true;
        return s.isEmpty();
    }

    public static boolean lengthInRange(String s, int min, int max){
        if(isNull(s))
            return true;
        return s.length() >= min && s.length() <= max;
    }

    public static boolean lengthNotExceededMax(String s, int max) {
        if(isNull(s))
            return true;
        return s.length() <= max;
    }

    public static boolean lengthNotLessMin(String s, int min) {
        if(isNull(s))
            return true;
        return s.length() >= min;
    }

}
