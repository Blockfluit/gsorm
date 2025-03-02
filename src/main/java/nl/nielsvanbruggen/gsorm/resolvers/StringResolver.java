package nl.nielsvanbruggen.gsorm.resolvers;

public class StringResolver implements TypeResolver<String> {
    @Override
    public boolean supports(Class<?> type) {
        return String.class == type;
    }

    @Override
    public String resolve(String value) {
        return value;
    }
}
