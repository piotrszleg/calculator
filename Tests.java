class Tests {
    static boolean close(double a, double b){
        return Math.abs(b-a)<0.001;
    }
    static void assertInterpretation(String text, double expectedResult) 
    throws InterpreterException, AssertionError {
        double result=Interpreter.interpret(text);
        assert close(result, expectedResult) 
             : String.format("\"%s\" evaluates to: %.2f, expected %.2f.", text, result, expectedResult);
    }
    public static void main(String[] args) throws Exception {
        assertInterpretation("12.5", 12.5);
        // prefixes
        assertInterpretation("-12", -12.0);
        // binary operators
        assertInterpretation("12+10", 22.0);
        // parentheses
        assertInterpretation("(12+10)", 22.0);
        assertInterpretation("2×(12+10)", 44.0);
        // constants and functions
        assertInterpretation("PI", Math.PI);
        assertInterpretation("sin(PI)", Math.sin(Math.PI));
        assertInterpretation("pow(2,3)", Math.pow(2, 3));
        assertInterpretation("ln(1)×7", Math.log(1)*7);
        // order of operations
        assertInterpretation("5+2×2", 9.0);
        assertInterpretation("5+2×-2", 1.0);
        assertInterpretation("4×3-5+2×2", 11.0);
        // incomplete expressions
        assertInterpretation("5+", 5.0);
        assertInterpretation("5+2+", 7.0);
        assertInterpretation("sin(", Math.sin(0));
        assertInterpretation("2/ln(5+1+", 2.0/Math.log(5+1));
    }
}