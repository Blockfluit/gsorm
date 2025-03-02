package nl.nielsvanbruggen.gsorm.resolvers;

public class FloatResolver implements TypeResolver<Float> {
    @Override
    public boolean supports(Class<?> type) {
        return float.class == type || Float.class == type;
    }

    @Override
    public Float resolve(String value) {
        return Float.parseFloat(value);
    }
}
