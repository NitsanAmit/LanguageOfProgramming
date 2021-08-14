import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.*;

public class JavaFileAnalyzer {

    public static void analyze(String path) {
        TotalIdentifiersState state = new TotalIdentifiersState();
        File projectsRoot = new File(path);
        File[] projects = projectsRoot.listFiles(File::isDirectory);
        if (projects == null) return;
        for (File project : projects) {
            ProjectIdentifiersState projectState = new ProjectIdentifiersState();
            state.setProjectState(projectState);
            compileProjectFiles(project, state);
            AnalyzerResultsWriter.writeAnalyzerResults(state.getProjectState(), project.getName());
        }
        AnalyzerResultsWriter.writeAnalyzerResults(state, "ALL");
    }

    private static void compileProjectFiles(File root, TotalIdentifiersState state) {
        File[] files = root.listFiles((f) -> f.getName().toLowerCase().endsWith(".java") || (f.isDirectory() && !f.getName().equals("test")));
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                compileProjectFiles(file, state);
            } else {
                compileFile(file, state);
            }
        }

    }

    private static void compileFile(File file, TotalIdentifiersState state) {
        CompilationUnit cu;
        try {
            cu = StaticJavaParser.parse(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (ParseProblemException e) {
            System.out.println("Bad format file: " + file.getAbsolutePath());
            e.printStackTrace();
            return;
        }
        IdentifierVisitor visitor = new IdentifierVisitor();
        visitor.visit(cu, state);
    }

}
