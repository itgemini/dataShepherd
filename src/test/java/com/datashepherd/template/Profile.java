/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd
.template;

import com.datashepherd.annotation.Cell;
import com.datashepherd.annotation.Image;
import com.datashepherd.annotation.Sheet;

@Sheet(name = "page")
public class Profile {
    @Image
    @Cell(firstRow = 1,firstColumn = 1,lastRow = 4,lastColumn = 3)
    private final byte[] photo;
    @Cell(firstRow = 6,firstColumn = 3,lastRow = 7,lastColumn = 4)
    private final String name;
    @Cell(firstRow = 9,firstColumn = 3,lastRow = 10,lastColumn = 4)
    private final String address;
    @Cell(firstRow = 12,firstColumn = 3,lastRow = 13,lastColumn = 4)
    private final String phone;
    public Profile(String phone, String address, String name, byte[] photo) {
        this.phone = phone;
        this.address = address;
        this.name = name;
        this.photo = photo;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
