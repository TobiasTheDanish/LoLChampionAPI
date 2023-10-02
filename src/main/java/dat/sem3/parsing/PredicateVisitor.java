package dat.sem3.parsing;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * A class capable of visiting an abstract syntax tree (ast), and produce a list of Predicates for type param T.
 * Good for filtering a stream.
 * @param <T> the type of the input to the predicate.
 */
public class PredicateVisitor<T> implements AstVisitor<List<Predicate<T>>> {
    /**
     * Creates a list of Predicates for type T, when given an AstNode and Class of K
     * @param node the root node of ast.
     * @param cls the class of type K, used to reflect fields within.
     * @return a list of predicates for type T.
     * @param <K> Should be the same as type T
     */
    @Override
    public <K> List<Predicate<T>> visit(AstNode node, Class<K> cls) {
        try {
            return visitBinaryOperations(node, cls);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private <K> List<Predicate<T>> visitBinaryOperations(AstNode node, Class<K> cls) throws NoSuchFieldException {
        if (node instanceof BinaryOperationsNode bOpsNode) {
            List<Predicate<T>> list = new ArrayList<>();

            for (AstNode n : bOpsNode.getNodes()) {
                if (n.getType() == AstNodeType.BIN_OP) {
                    list.add(visitBinOp((BinOpNode) n, cls));
                } else {
                    throw new RuntimeException("Invalid ast node as root");
                }
            }

            return list;
        }
        throw new RuntimeException("Invalid ast node as root");
    }

    private <K> Predicate<T> visitBinOp(BinOpNode node, Class<K> cls) throws NoSuchFieldException {
        List<Field> fields = new ArrayList<>();
        switch (node.getLhs().getType()) {
            case NESTED_IDENTIFIER -> fields = visitNestedIdentifier((NestedIdentifierNode) node.getLhs(), cls, new ArrayList<>());
            case IDENTIFIER -> {
                IdentifierNode identifierNode = (IdentifierNode) node.getLhs();
                fields.add(cls.getDeclaredField(identifierNode.getValue()));
            }
            default -> throw new RuntimeException("Invalid ast node as root");
        }

        fields.forEach(field -> field.setAccessible(true));

        OperationType op = visitOperation((OperationNode) node.getOp());

        List<Field> finalFields = fields;
        return t -> {
            Object o = t;
            Field field = finalFields.get(finalFields.size()-1);
            for (int i = 0; i < finalFields.size()-1; i++) {
                Field f = finalFields.get(i);
                try {
                    o = f.get(o);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                switch (op) {
                    case EQUALS -> {
                        if (node.getRhs() instanceof IdentifierNode identifierNode) {
                            String value = (String) field.get(o);
                            return value.equals(identifierNode.getValue());
                        }
                        throw new RuntimeException("Invalid ast node type for right hand side of bin op");
                    }
                    case NOT_EQUALS -> {
                        if (node.getRhs() instanceof IdentifierNode identifierNode) {
                            String value = (String) field.get(o);
                            return !value.equals(identifierNode.getValue());
                        }
                        throw new RuntimeException("Invalid ast node type for right hand side of bin op");
                    }
                    case CONTAINS -> {
                        if (node.getRhs() instanceof IdentifierNode identifierNode) {
                            String value = (String) field.get(o);
                            return value.contains(identifierNode.getValue());
                        }
                        throw new RuntimeException("Invalid ast node type for right hand side of bin op");
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return false;
        };
    }

    private <K> List<Field> visitNestedIdentifier(NestedIdentifierNode node, Class<K> cls, List<Field> fields) throws NoSuchFieldException {
        if (node.getLhs() instanceof IdentifierNode lhs) {
            fields.add(cls.getDeclaredField(lhs.getValue()));
        } else {
            throw new RuntimeException("Illegal type of ast node, in nestedIdentifier lhs. Expected 'IdentifierNode', found '" + node.getLhs().getClass().getSimpleName() + "'.");
        }

        switch (node.getRhs().getType()) {
            case NESTED_IDENTIFIER -> {
                return visitNestedIdentifier((NestedIdentifierNode) node.getRhs(), fields.get(fields.size()-1).getType(), fields);
            }
            case IDENTIFIER -> {
                IdentifierNode n = (IdentifierNode) node.getRhs();
                fields.add(fields.get(fields.size()-1).getType().getDeclaredField(n.getValue()));
                return fields;
            }
            default -> throw new RuntimeException("Illegal type of ast node, in nestedIdentifier rhs. Expected 'NESTED_IDENTIFIER' or 'IDENTIFIER', found '" + node.getRhs().getType().name() + "'.");
        }
    }

    private OperationType visitOperation(OperationNode node) {
        return OperationType.get(node.getValue());
    }
}
