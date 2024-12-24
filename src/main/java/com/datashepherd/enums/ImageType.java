/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.enums;


public enum ImageType {
    PICTURE_TYPE_EMF(2),
    PICTURE_TYPE_WMF(3),
    PICTURE_TYPE_PICT(4),
    PICTURE_TYPE_JPEG(5),
    PICTURE_TYPE_PNG(6),
    PICTURE_TYPE_DIB(7);

    private final int typeIndex;
    ImageType(int typeIndex) {
        this.typeIndex=typeIndex;
    }

    public int getTypeIndex(){return typeIndex;}
}
