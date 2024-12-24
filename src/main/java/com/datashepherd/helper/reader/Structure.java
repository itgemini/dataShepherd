/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.reader;


import org.apache.poi.ss.usermodel.Cell;

import java.util.function.Function;

public record Structure(String name, Integer order, Function<Cell,Object> processor,Class<?> type) {
}
