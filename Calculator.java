import javax.swing.*;

class Calculator extends JFrame implements InputReceiver {
    Display display;
    Keyboard keyboard;
    FunctionsPanel functionsPanel;
    boolean panelsSwitch=true;
    String input="";

    String interpretInput() throws ParsingException, EvaluationException {
        return String.format("%.2f", new Interpreter(input).interpret());
    }

    void updateDisplay(){
        display.setInput(input);
        try {
            display.setPreview(interpretInput());
        } catch(ParsingException|EvaluationException e){
            display.setPreview("");
        }
    }

    void backspace(){
        if(input.length()>0){
            int i;
            for(i=input.length()-1; i>0; i--){
                if(!Character.isAlphabetic(input.charAt(i))){
                    break;
                }
            }
            input=input.substring(0, i);
        }
    }

    void equalsSign(){
        try {
            input=interpretInput();
        } catch(ParsingException|EvaluationException e) {
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