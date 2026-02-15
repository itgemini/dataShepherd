/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.writer.style.condional;

import java.io.Serializable;

@FunctionalInterface
public interface Command extends Serializable {
   void execute();
}