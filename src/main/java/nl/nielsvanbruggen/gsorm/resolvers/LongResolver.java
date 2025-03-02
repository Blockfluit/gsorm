package nl.nielsvanbruggen.gsorm.resolvers;

public class LongResolver implements TypeResolver<Long> {
    @Override
    public boolean supports(Class<?> type) {
        return long.class == type || Long.class == type;
    }

    @Override
    public Long resolve(String value) {
        return Long.parseLong(value);
    }
}
