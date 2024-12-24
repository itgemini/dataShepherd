/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Child {
    Class<?> mappedBy();
    String referencedBy();
}
