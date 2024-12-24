/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper;


@FunctionalInterface
public interface Command {

    /**
     * Executes the command.
     */
    void execute();
}