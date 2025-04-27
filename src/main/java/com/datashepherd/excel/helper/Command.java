/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper;


@FunctionalInterface
public interface Command {

    /**
     * Executes the command.
     */
    void execute();
}