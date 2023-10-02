package dat.sem3.parsing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NestedIdentifierNode extends AstNode {
    private AstNode lhs;
    private AstNode rhs;
    public NestedIdentifierNode() {
        super(AstNodeType.NESTED_IDENTIFIER);
    }

    @Override
    public String toString() {
        return "NestedIdentifierNode {\n" +
                "lhs: " + lhs +
                ",\nrhs: " + rhs +
                "\n}";
    }
}
