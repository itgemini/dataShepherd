/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.order;

import com.datashepherd.annotation.Cell;
import com.datashepherd.annotation.Image;
import com.datashepherd.annotation.Sheet;

@Sheet(name = "Profile")
public class Profile {
    @Image
    @Cell(firstRow = 1,firstColumn = 1,lastRow = 4,lastColumn = 3)
    private byte[] photo;
    @Cell(firstRow = 6,firstColumn = 1,lastRow = 7,lastColumn = 2)
    private final String nameLabel = "Name";
    @Cell(firstRow = 6,firstColumn = 3,lastRow = 7,lastColumn = 4)
    private String name;
    @Cell(firstRow = 9,firstColumn = 1,lastRow = 10,lastColumn = 2)
    private final String addressLabel = "Address";
    @Cell(firstRow = 9,firstColumn = 3,lastRow = 10,lastColumn = 4)
    private String address;
    @Cell(firstRow = 12,firstColumn = 1,lastRow = 13,lastColumn = 2)
    private final String phoneLabel = "Phone";
    @Cell(firstRow = 12,firstColumn = 3,lastRow = 13,lastColumn = 4)
    private String phone;
    public Profile(String phone, String address, String name, byte[] photo) {
        this.phone = phone;
        this.address = address;
        this.name = name;
        this.photo = photo;
    }
}
