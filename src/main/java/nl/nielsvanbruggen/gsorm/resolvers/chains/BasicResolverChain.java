package nl.nielsvanbruggen.gsorm.resolvers.chains;

import nl.nielsvanbruggen.gsorm.resolvers.TypeResolver;

import java.util.ArrayList;
import java.util.List;

public class BasicResolverChain implements ResolverChain {
    private final List<TypeResolver<?>> typeResolvers;

    public BasicResolverChain() {
        this.typeResolvers = new ArrayList<>();
    }

    public BasicResolverChain(List<TypeResolver<?>> typeResolvers) {
        this.typeResolvers = new ArrayList<>(typeResolvers);
    }

    @Override
    public <T> Object resolve(Class<T> clazz, String value) {
        for (TypeResolver<?> resolver : typeResolvers) {
            if(resolver.supports(clazz)) {
                return resolver.resolve(value);
            }
        }

        throw new IllegalStateException("No compatible TypeResolver found!");
    }

    @Override
    public void insertBefore(TypeResolver<?> typeResolver, Class<?> clazz) {

    }

    @Override
    public void insertAfter(TypeResolver<?> typeResolver, Class<?> clazz) {

    }

    @Override
    public void add(TypeResolver<?> typeResolver) {

    }
}
