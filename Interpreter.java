import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class InterpreterException extends Exception {}
class ParsingException extends InterpreterException {}
class EvaluationException extends InterpreterException {}
class UnfinishedException extends InterpreterException {}

// static interpret method should be used by external classes to interpret mathematical expressions
// internally Interpreter class instances are used to store interpretation state
class Interpreter {
    static final List<Character> binaryOperators=Arrays.asList(new Character[]{'×', '/', '%', '-', '+'});
    String text;
    int pointer;

    static double interpret(String text) throws InterpreterException {
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
    double parseNumber() throws UnfinishedException, ParsingException {
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
            throw new UnfinishedException();
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
    void consume(char character) throws ParsingException {
        checkBounds();
        if(text.charAt(pointer)!='('){
            throw new ParsingException();
        }
        pointer++;
    }
    String collectUntil(char end) {
        int begin=pointer;
        for(; pointer<text.length(); pointer++){
            if(text.charAt(pointer)==end){
                pointer++;// "consume" the ending character
                return text.substring(begin, pointer-1);
            }
        }
        return text.substring(begin);
    }
    double parseArgument() throws InterpreterException {
        consume('(');
        return interpretOrZero(collectUntil(')'));
    }
    double[] parseArguments() throws InterpreterException {
        consume('(');
        double[] arguments=new double[2];
        arguments[0]=interpretOrZero(collectUntil(','));
        arguments[1]=interpretOrZero(collectUntil(')'));
        return arguments;
    }
    double parseFunction() throws InterpreterException {
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
    double identityValue(char operator){
        switch(operator){
            case '+':
            case '-':
                return 0;
            case '×':
            case '/':
            case '%':
                return 1;
            default:
                return 0;
        }
    }
    double evaluateBinary(double left, double right, char operator) throws EvaluationException {
        switch(operator){
            case '+':
                return left+right;
            case '-':
                return left-right;
            case '×':
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
    Pair<Character, Integer> findHighestOperator() throws ParsingException {
        Pair<Character, Integer> highestOperator=null;
        Comparator<Character> operatorsComparator=
            (Character a, Character b)->binaryOperators.indexOf(a)-binaryOperators.indexOf(b);
        
        // lastWasOperator is used to skip over prefixes
        // for example in "2×-2" '-' will be skipped over
        boolean lastWasOperator=false;
        for(pointer=0; pointer<text.length(); pointer++){
            char character=text.charAt(pointer);
            if(isOperator(character)){
                if(!lastWasOperator){
                    if(highestOperator==null 
                    || operatorsComparator.compare(character, highestOperator.getLeft())>0){
                        highestOperator=new Pair<Character, Integer>(character, pointer);
                    }
                    lastWasOperator=true;
                }
            } else {
                // skip over expressions in parentheses
                if(character=='('){
                    collectUntil(')');
                    pointer--;
                }
                lastWasOperator=false;
            }
        }
        if(highestOperator==null){
            throw new ParsingException();
        }
        return highestOperator;
    }
    double interpretOrIdentityValue(String text, char operator){
        try {
            return Interpreter.interpret(text);
        } catch(InterpreterException e){
            return identityValue(operator);
        }
    }
    double interpretOrZero(String text){
        try {
            return Interpreter.interpret(text);
        } catch(InterpreterException e){
            return 0;
        }
    }
    double parseBinary(double left) throws InterpreterException {
        Pair<Character,Integer> highestOperator=findHighestOperator();
        String toTheLeft=text.substring(0, highestOperator.getRight());
        String toTheRight=text.substring(highestOperator.getRight()+1, text.length());
        return evaluateBinary(interpretOrIdentityValue(toTheLeft, highestOperator.getLeft()),
                              interpretOrIdentityValue(toTheRight, highestOperator.getLeft()),
                              highestOperator.getLeft());
    }
    double parseIfBinary(double left) throws InterpreterException {
        if(pointer<text.length()){
            return parseBinary(left);
        } else {
            return left;
        }
    }
    double parse() throws InterpreterException {
        if(pointer>=text.length()){
            throw new UnfinishedException();  
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
            pointer++;
            return parseIfBinary(Interpreter.interpret(collectUntil(')')));
        } else if(Character.isLetter(text.charAt(pointer))){
            return parseIfBinary(parseFunction());
        }
        throw new UnfinishedException();
    }
}