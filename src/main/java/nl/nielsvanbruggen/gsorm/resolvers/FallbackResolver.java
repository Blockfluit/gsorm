package nl.nielsvanbruggen.gsorm.resolvers;

public class FallbackResolver implements TypeResolver<Object> {
    @Override
    public boolean supports(Class<?> type) {
        return true;
    }

    @Override
    public Object resolve(String value) {
        return value;
    }
}
