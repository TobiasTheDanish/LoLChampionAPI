package dat.sem3.parsing;

import java.util.Arrays;

public enum OperationType {
    EQUALS("="),
    NOT_EQUALS("!="),
    CONTAINS("*=")
    ;

    final String op;
    OperationType(String op) {
        this.op = op;
    }

    public static OperationType get(String op) {
        return Arrays.stream(OperationType.values())
                .filter(operation -> operation.op.equals(op))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid operation. Valid operations are: '=', '!='."));
    }
}
