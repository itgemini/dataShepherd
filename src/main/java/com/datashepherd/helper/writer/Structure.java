/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer;

import org.apache.poi.ss.usermodel.Cell;

import java.util.function.BiConsumer;

public record Structure(String name, Integer order, BiConsumer<Cell,Object> processor) {}
