import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;

import java.io.*;

public class JavaFileAnalyzer {

    public static void analyze(String path) {
        IdentifiersLog identifiersLog = new IdentifiersLog();
        File projectsRoot = new File(path);
        File[] projects = projectsRoot.listFiles(File::isDirectory);
        if (projects == null) return;
        for (File project : projects) {
            identifiersLog.setCurrentProject(project.getName());
            compileProjectFiles(project, identifiersLog);
        }
        AnalyzerResultsWriter.writeAnalyzerResults(identifiersLog);
    }

    private static void compileProjectFiles(File root, IdentifiersLog state) {
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

    private static void compileFile(File file, IdentifiersLog state) {
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
        GenericVisitorAdapter<Void, IdentifiersLog> visitor = new IdentifiersVisitor();
        visitor.visit(cu, state);
    }

}
