package nl.nielsvanbruggen.gsorm.deserializers;

import nl.nielsvanbruggen.gsorm.annotations.SheetColumn;
import nl.nielsvanbruggen.gsorm.resolvers.TypeResolver;
import nl.nielsvanbruggen.gsorm.resolvers.chains.ResolverChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class TableDeserializer implements Deserializer {
    private final static Logger logger = LoggerFactory.getLogger(TableDeserializer.class);

    private final ResolverChain resolverChain;

    public TableDeserializer(ResolverChain resolverChain) {
        this.resolverChain = resolverChain;
    }

    public <T> List<T> map(List<List<Object>> table, Class<T> clazz) {
        if(table.isEmpty()) return Collections.emptyList();

        List<String> headers = table.get(0).stream()
                .map(o -> (String) o)
                .collect(Collectors.toList());

        Map<String, Field> fieldMap = new HashMap<>();
        Set<String> trim = new HashSet<>();
        Set<String> caseInsensitive = new HashSet<>();

        for (Field field : clazz.getDeclaredFields()) {
            if(field.isAnnotationPresent(SheetColumn.class)) {
                SheetColumn annotation = field.getAnnotation(SheetColumn.class);
                String fieldName = annotation.value();
                fieldMap.put(fieldName, field);
                trim.add(fieldName);
                caseInsensitive.add(fieldName);
            }
            else {
                String fieldName = field.getName();
                fieldMap.put(fieldName, field);
            }

            // Make sure field is accessible
            field.setAccessible(true);
        }

        return table.stream()
                // Skip header.
                .skip(1)
                .map(values -> {
                    T row;

                    try {
                        row = clazz.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        logger.debug(e.getMessage());
                        return null;
                    }

                    for (int j = 0; j < headers.size() && j < values.size(); j++) {
                        String columnName = headers.get(j);
                        String value = (String) values.get(j);

                        if(caseInsensitive.contains(columnName.toLowerCase())) columnName = columnName.toLowerCase();
                        if(trim.contains(columnName)) columnName = columnName.trim();

                        Field field = fieldMap.get(columnName);
                        if (field != null) {
                            Object object = resolverChain.resolve(field.getType(), value);

                            try {
                                field.set(row, object);
                            } catch (IllegalAccessException e) {
                                logger.debug(e.getMessage());
                            }
                        }
                    }

                    return row;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
