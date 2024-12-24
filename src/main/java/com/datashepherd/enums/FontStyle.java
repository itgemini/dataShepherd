/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.enums;


public enum FontStyle {
    NORMAL("Normal"),
    BOLD("Bold"),
    ITALIC("Italic"),
    UNDERLINE("Underline"),
    STRIKETHROUGH("Strikethrough"),
    DOUBLE_UNDERLINE("DoubleUnderline");
    private final String value;

    FontStyle(String value){
        this.value=value;
    }

    public String getValue(){
        return value;
    }
}
