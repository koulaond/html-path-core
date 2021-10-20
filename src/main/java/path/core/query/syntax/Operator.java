package path.core.query.syntax;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

import static java.lang.String.format;

@EqualsAndHashCode
@Getter
public class Operator<T> {

    private final OperatorType operatorType;

    private final T value;

    @EqualsAndHashCode.Exclude
    private final Function<Object, Boolean> function;

    private Operator(OperatorType operatorType, T value, Function<Object, Boolean> function) {
        if (!operatorType.supportsType(value.getClass())) {
            throw new IllegalStateException(format("Operator type %s does not support class type %s",
                    operatorType.name(), value.getClass().getName()));
        }
        this.operatorType = operatorType;
        this.value = value;
        this.function = function;
    }

    public boolean operate(Object input) {
        return function.apply(input);
    }

    public T getValue() {
        return value;
    }

    public static Operator<String> exact(String value) {
        return new Operator<>(OperatorType.EXACT, value, o -> createStringFunction(o, s -> s.equals(value)));
    }

    public static Operator<String> any() {
        return new Operator<>(OperatorType.MATCHES_REGEX, ".",  o -> createStringFunction(o, s -> s.matches(".")));
    }

    public static Operator<String> startsWith(String value) {
        return new Operator<>(OperatorType.STARTS_WITH, value,  o -> createStringFunction(o, s -> s.startsWith(value)));
    }

    public static Operator<String> endsWith(String value) {
        return new Operator<>(OperatorType.ENDS_WITH, value,  o -> createStringFunction(o, s -> s.endsWith(value)));
    }

    public static Operator<String> contains(String value) {
        return new Operator<>(OperatorType.CONTAINS, value, o -> createStringFunction(o, s -> s.contains(value)));
    }

    public static Operator<Integer> isEqualTo(int value) {
        return new Operator<>(OperatorType.EQUALS, value, o -> createIntFunction(o, integer -> integer == value));
    }


    public static Operator<Integer> lessThan(int value) {
        return new Operator<>(OperatorType.LESS_THAN, value, o -> createIntFunction(o, integer -> integer < value));
    }

    public static Operator<Integer> greaterThan(int value) {
        return new Operator<>(OperatorType.GREATER_THAN, value, o -> createIntFunction(o, integer -> integer > value));
    }

    public static Operator<Date> before(Date value, String dateFormat) {
        return new Operator<>(OperatorType.GREATER_THAN, value,
                o -> createDateFunction(o, date -> date.before(value), dateFormat));
    }

    public static Operator<Date> after(Date value, String dateFormat) {
        return new Operator<>(OperatorType.AFTER, value,
                o -> createDateFunction(o, date -> date.after(value), dateFormat));
    }

    public static Operator<Pair<?>> between(Date from, Date to, String dateFormat) {
        return new Operator<>(OperatorType.GREATER_THAN, new Pair<>(from, to),
                o -> createDateFunction(o, date -> date.after(from) && date.before(to), dateFormat));
    }

    private static Boolean createIntFunction(Object o, Function<Integer, Boolean> function) {
        if (o instanceof Integer) {
            return function.apply((Integer) o);
        } else if (o instanceof String) {
            try {
                Integer parsedValue = Integer.parseInt((String) o);
                return function.apply(parsedValue);
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        return false;
    }

    private static Boolean createDateFunction(Object o, Function<Date, Boolean> function, String dateFormat) {
        if (o instanceof Date) {
            return function.apply((Date) o);
        } else if (o instanceof String) {
            try {
                Date parsedValue = new SimpleDateFormat(dateFormat).parse((String) o);
                return function.apply(parsedValue);
            } catch (ParseException ex) {
                return false;
            }
        }
        return false;
    }

    private static Boolean createStringFunction(Object o, Function<String, Boolean> function) {
        return o instanceof String && function.apply((String) o);
    }
}
