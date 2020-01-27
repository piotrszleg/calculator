class Tests {
    static boolean close(double a, double b){
        return Math.abs(b-a)<0.001;
    }
    static void assertInterpretation(String text, double expectedResult) throws Exception {
        double result=Interpreter.interpret(text);
        assert close(result, expectedResult) 
             : String.format("\"%s\" evaluates to: %.2f, expected %.2f.", text, result, expectedResult);
    }
    public static void main(String[] args) throws Exception {
        assertInterpretation("12", 12.0);
        assertInterpretation("-12", -12.0);
        assertInterpretation("12+10", 22.0);
        assertInterpretation("(12+10)", 22.0);
        assertInterpretation("2x(12+10)", 44.0);
        assertInterpretation("PI", Math.PI);
        assertInterpretation("sin(PI)", Math.sin(Math.PI));
        assertInterpretation("pow(2,3)", Math.pow(2, 3));
        assertInterpretation("5+2x2", 9.0);
        assertInterpretation("5+2x-2", 1.0);
        assertInterpretation("4x3-5+2x2", 11.0);
    }
}