package dat.sem3.parsing;

import dat.sem3.parsing.exceptions.ParseException;

public abstract class Parser {
    protected Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public abstract AstNode parse() throws ParseException;
    protected abstract void consume();
    protected abstract String match(int tokenType) throws ParseException;
}
