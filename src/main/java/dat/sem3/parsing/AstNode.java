package dat.sem3.parsing;

import lombok.Getter;

@Getter
public abstract class AstNode {
    protected AstNodeType type;

    public AstNode(AstNodeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "\nAstNode {\n" +
                "type: " + type + "\n " +
                "}, ";
    }
}
