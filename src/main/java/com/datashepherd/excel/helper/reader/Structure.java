/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.reader;


import java.util.Optional;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.Cell;

public record Structure(String name, Integer order, Function<Cell, Optional<Object>> processor, Class<?> type) {
}
