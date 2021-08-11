import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.*;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JavaFileAnalyzer {

    private static final String OUTPUT_FILE = "javaAnalyzerResults.txt";

    public static void analyze(String path) {
        IdentifierState state = new IdentifierState();
        File root = new File(path);
        compileProjectFiles(root, state);
        try {
            writeResultsToFile(state, OUTPUT_FILE);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Results written successfully to: " + OUTPUT_FILE);
    }

    private static void compileProjectFiles(File root, IdentifierState state) {
        File[] files = root.listFiles((f) -> {
            return f.getName().toLowerCase().endsWith(".java") || (f.isDirectory() && !f.getName().equals("test"));
        });
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                compileProjectFiles(file, state);
            }else {
                compileFile(file, state);
            }
        }

    }

    private static void compileFile(File file, IdentifierState state) {
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

    private static void writeResultsToFile(IdentifierState state, String outputFileName) throws IOException {
        FileWriter writer = new FileWriter(outputFileName);
        writer.write("Identifiers frequency:\n");
        writer.write(state.getIdentifiersCount().entrySet()
                .stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n")));
        writer.write("\n\n\n");
        writer.write("Identifiers lengths frequency:\n");
        writer.write(state.getIdentifiersLengthCount().entrySet()
                .stream()
                .sorted((a, b) -> Integer.compare(b.getKey(), a.getKey()))
                .map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n")));
        writer.flush();
        writer.close();
    }

}
