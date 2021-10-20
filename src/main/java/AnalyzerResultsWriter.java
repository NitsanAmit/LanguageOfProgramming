import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

public class AnalyzerResultsWriter {

    private static final String ANALYZER_V2_OUTPUT_CSV = "analyzerResults_v2.csv";
    private static final String ANALYZER_V2_CSV_ROW = "%s,%s,%s,%s,%d";

    public static void writeAnalyzerResults(IdentifiersLog identifiersLog) {
        try {
            FileWriter writer = new FileWriter(ANALYZER_V2_OUTPUT_CSV);
            writer.write("project,name,className,role,length\n");
            writer.write(identifiersLog.getIdentifiers()
                    .stream()
                    .map(log -> String.format(ANALYZER_V2_CSV_ROW,
                            escapeSpecialCharacters(log.getProject()),
                            escapeSpecialCharacters(log.getName()),
                            escapeSpecialCharacters(log.getClassName()),
                            escapeSpecialCharacters(log.getRole()),
                            log.getLength()
                    ))
                    .collect(Collectors.joining("\n")));
            writer.flush();
            writer.close();
            System.out.printf("Results written successfully to: %s.%n", ANALYZER_V2_OUTPUT_CSV);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Error printing state summary results: %s.%n", ANALYZER_V2_OUTPUT_CSV);
        }
    }

    private static String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
