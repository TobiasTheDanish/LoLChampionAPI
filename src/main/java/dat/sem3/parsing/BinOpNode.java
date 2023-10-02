package dat.sem3.parsing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinOpNode extends AstNode {
    private AstNode lhs;
    private AstNode op;
    private AstNode rhs;

    public BinOpNode() {
        super(AstNodeType.BIN_OP);
    }

    @Override
    public String toString() {
        return "BinOpNode {\n" +
                "lhs: " + lhs +
                ", \nop: " + op +
                ", \nrhs: " + rhs +
                "\n}";
    }
}
