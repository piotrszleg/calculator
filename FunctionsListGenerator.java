import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileWriter;

class FunctionsListGenerator {
    static final String constants="PI E";
    static final String oneArgumentFunctions="sin cos tan asin acos atan sinh cosh tanh abs";
    static final String twoArgumentsFunctions="pow min max";
    static final String templateFile="FunctionsList.template";
    static final String outputFile="FunctionsList.java";
    
    private static String readEntireFile(String filePath) throws IOException{
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get(templateFile), StandardCharsets.UTF_8)){
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        return contentBuilder.toString();
    }

    private static String readTemplate() throws IOException{
        return readEntireFile(templateFile);
    }
    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileWriter file=new FileWriter(outputFile);
        StringBuilder generated=new StringBuilder();
        String indentation="        ";
        for(String constant : constants.split(" ")){
            generated.append(String.format(indentation+"constants.put(\"%s\", Math.%s);\n", constant, constant));
        }
        for(String function : oneArgumentFunctions.split(" ")){
            generated.append(String.format(indentation+"oneArgumentFunctions.put(\"%s\", Math::%s);\n", function, function));
        }
        for(String function : twoArgumentsFunctions.split(" ")){
            generated.append(String.format(indentation+"twoArgumentsFunctions.put(\"%s\", Math::%s);\n", function, function));
        }
        generated.append(String.format(indentation+"oneArgumentFunctions.put(\"ln\", Math::log);\n"));
        String logFunction="(Double a, Double b)->Math.log(a)/Math.log(b)";
        generated.append(String.format(indentation+"twoArgumentsFunctions.put(\"log\", %s);\n", logFunction));
        file.write(String.format(readTemplate(), generated.toString()));
        file.close();
    }
}