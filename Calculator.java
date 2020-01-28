import javax.swing.*;
import java.awt.event.*;

class Calculator extends JFrame implements InputReceiver, KeyListener {
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
        requestFocusInWindow();
    }
    public void keyTyped(KeyEvent e) {
        char character=e.getKeyChar();
        switch(character) {
            case '*':
                input+='×';
                break;
            case '/':
                input+='÷';
                break;
            case '+':
            case '-':
            case ',':
            case '(':
            case ')':
            case '.':
                input+=character;
                break;
            case '=':
                equalsSign();
            default:
                if(Character.isDigit(character) || Character.isLetter(character)){
                    input+=character;
                }
        }
        updateDisplay();
    }
    public void keyPressed(KeyEvent e) {
        // backspace can't be red in keyTyped event
        if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
            backspace();
        }
        updateDisplay();
    }
    public void keyReleased(KeyEvent e) {}

    @Override
    public boolean isFocusable(){
        return true;
    }

    public Calculator() {
        super("Calculator");
        addKeyListener(this);
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