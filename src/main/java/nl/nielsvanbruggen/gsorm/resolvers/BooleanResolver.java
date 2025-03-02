package nl.nielsvanbruggen.gsorm.resolvers;

public class BooleanResolver implements TypeResolver<Boolean> {
    @Override
    public boolean supports(Class<?> type) {
        return boolean.class == type || Boolean.class == type;
    }

    @Override
    public Boolean resolve(String value) {
        return Boolean.getBoolean(value);
    }
}
