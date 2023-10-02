package dat.sem3.parsing;

import lombok.Getter;

@Getter
public abstract class Token {
    protected String value;
    protected int tokenType;

    public Token(String value, int tokenType) {
        this.value = value;
        this.tokenType = tokenType;
    }

    public abstract String getName(int tokenType);
}
