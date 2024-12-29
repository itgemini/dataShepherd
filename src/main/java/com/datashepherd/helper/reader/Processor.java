/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.helper.reader;


import com.datashepherd.annotation.Parent;
import com.datashepherd.exception.ReadException;
import com.datashepherd.helper.Children;
import com.datashepherd.service.Reader;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Processor<T> {

    private final Class<T> entityClass;
    private final List<Children> subs;
    private final Workbook workbook;

    public Processor(Class<T> entityClass, List<Children> subs, Workbook workbook) {
        this.entityClass = entityClass;
        this.subs = subs;
        this.workbook = workbook;
    }

    public void processChild(List<T> parents) {
        for (Children children : subs) {
            Collection<?> list = new Reader<>(workbook, children.mappedBy()).read();
            for (T parent : parents) {
                try {
                    processSingleChild(parent, children, list);
                } catch (ReadException e) {
                    throw new ReadException("Failed to process child", e);
                }
            }
        }
    }

    private void processSingleChild(T parent, Children children, Collection<?> list) throws ReadException {
        try {
            Field mapper = entityClass.getDeclaredField(children.name());
            Field referencedBy = getReferencedByField(children);
            String reference = getReferenceAnnotation(referencedBy).reference();
            Field parentField = getAccessibleField(reference);

            if (Collection.class.isAssignableFrom(mapper.getType()) || List.class.isAssignableFrom(mapper.getType())) {
                setCollectionField(parent, children, list, referencedBy, parentField,mapper);
            } else {
                setSingleField(parent, children, list, referencedBy, parentField,mapper);
            }
        } catch (Exception e) {
            throw new ReadException("Failed to read child field", e);
        }
    }

    private Field getReferencedByField(Children children) throws NoSuchFieldException {
        return children.mappedBy().getDeclaredField(children.referencedBy());
    }

    private Parent getReferenceAnnotation(Field referencedBy) throws ReadException {
        return Optional.ofNullable(referencedBy.getAnnotation(Parent.class))
                .orElseThrow(() -> new ReadException("Missing Parent annotation on the child class"));
    }

    private Field getAccessibleField(String reference) throws NoSuchFieldException {
        return entityClass.getDeclaredField(reference);
    }

    private void setCollectionField(T parent, Children children, Collection<?> list, Field referencedBy, Field parentField, Field mapper) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Collection<?> filteredList = filterList(list, referencedBy, parentField, parent,mapper);
        invokeSetter(parent, children, filteredList,mapper);
    }

    private void setSingleField(T parent, Children children, Collection<?> list, Field referencedBy, Field parentField, Field mapper) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Optional<?> child = filterList(list, referencedBy, parentField, parent, mapper).stream().findAny();
        if (child.isPresent()) {
            invokeSetter(parent, children, child.get(), mapper);
        }
    }

    private Collection<?> filterList(Collection<?> list, Field referencedBy, Field parentField, T parent, Field mapper) {
        Stream<?> abstractCollection = list.stream()
                .filter(child -> {
                    try {
                        return Objects.equals(parent.getClass().getDeclaredMethod("get".concat(StringUtils.capitalize(parentField.getName()))).invoke(parent), child.getClass().getDeclaredMethod("get".concat(StringUtils.capitalize(referencedBy.getName()))).invoke(child));
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        throw new ReadException("Failed to read child referencedBy", e);
                    }
                });
        if (Set.class.isAssignableFrom(mapper.getType())) {
            return abstractCollection.collect(Collectors.toCollection(HashSet::new));
        } else if (List.class.isAssignableFrom(mapper.getType())) {
            return abstractCollection.collect(Collectors.toCollection(ArrayList::new));
        } else {
            throw new IllegalArgumentException("Unsupported collection type: " + mapper.getType());
        }
    }

    private void invokeSetter(T parent, Children children, Object value, Field mapper) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        entityClass.getDeclaredMethod("set".concat(StringUtils.capitalize(children.name())), Class.forName(mapper.getType().getName()))
                .invoke(parent, value);
    }
}
