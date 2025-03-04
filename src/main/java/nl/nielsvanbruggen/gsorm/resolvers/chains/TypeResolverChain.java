package nl.nielsvanbruggen.gsorm.resolvers.chains;

import nl.nielsvanbruggen.gsorm.resolvers.TypeResolver;

public interface TypeResolverChain {
    <T> Object resolve(Class<T> clazz, String value);
    void insertBefore(TypeResolver<?> typeResolver, Class<?> clazz);
    void insertAfter(TypeResolver<?> typeResolver, Class<?> clazz);
    void add(TypeResolver<?> typeResolver);
}
