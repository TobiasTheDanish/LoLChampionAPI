package dat.sem3.parsing;

public class TokenImpl extends Token {
    public static final int EOF = 0;
    public static final int IDENT = 1;
    public static final int PERIOD = 2;
    public static final int COMMA = 3;
    public static final int EQUALS = 4;
    public static final int CONTAINS = 5;
    public static final int NOT_EQUALS = 6;

    protected final String[] tokenNames = new String[] { "<EOF>", "Identifier", "Period", "Comma", "Equals", "Contains", "Not equals" };

    public TokenImpl(String value, int tokenType) {
        super(value, tokenType);
    }

    @Override
    public String getName(int tokenType) {
        return tokenNames[tokenType];
    }

    @Override
    public String toString() {
        return "Token(" +
                " <" + getName(tokenType) + ">, " +
                "'" + value + "' " +
                ')';
    }
}
