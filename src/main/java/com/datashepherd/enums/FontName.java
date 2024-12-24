/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.enums;


public enum FontName {
    ARIAL("Arial"),
    TIMES_NEW_ROMAN("Times New Roman"),
    CALIBRI("Calibri"),
    VERDANA("Verdana"),
    COURIER_NEW("Courier New"),
    TAHOMA("Tahoma"),
    LUCIDA_SANS_UNICODE("Lucida Sans Unicode"),
    GEORGIA("Georgia"),
    GARAMOND("Garamond"),
    TREBUCHET_MS("Trebuchet MS"),
    COMIC_SANS_MS("Comic Sans MS"),
    IMPACT("Impact"),
    BOOKMAN_OLD_STYLE("Bookman Old Style"),
    PALATINO_LINOTYPE("Palatino Linotype"),
    AVENIR("Avenir"),
    HELVETICA("Helvetica");

    private final String name;

    FontName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
