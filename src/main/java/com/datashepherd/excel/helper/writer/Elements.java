/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.writer;

import com.datashepherd.excel.helper.Children;

import java.util.List;

public record Elements(List<Structure> structures, List<Children> children, List<Conditional> conditional) {
}
