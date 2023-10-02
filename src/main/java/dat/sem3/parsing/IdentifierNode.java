package dat.sem3.parsing;

import lombok.Getter;

@Getter
public class IdentifierNode extends AstNode {
    private final String value;
    public IdentifierNode(String value) {
        super(AstNodeType.IDENTIFIER);
        this.value = value;
    }

    @Override
    public String toString() {
        return "IdentifierNode: {\n" +
                "value='" + value + '\'' +
                ",\ntype=" + type +
                "\n}";
    }
}
