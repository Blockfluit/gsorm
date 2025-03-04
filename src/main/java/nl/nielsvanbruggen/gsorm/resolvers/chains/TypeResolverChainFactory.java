package nl.nielsvanbruggen.gsorm.resolvers.chains;

import nl.nielsvanbruggen.gsorm.resolvers.*;

import java.util.Arrays;
import java.util.List;

public class TypeResolverChainFactory {

    private TypeResolverChainFactory() {}

    public static BasicTypeResolverChain getBasicTypeResolverChain() {
        List<TypeResolver<?>> resolvers = Arrays.asList(
                new StringResolver(),
                new IntegerResolver(),
                new LongResolver(),
                new FloatResolver(),
                new DoubleResolver(),
                new BooleanResolver(),
                new FallbackResolver()
        );

        return new BasicTypeResolverChain(resolvers);
    }
}
