/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.writer;

import com.datashepherd.helper.Children;

import java.util.List;

public record Elements(List<Structure> structures, List<Children> children, List<Conditional> conditional) {
}
