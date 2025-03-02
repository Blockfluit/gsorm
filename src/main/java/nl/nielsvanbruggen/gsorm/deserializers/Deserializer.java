package nl.nielsvanbruggen.gsorm.deserializers;

import java.util.List;

public interface Deserializer {
    <T> List<T> map(List<List<Object>> table, Class<T> clazz);
}
