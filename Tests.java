class Tests {
    static boolean close(double a, double b){
        return Math.abs(b-a)<0.001;
    }
    public static void main(String[] args) throws Exception {
        assert(close(new Interpreter("12").interpret(), 12.0));
        assert(close(new Interpreter("-12").interpret(), -12.0));
        assert(close(new Interpreter("12+10").interpret(), 22.0));
        assert(close(new Interpreter("(12+10)").interpret(), 22.0));
    }
}