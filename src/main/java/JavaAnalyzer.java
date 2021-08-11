public class JavaAnalyzer {


    /**
     * args[0] = project root
     * args[1] = optional, java version, default is 8
     */
    public static void main(String[] args) {
        JavaFileAnalyzer.analyze(args[0]);
    }


}
