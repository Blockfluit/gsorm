package nl.nielsvanbruggen.gsorm.resolvers;

public interface TypeResolver<T> {
    boolean supports(Class<?> type);
    T resolve(String value);
}
