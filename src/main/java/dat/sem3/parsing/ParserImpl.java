package dat.sem3.parsing;

import dat.sem3.parsing.exceptions.ParseException;

public class ParserImpl extends Parser {
    private int tokenIndex;
    private final Token[] tokens;
    private final int tokenCount;
    public ParserImpl(Lexer lexer, int tokenCount) {
        super(lexer);
        this.tokenCount = tokenCount;
        this.tokens = new Token[tokenCount];
        this.tokenIndex = 0;

        for (int i = 0; i < tokenCount; i++) {
            tokens[i] = lexer.nextToken();
        }
//        System.out.println(Arrays.toString(tokens));
    }

    @Override
    public AstNode parse() throws ParseException {
        return binaryOperations();
    }

    @Override
    protected void consume() {
        tokens[tokenIndex] = lexer.nextToken();
        tokenIndex = ++tokenIndex % tokenCount;
//        System.out.println(Arrays.toString(tokens));
//        System.out.println("Index: " + tokenIndex);
    }

    @Override
    protected String match(int tokenType) throws ParseException {
        Token token = tokens[tokenIndex];
//        System.out.println("Matching: " + token.getName(token.tokenType));
        if (token.tokenType == tokenType) {
            String res = token.value;
            consume();
            return res;
        } else {
            throw new ParseException("Unexpected tokentype. Found: " + token.getName(token.tokenType) + ", expected: " + token.getName(tokenType));
        }
    }

    /* binaryOperations: binOp (COMMA binOp)* ; */
    private AstNode binaryOperations() throws ParseException {
        BinaryOperationsNode node = new BinaryOperationsNode();
        node.addNode(binOp());
        while (tokens[tokenIndex].tokenType == TokenImpl.COMMA) {
            match(TokenImpl.COMMA);
            node.addNode(binOp());
        }
        return node;
    }

    /* binOp: (IDENTIFIER | nestedIdentifier) op IDENTIFIER ; */
    private AstNode binOp() throws ParseException {
        BinOpNode node = new BinOpNode();
        if (tokens[tokenIndex].tokenType == TokenImpl.IDENT && tokens[tokenIndex+1].tokenType == TokenImpl.PERIOD) {
            node.setLhs(nestedIdentifier());
        } else if (tokens[tokenIndex].tokenType == TokenImpl.IDENT) {
            node.setLhs(new IdentifierNode(match(TokenImpl.IDENT)));
        } else {
            throw new ParseException("Unexpected identifier token in start of binOp. ");
        }
        node.setOp(op());
        node.setRhs(new IdentifierNode(match(TokenImpl.IDENT)));

        return node;
    }
    /* nestedIdentifier: IDENTIFIER PERIOD (nestedIdentifier | IDENTIFIER) ; */
    private AstNode nestedIdentifier() throws ParseException {
        NestedIdentifierNode node = new NestedIdentifierNode();
        node.setLhs(new IdentifierNode(match(TokenImpl.IDENT)));
        match(TokenImpl.PERIOD);
        if (tokens[tokenIndex].tokenType == TokenImpl.IDENT && tokens[tokenIndex+1].tokenType == TokenImpl.PERIOD) {
            node.setRhs(nestedIdentifier());
        } else if (tokens[tokenIndex].tokenType == TokenImpl.IDENT) {
            node.setRhs(new IdentifierNode(match(TokenImpl.IDENT)));
        } else {
            throw new ParseException("Unexpected identifier token in start of binOp. ");
        }

        return node;
    }
    /* op: EQUALS | CONTAINS | NOT_EQUALS ; */
    private AstNode op() throws ParseException {
        Token token = tokens[tokenIndex];
        if (token.tokenType == TokenImpl.EQUALS) {
            return new OperationNode(match(TokenImpl.EQUALS));
        } else if (token.tokenType == TokenImpl.CONTAINS) {
            return new OperationNode(match(TokenImpl.CONTAINS));
        } else if (token.tokenType == TokenImpl.NOT_EQUALS) {
            return new OperationNode(match(TokenImpl.NOT_EQUALS));
        } else {
            throw new ParseException("Unexpected identifier token in op. Found: " + token.getName(token.tokenType));
        }
    }
}
