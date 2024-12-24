/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer;

import org.apache.poi.ss.usermodel.Cell;

import java.util.function.BiConsumer;

public record Conditional(String name, BiConsumer<Cell,Object> processor) {
}
