public class TotalIdentifiersState extends ProjectIdentifiersState {

    private ProjectIdentifiersState projectState;

    public void addIdentifierUse(String identifier) {
        super.addIdentifierUse(identifier);
        projectState.addIdentifierUse(identifier);
    }

    public ProjectIdentifiersState getProjectState() {
        return projectState;
    }

    public void setProjectState(ProjectIdentifiersState projectState) {
        this.projectState = projectState;
    }
}
