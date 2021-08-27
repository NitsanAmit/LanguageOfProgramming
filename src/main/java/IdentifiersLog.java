import java.util.ArrayList;
import java.util.List;

public class IdentifiersLog {

    private List<Identifier> identifiers = new ArrayList<>();
    private String currentProject;

    public void log(Identifier identifier) {
        identifier.setProject(this.currentProject);
        identifiers.add(identifier);
    }

    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    public void setCurrentProject(String project) {
        this.currentProject = project;
    }
}
