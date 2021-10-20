package path.core.query.syntax;

import java.util.Date;

public enum OperatorType {
    EXACT(String.class),
    STARTS_WITH(String.class, Integer.class),
    ENDS_WITH(String.class, Integer.class),
    CONTAINS(String.class, Integer.class),
    EQUALS(Integer.class),
    LESS_THAN(Integer.class),
    GREATER_THAN(Integer.class),
    IS_ON_POSITION(Integer.class),
    BEFORE(Date.class),
    AFTER(Date.class),
    BETWEEN(Date.class),
    MATCHES_REGEX(String.class);

    private Class[] supportedTypes;

    OperatorType(Class... supportedTypes) {
        this.supportedTypes = supportedTypes;
    }

    public Class[] getSupportedTypes() {
        return supportedTypes;
    }

    public boolean supportsType(Class type) {
        for (Class supportedType : supportedTypes) {
            if(supportedType.isAssignableFrom(type)){
                return true;
            }
        }
        return false;
    }
}
