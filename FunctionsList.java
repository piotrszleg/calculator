import java.util.Map;
import java.util.HashMap;
import java.util.function.*;

public class FunctionsList {
    public Map<String, Double> constants;
    public Map<String, Function<Double, Double>> oneArgumentFunctions;
    public Map<String, BiFunction<Double, Double, Double>> twoArgumentsFunctions;
    FunctionsList(){
        constants=new HashMap<String, Double>();
        constants.put("PI", Math.PI);
        oneArgumentFunctions=new HashMap<String, Function<Double, Double>>();
        oneArgumentFunctions.put("sin", (Double a)->(Double)Math.sin(a));
        twoArgumentsFunctions=new HashMap<String, BiFunction<Double, Double, Double>>();
        twoArgumentsFunctions.put("sin", (Double a, Double b)->(Double)Math.pow(a, b));
    }
    static FunctionsList instance;
    public static FunctionsList getInstance(){
        if(instance==null){
            instance=new FunctionsList();
        }
        return instance;
    }
}