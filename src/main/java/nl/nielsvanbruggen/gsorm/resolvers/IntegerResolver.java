package nl.nielsvanbruggen.gsorm.resolvers;

public class IntegerResolver implements TypeResolver<Integer> {
    @Override
    public boolean supports(Class<?> type) {
        return int.class == type || Integer.class == type;
    }

    @Override
    public Integer resolve(String value) {
        return Integer.parseInt(value);
    }
}
