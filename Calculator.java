import javax.swing.*;

class Calculator extends JFrame implements InputReceiver {
    Display display;
    Keyboard keyboard;
    FunctionsPanel functionsPanel;
    boolean panelsSwitch=true;
    String input="";

    String interpretInput() throws InterpreterException{
        return String.format("%.2f", Interpreter.interpret(input));
    }

    void updateDisplay(){
        display.setInput(input);
        try {
            display.setPreview(interpretInput());
        } catch(InterpreterException e){
            display.setPreview("");
        }
    }

    void backspace(){
        if(input.length()>0){
            int i=input.length()-1;
            if(Character.isLetter(input.charAt(i))){
                while(i>=0 && Character.isLetter(input.charAt(i))){
                    i--;
                }
                input=input.substring(0, i+1);
            } else {
                input=input.substring(0, i);
            }
        }
    }

    void equalsSign(){
        try {
            input=interpretInput();
        } catch(InterpreterException e) {
            input="Error";
        }
    }

    void switchButtonPanels(){
        if(panelsSwitch){
            add(keyboard);
            remove(functionsPanel);
        } else {
            add(functionsPanel);
            remove(keyboard);
        }
        panelsSwitch=!panelsSwitch;
        doLayout();
        revalidate();
        repaint();
    }
    
    public void receiveInput(String newInput){
        switch(newInput){
            case "CE":
                input="";
                break;
            case "<":
                backspace();
                break;
            case "=":
                equalsSign();
                break;
            case "f(x)":
                switchButtonPanels();
                break;
            default:
                input+=newInput;
        }
        if(FunctionsList.instance.isFunction(newInput)){
            input+='(';
        }
        updateDisplay();
    }

    public Calculator() {
        super("Calculator");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        display=new Display();
        add(display);
        add(new Controls(this));
        keyboard=new Keyboard(this);
        functionsPanel=new FunctionsPanel(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 500);
        setVisible(true);
        switchButtonPanels();
    }
}