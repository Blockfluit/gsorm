package nl.nielsvanbruggen.gsorm.resolvers.chains;

import nl.nielsvanbruggen.gsorm.resolvers.TypeResolver;

import java.util.ArrayList;
import java.util.List;

public class SimpleResolverChain implements ResolverChain {
    private final List<TypeResolver<?>> typeResolvers;

    public SimpleResolverChain() {
        this.typeResolvers = new ArrayList<>();
    }

    public SimpleResolverChain(List<TypeResolver<?>> typeResolvers) {
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

    public void insertBefore() {

    }
}
