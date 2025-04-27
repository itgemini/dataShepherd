package com.datashepherd.xml.pattern;

import com.datashepherd.xml.exception.XMLAPIException;
import com.datashepherd.xml.exception.XMLWarningHandler;

/**
 * Strategy interface for XML parsing.
 *
 * @param <T> the type parameter for the resulting mapped object.
 */
public interface XMLParsingStrategy<T> {
    /**
     * Parses an XML file and returns an object of type T.
     *
     * @param filePath the path to the XML file.
     * @param clazz    the class type to map to.
     * @return an object of type T mapped from XML.
     * @throws XMLAPIException if parsing fails.
     */
    T parse(String filePath, Class<T> clazz) throws XMLAPIException;

    /**
     * Sets the warning handler for XML parsing.
     *
     * @return warningHandler the warning handler to set.
     */
    XMLWarningHandler getWarningHandler();
}