package nl.nielsvanbruggen.gsorm.resolvers.chains;

public interface ResolverChain {
    <T> Object resolve(Class<T> clazz, String value);
}
