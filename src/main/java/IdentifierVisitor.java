import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;

public class IdentifierVisitor extends GenericVisitorAdapter<Void , TotalIdentifiersState> {

    @Override
    public Void visit(SimpleName n, TotalIdentifiersState state) {
        state.addIdentifierUse(n.getIdentifier());
        return super.visit(n, state);
    }
}
