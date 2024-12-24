/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer.model;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Sheet;

abstract class Anchor extends Draw {
    protected ClientAnchor clientAnchor;
    Anchor(Sheet sheet) {
        super(sheet);
        clientAnchor = factory.createClientAnchor();
    }
}
