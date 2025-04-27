package com.datashepherd.xml.helper;

import com.datashepherd.xml.exception.XMLAPIException;
import com.datashepherd.xml.pattern.XMLCommand;
import com.datashepherd.xml.pattern.XMLParsingStrategy;

/**
 * Command for reading XML using a given parsing strategy.
 *
 * @param <T> the type parameter for the mapped object.
 */
public class XMLReaderCommand<T> implements XMLCommand {

    private final String filePath;
    private final Class<T> clazz;
    private final XMLParsingStrategy<T> parsingStrategy;
    private T result;

    /**
     * Constructor.
     *
     * @param filePath        the XML file path.
     * @param clazz           the class type to map to.
     * @param parsingStrategy the strategy to use for parsing.
     */
    public XMLReaderCommand(String filePath, Class<T> clazz, XMLParsingStrategy<T> parsingStrategy) {
        this.filePath = filePath;
        this.clazz = clazz;
        this.parsingStrategy = parsingStrategy;
    }

    @Override
    public void execute() throws XMLAPIException {
        result = parsingStrategy.parse(filePath, clazz);
    }

    /**
     * Returns the result object after execution.
     *
     * @return the mapped object.
     */
    public T getResult() {
        return result;
    }
}