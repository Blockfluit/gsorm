package nl.nielsvanbruggen.gsorm.resolvers;

public class DoubleResolver implements TypeResolver<Double> {
    @Override
    public boolean supports(Class<?> type) {
        return double.class == type || Double.class == type;
    }

    @Override
    public Double resolve(String value) {
        return Double.parseDouble(value);
    }
}
