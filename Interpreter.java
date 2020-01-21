class ParsingException extends Exception {}
class EvaluationException extends Exception {}
class IncompletedException extends Exception {
    String completed;
    double completedValue;
    public IncompletedException(String completed, double completedValue){
        this.completed=completed;
        this.completedValue=completedValue;
    }
}

class Interpreter {
    String text;
    int pointer;

    Interpreter(String text){
        this.text=text;
    }
    
    double parseNumber() throws ParsingException{
        boolean valueParsed=false;
        double value=0.0;
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
    char parseBinaryOperator() throws ParsingException{
        char[] binaryOperators={'-', '+', 'x', '/', '%'};
        char character=text.charAt(pointer);
        for(char operator : binaryOperators){
            if(operator==character){
                pointer++;
                return operator;
            }
        }
        throw new ParsingException();
    }
    String parseName() {
        String name="";
        for(; pointer<text.length(); pointer++){
            char character=text.charAt(pointer);
            if(Character.isAlphabetic(character)){
                name+=character;
            } else {
                break;
            }
        }
        return name;
    }
    double parseArgument() throws ParsingException, EvaluationException {
        if(text.charAt(pointer)!='('){
            throw new ParsingException();
        }
        pointer++;
        int begin=pointer;
        for(; pointer<text.length(); pointer++){
            if(text.charAt(pointer)==')'){
                return new Interpreter(text.substring(begin+1, pointer)).interpret();
            }
        }
        throw new ParsingException();
    }
    String collectUntil(char end) {
        int begin=pointer;
        for(; pointer<text.length(); pointer++){
            if(text.charAt(pointer)==end){
                return text.substring(begin+1, pointer);
            }
        }
        return null;
    }
    double[] parseArguments() throws ParsingException, EvaluationException {
        String[] argumentStrings=new String[2];
        double[] arguments=new double[2];
        if(text.charAt(pointer)!='('){
            throw new ParsingException();
        }
        pointer++;
        argumentStrings[0]=collectUntil(',');
        if(argumentStrings[0]!=null){
            arguments[0]=new Interpreter(argumentStrings[0]).interpret();
        } else{
            throw new ParsingException();
        }
        argumentStrings[1]=collectUntil(',');
        if(argumentStrings[1]!=null){
            arguments[1]=new Interpreter(argumentStrings[1]).interpret();
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
        } else if(functionsList.oneArgumentFunctions.containsKey(name)) {
            double[] arguments=parseArguments();
            return functionsList.twoArgumentsFunctions.get(name).apply(arguments[0], arguments[1]);
        } else if(functionsList.twoArgumentsFunctions.containsKey(name)) {
            return functionsList.oneArgumentFunctions.get(name).apply(parseArgument());
        }
        throw new ParsingException();
    }
    double parseBinary(double left) throws ParsingException, EvaluationException {
        char operator=parseBinaryOperator();
        double right=interpret();
        switch(operator){
            case '+':
                return left+right;
            case '-':
                return left+right;
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
    double parseIfBinary(double left) throws ParsingException, EvaluationException {
        if(pointer<text.length()){
            return parseBinary(left);
        } else {
            return left;
        }
    }
    double interpret() throws ParsingException, EvaluationException {
        if(pointer>=text.length()){
            throw new ParsingException();    
        }
        if(Character.isDigit(text.charAt(pointer))){
            return parseIfBinary(parseNumber());
        } else if(text.charAt(pointer)=='-'){
            pointer++;
            return parseIfBinary(-parseNumber());
        } else if(text.charAt(pointer)=='('){
            int begin=pointer;
            for(; pointer<text.length(); pointer++){
                if(text.charAt(pointer)==')'){
                    pointer++;
                    return parseIfBinary(new Interpreter(text.substring(begin+1, pointer-1)).interpret());
                }
            }
        }
        throw new ParsingException();
    }
}