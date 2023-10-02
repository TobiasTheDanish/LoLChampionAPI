package dat.sem3.parsing;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BinaryOperationsNode extends AstNode {
    private final List<AstNode> nodes;
    public BinaryOperationsNode() {
        super(AstNodeType.BINARY_OPERATIONS);
        nodes = new ArrayList<>();
    }

    public void addNode(AstNode node) {
        nodes.add(node);
    }

    @Override
    public String toString() {
        return "BinaryOperationsNode{" +
                "nodes:\n" + nodes +
                ", type=" + type +
                '}';
    }
}
