package com.datashepherd.xml.exception;

import java.util.ArrayList;
import java.util.List;

public class XMLWarningHandler {
    private final List<String> warnings = new ArrayList<>();

    public void addWarning(String message) {
        warnings.add(message);
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }
}
