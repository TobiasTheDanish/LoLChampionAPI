package dat.sem3.parsing;

public abstract class Lexer {
    protected String input;
    protected char currentChar;
    protected int index;

    public Lexer(String input) {
        this.input = input;
        this.index = 0;
        this.currentChar = input.charAt(this.index);
    }

    public abstract Token nextToken();
}
