import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.symbolsolver.resolution.naming.NameLogic;

import java.util.Optional;

public class IdentifiersVisitor extends GenericVisitorAdapter<Void , IdentifiersLog> {

    @Override
    public Void visit(SimpleName n, IdentifiersLog identifiersLog) {
        Optional<NodeWithSimpleName> ancestor = n.findAncestor(NodeWithSimpleName.class);
        if (ancestor.isPresent()) {
            NodeWithSimpleName parent = ancestor.get();
            String[] parentClassName = parent.getClass().getName().split("\\.");
            Identifier identifier = new Identifier(n.getIdentifier(), parentClassName[parentClassName.length - 1], NameLogic.classifyRole(parent.getName()).name());
            identifiersLog.log(identifier);
        }
        return super.visit(n, identifiersLog);
    }

}
