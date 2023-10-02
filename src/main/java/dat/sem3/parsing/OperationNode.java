package dat.sem3.parsing;


import lombok.Getter;

@Getter
public class OperationNode extends AstNode {
    private final String value;
    public OperationNode(String value) {
        super(AstNodeType.OPERATION);
        this.value = value;
    }

    @Override
    public String toString() {
        return "OperationNode: {\n" +
                "value='" + value + '\'' +
                ",\ntype=" + type +
                "\n}";
    }
}
