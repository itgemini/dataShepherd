/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.writer;

import java.util.List;

import com.datashepherd.excel.helper.Children;

public record Elements(List<Structure> structures, List<Children> children, List<Conditional> conditional,
                       int headerRow) {
}
