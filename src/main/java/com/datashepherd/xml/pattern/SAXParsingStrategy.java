package com.datashepherd.xml.pattern;

import com.datashepherd.xml.exception.XMLAPIException;
import com.datashepherd.xml.exception.XMLWarningHandler;
import com.datashepherd.xml.helper.ConcurrentProcessor;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import java.io.FileInputStream;

/**
 * Concrete implementation of XMLParsingStrategy using SAXParser.
 *
 * @param <T> the type parameter.
 */
public class SAXParsingStrategy<T> implements XMLParsingStrategy<T> {
    private ConcurrentProcessor<T> processor;

    @Override
    public T parse(String filePath, Class<T> clazz) throws XMLAPIException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        try (FileInputStream fis = new FileInputStream(filePath)) {
            XMLEventReader eventReader = xmlInputFactory.createXMLEventReader(fis);
            processor = new ConcurrentProcessor<>(clazz, eventReader).start();
            return processor.build();
        } catch (Exception e) {
            throw new XMLAPIException("Error during SAX parsing", e);
        }
    }

    @Override
    public XMLWarningHandler getWarningHandler() {
        return processor.getWarningHandler();
    }
}