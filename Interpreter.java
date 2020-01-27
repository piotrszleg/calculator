import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class ParsingException extends Exception {}
class EvaluationException extends Exception {}

// static interpret method should be used by external classes to interpret mathematical expressions
// internally Interpreter class instances are used to store interpretation state
class Interpreter {
    final List<Character> binaryOperators=Arrays.asList(new Character[]{'x', '/', '%', '-', '+'});
    String text;
    int pointer;

    static double interpret(String text) throws ParsingException, EvaluationException {
        return new Interpreter(text).parse();
    }
    private Interpreter(String text){
        this.text=text;
    }
    void checkBounds() throws ParsingException {
        if(pointer>=text.length()){
            throw new ParsingException();
        }
    }
    double parseNumber() throws ParsingException{
        boolean valueParsed=false;
        double value=0.0;
        checkBounds();
        String parsedText=""+text.charAt(pointer);
        pointer++;
        while(true){
            try {
                value=Double.valueOf(parsedText);
                valueParsed=true;
            } catch(NumberFormatException e) {
                pointer--;
                break;
            }
            if(pointer<text.length()){
                parsedText+=text.charAt(pointer);
                pointer++;
            } else {
                break;
            }
        }
        if(valueParsed){
            return value;
        } else {
            throw new ParsingException();
        }
    }
    boolean isOperator(char character){
        for(char operator : binaryOperators){
            if(operator==character){
                return true;
            }
        }
        return false;
    }
    char parseBinaryOperator() throws ParsingException{
        checkBounds();
        char operator=text.charAt(pointer);
        if(isOperator(operator)){
            pointer++;
            return operator;
        }
        throw new ParsingException();
    }
    String parseName() {
        String name="";
        for(; pointer<text.length(); pointer++){
            char character=text.charAt(pointer);
            if(Character.isLetter(character)){
                name+=character;
            } else {
                break;
            }
        }
        return name;
    }
    double parseArgument() throws ParsingException, EvaluationException {
        checkBounds();
        if(text.charAt(pointer)!='('){
            throw new ParsingException();
        }
        pointer++;
        int begin=pointer;
        for(; pointer<text.length(); pointer++){
            if(text.charAt(pointer)==')'){
                pointer++;
                return Interpreter.interpret(text.substring(begin, pointer-1));
            }
        }
        throw new ParsingException();
    }
    String collectUntil(char end) {
        int begin=pointer;
        for(; pointer<text.length(); pointer++){
            if(text.charAt(pointer)==end){
                pointer++;
                return text.substring(begin, pointer-1);
            }
        }
        return null;
    }
    double[] parseArguments() throws ParsingException, EvaluationException {
        String[] argumentStrings=new String[2];
        double[] arguments=new double[2];
        checkBounds();
        if(text.charAt(pointer)!='('){
            throw new ParsingException();
        }
        pointer++;
        argumentStrings[0]=collectUntil(',');
        if(argumentStrings[0]!=null){
            arguments[0]=Interpreter.interpret(argumentStrings[0]);
        } else{
            throw new ParsingException();
        }
        argumentStrings[1]=collectUntil(')');
        if(argumentStrings[1]!=null){
            arguments[1]=Interpreter.interpret(argumentStrings[1]);
        } else{
            throw new ParsingException();
        }
        return arguments;
    }
    double parseFunction() throws ParsingException, EvaluationException {
        String name=parseName();
        FunctionsList functionsList=FunctionsList.getInstance();
        if(functionsList.constants.containsKey(name)){
            return functionsList.constants.get(name);
        } else if(functionsList.twoArgumentsFunctions.containsKey(name)) {
            double[] arguments=parseArguments();
            return functionsList.twoArgumentsFunctions.get(name).apply(arguments[0], arguments[1]);
        } else if(functionsList.oneArgumentFunctions.containsKey(name)) {
            return functionsList.oneArgumentFunctions.get(name).apply(parseArgument());
        }
        throw new ParsingException();
    }
    double evaluateBinary(double left, double right, char operator) throws EvaluationException {
        switch(operator){
            case '+':
                return left+right;
            case '-':
                return left-right;
            case 'x':
                return left*right;
            case '/':
                return left/right;
            case '%':
                if(Math.floor(left)!=left || Math.floor(right)!=right){
                    throw new EvaluationException();    
                } else {
                    return (int)left%(int)right;
                }
            default:
                throw new EvaluationException();
        }
    }
    class Pair<L, R> {
        public L left;
        public R right;
        public Pair(L left, R right){
            this.left=left;
            this.right=right;
        }
    }
    Pair<Character, Integer> findHighestOperator() throws ParsingException {
        Pair<Character, Integer> highestOperator=null;
        Comparator<Character> operatorsComparator=
            (Character a, Character b)->binaryOperators.indexOf(a)-binaryOperators.indexOf(b);
        
        // last binary is used to skip over prefixes
        // for example in "2x-2" '-' will be skipped over
        boolean lastBinary=false;
        for(pointer=0; pointer<text.length(); pointer++){
            char character=text.charAt(pointer);
            if(isOperator(character)){
                if(!lastBinary){
                    if(highestOperator==null 
                    || operatorsComparator.compare(character, highestOperator.left)>0){
                        highestOperator=new Pair<Character, Integer>(character, pointer);
                    }
                    lastBinary=true;
                }
            } else {
                // skip over expressions in parentheses
                if(character=='('){
                    collectUntil(')');
                }
                lastBinary=false;
            }
        }
        if(highestOperator==null){
            throw new ParsingException();
        }
        return highestOperator;
    }
    double parseBinary(double left) throws ParsingException, EvaluationException {
        Pair<Character,Integer> highestOperator=findHighestOperator();
        String toTheLeft=text.substring(0, highestOperator.right);
        String toTheRight=text.substring(highestOperator.right+1, text.length());
        return evaluateBinary(Interpreter.interpret(toTheLeft),
                              Interpreter.interpret(toTheRight), 
                              highestOperator.left);
    }
    double parseIfBinary(double left) throws ParsingException, EvaluationException {
        if(pointer<text.length()){
            return parseBinary(left);
        } else {
            return left;
        }
    }
    double parse() throws ParsingException, EvaluationException {
        if(pointer>=text.length()){
            throw new ParsingException();    
        }
        if(Character.isDigit(text.charAt(pointer))){
            return parseIfBinary(parseNumber());
        } else if(text.charAt(pointer)=='-'){
            pointer++;
            return parseIfBinary(-parseNumber());
        } else if(text.charAt(pointer)=='√'){
            pointer++;
            return parseIfBinary(Math.sqrt(parseNumber()));
        } else if(text.charAt(pointer)=='('){
            int begin=pointer;
            for(; pointer<text.length(); pointer++){
                if(text.charAt(pointer)==')'){
                    pointer++;
                    return parseIfBinary(Interpreter.interpret(text.substring(begin+1, pointer-1)));
                }
            }
        } else if(Character.isLetter(text.charAt(pointer))){
            return parseIfBinary(parseFunction());
        }
        throw new ParsingException();
    }
}