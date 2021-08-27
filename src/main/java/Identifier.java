public class Identifier {

    private String project;
    private String name;
    private String className;
    private String role;
    private int length;

    public Identifier(String name, String className, String role) {
        this.name = name;
        this.className = className;
        this.role = role;
        this.length = name.length();
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public String getRole() {
        return role;
    }

    public int getLength() {
        return length;
    }
}
