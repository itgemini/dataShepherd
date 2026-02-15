/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.datashepherd.excel.enums.ImageType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Picture {
    ImageType extension() default ImageType.PICTURE_TYPE_JPEG;
    String path();
    int startColumn();
    int startRow();
    int endColumn();
    int endRow();
}
