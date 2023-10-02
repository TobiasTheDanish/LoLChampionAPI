package dat.sem3.parsing;

import dat.sem3.parsing.exceptions.TokenException;
import lombok.SneakyThrows;

public class LexerImpl extends Lexer {
    public LexerImpl(String input) {
        super(input);
    }

    @SneakyThrows
    @Override
    public Token nextToken() {
        if (index >= input.length()-1) {
            return new TokenImpl("EOF", TokenImpl.EOF);
        }

        if (Character.isWhitespace(currentChar)) {
            skipWhitespace();
        }

        if (currentChar >= 'a' && currentChar <= 'z' || currentChar >= 'A' && currentChar <= 'Z') {
            return new TokenImpl( readIdent(), TokenImpl.IDENT);
        }

        return switch (currentChar) {
            case '!' -> {
                advance();
                if (currentChar == '=') yield getTokenAndAdvance(TokenImpl.NOT_EQUALS, "!=");
                else throw new TokenException("Unexpected character: " + currentChar);
            }
            case '*' -> {
                advance();
                if (currentChar == '=') yield getTokenAndAdvance(TokenImpl.CONTAINS, "*=");
                else throw new TokenException("Unexpected character: " + currentChar);
            }
            case '.' -> getTokenAndAdvance(TokenImpl.PERIOD, String.valueOf(currentChar));
            case '=' -> getTokenAndAdvance(TokenImpl.EQUALS, String.valueOf(currentChar));
            case ',' -> getTokenAndAdvance(TokenImpl.COMMA, String.valueOf(currentChar));

            default -> new TokenImpl("EOF", TokenImpl.EOF);
        };
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(currentChar)) {
            advance();
        }
    }

    private String readIdent() {
        StringBuilder sb = new StringBuilder();
        while (currentChar >= 'a' && currentChar <= 'z' || currentChar >= 'A' && currentChar <= 'Z') {
            sb.append(currentChar);
            advance();
        }
        return sb.toString();
    }

    private Token getTokenAndAdvance(int tokenType, String value) {
        Token t = new TokenImpl(value, tokenType);
        advance();
        return t;
    }

    private void advance() {
        if (index < input.length()-1) {
            currentChar = input.charAt(++index);
        } else {
            currentChar = (char) -1;
        }
    }
}
