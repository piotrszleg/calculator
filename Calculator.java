import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Calculator extends JFrame implements InputReceiver {
    Display display;
    Keyboard keyboard;
    String input="";

    String interpretInput() throws ParsingException, EvaluationException {
        return String.valueOf(new Interpreter(this.input).interpret());
    }

    void updateDisplay(){
        display.setInput(this.input);
        try {
            display.setPreview(interpretInput());
        } catch(ParsingException|EvaluationException e){
            display.setPreview("");
        }
    }
    
    public void receiveInput(String newInput){
        if(newInput.equals("CE")){
            this.input="";
        } else if(newInput.equals("<")){
            this.input=this.input.substring(0, this.input.length()-1);
        } else if(newInput.equals("=")){
            try {
                this.input=interpretInput();
            } catch(ParsingException|EvaluationException e) {}
        } else if(newInput.equals("f(x)")){
            // open functions panel    
        } else {
            this.input+=newInput;
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
        add(keyboard);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 500);
        setVisible(true);
    }
}