import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;

public class IdentifierVisitor extends GenericVisitorAdapter<Void ,IdentifierState> {

    @Override
    public Void visit(SimpleName n, IdentifierState state) {
        state.addIdentifierUse(n.getIdentifier());
        return super.visit(n, state);
    }
}
