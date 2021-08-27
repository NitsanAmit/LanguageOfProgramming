import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.symbolsolver.resolution.naming.NameLogic;

import java.util.Optional;

public class IdentifiersVisitorV2 extends GenericVisitorAdapter<Void , IdentifiersLog> {


    @Override
    public Void visit(SimpleName n, IdentifiersLog arg) {
        Optional<NodeWithSimpleName> ancestor = n.findAncestor(NodeWithSimpleName.class);
        if (ancestor.isPresent()) {
            NodeWithSimpleName parent = ancestor.get();
            String[] parentClassName = parent.getClass().getName().split("\\.");
            arg.log(new Identifier(n.getIdentifier(), parentClassName[parentClassName.length-1], NameLogic.classifyRole(parent.getName()).name()));
        } else {
        }
        return super.visit(n, arg);
    }


}
