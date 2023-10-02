package dat.sem3.parsing;

public interface AstVisitor<T> {
   <K> T visit(AstNode node, Class<K> cls);
}
