/**
 * Author: Mohamed Zarrouki
 */
package com.datashepherd.excel.helper.reader;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import com.datashepherd.excel.annotation.Parent;
import com.datashepherd.excel.exception.ReadException;
import com.datashepherd.excel.helper.Children;
import com.datashepherd.excel.service.Reader;

public class Processor<T> {

    private final Class<T> entityClass;
    private final ConcurrentLinkedQueue<Children> subs;
    private final Workbook workbook;

    public Processor(Class<T> entityClass, ConcurrentLinkedQueue<Children> subs, Workbook workbook) {
        this.entityClass = entityClass;
        this.subs = subs;
        this.workbook = workbook;
    }

    public void processChild(ConcurrentLinkedQueue<T> parents) {
        for (Children childEntry : subs) {
            ConcurrentLinkedQueue<?> allChildren = new Reader<>(workbook, childEntry.mappedBy()).read();
            try {
                Field referencedByField = childEntry.mappedBy().getDeclaredField(childEntry.referencedBy());
                referencedByField.setAccessible(true);
                Field mapperField = entityClass.getDeclaredField(childEntry.name());
                mapperField.setAccessible(true);
                String parentRefName = Optional.ofNullable(referencedByField.getAnnotation(Parent.class))
                                               .orElseThrow(() -> new ReadException("Missing Parent annotation on the child class"))
                                               .reference();
                Field parentRefField = entityClass.getDeclaredField(parentRefName);
                parentRefField.setAccessible(true);

                // Index children by their parent reference value
                Map<Object, List<Object>> childrenByParentId = allChildren.stream()
                                                                          .collect(Collectors.groupingBy(child -> {
                                                                              try {
                                                                                  return referencedByField.get(child);
                                                                              }
                                                                              catch (IllegalAccessException e) {
                                                                                  throw new ReadException("Failed to read child reference", e);
                                                                              }
                                                                          }));

                for (T parent : parents) {
                    Object parentId = parentRefField.get(parent);
                    List<Object> filteredChildren = childrenByParentId.getOrDefault(parentId, Collections.emptyList());

                    if (Collection.class.isAssignableFrom(mapperField.getType())) {
                        Collection<Object> targetCollection;
                        if (Set.class.isAssignableFrom(mapperField.getType())) {
                            targetCollection = new HashSet<>(filteredChildren);
                        }
                        else {
                            targetCollection = new ArrayList<>(filteredChildren);
                        }
                        invokeSetter(parent, childEntry, targetCollection, mapperField);
                    }
                    else if (!filteredChildren.isEmpty()) {
                        invokeSetter(parent, childEntry, filteredChildren.getFirst(), mapperField);
                    }
                }
            }
            catch (Exception e) {
                throw new ReadException("Failed to process child relationship: " + childEntry.name(), e);
            }
        }
    }

    private void invokeSetter(T parent, Children children, Object value, Field mapper) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        entityClass.getDeclaredMethod("set".concat(StringUtils.capitalize(children.name())), mapper.getType())
                .invoke(parent, value);
    }
}
