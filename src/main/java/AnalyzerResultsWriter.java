import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

public class AnalyzerResultsWriter {

    private static final String WORD_FREQUENCY_OUTPUT_CSV = "analyzerResults_frequency_%s.csv";
    private static final String LENGTH_FREQUENCY_OUTPUT_CSV = "analyzerResults_lengths_%s.csv";

    public static void writeAnalyzerResults(ProjectIdentifiersState state, String projectName){
        try {
            String wordFrequencyOutputPath = String.format(WORD_FREQUENCY_OUTPUT_CSV, projectName);
            String lengthsFrequencyOutputPath = String.format(LENGTH_FREQUENCY_OUTPUT_CSV, projectName);
            writeWordFrequenciesToFile(state, wordFrequencyOutputPath);
            writeLengthFrequenciesToFile(state, lengthsFrequencyOutputPath);
            System.out.printf("Results written successfully to: %s, %s.%n", wordFrequencyOutputPath, lengthsFrequencyOutputPath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Error printing state summary results: %s.%n", projectName);
        }
    }

    private static void writeWordFrequenciesToFile(
            ProjectIdentifiersState state,
            String wordFrequencyOutputPath
    ) throws IOException {
        FileWriter writer = new FileWriter(wordFrequencyOutputPath);
        writer.write("word,frequency\n");
        writer.write(state.getIdentifiersCount().entrySet()
                .stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .map(entry -> escapeSpecialCharacters(entry.getKey()) + "," + entry.getValue()).collect(Collectors.joining("\n")));
        writer.flush();
        writer.close();
    }

    private static void writeLengthFrequenciesToFile(
            ProjectIdentifiersState state,
            String lengthsFrequencyOutputPath
    ) throws IOException {
        FileWriter writer = new FileWriter(lengthsFrequencyOutputPath);
        writer.write("word_length,frequency\n");
        writer.write(state.getIdentifiersLengthCount().entrySet()
                .stream()
                .sorted((a, b) -> Integer.compare(b.getKey(), a.getKey()))
                .map(entry -> entry.getKey() + "," + entry.getValue()).collect(Collectors.joining("\n")));
        writer.flush();
        writer.close();
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
