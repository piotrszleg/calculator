import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@FunctionalInterface
interface InputReceiver {
    void receiveInput(String input);
}

class Keyboard extends Panel {
    void addButton(String name, ActionListener onClick){
        JButton button=new JButton(name);
        button.addActionListener(onClick);
        add(button);
    }
    String[][] buttonLayout={
        {"(", ")", "%", "√"},
        {"7", "8", "9", "/"},
        {"4", "5", "6", "x"},
        {"1", "2", "3", "-"},
        {"0", ".", "=", "+"}
    };
    public Keyboard(InputReceiver inputReceiver) {
        super();
        setLayout(new GridLayout(buttonLayout.length, buttonLayout[0].length));
        for(int i=0; i<buttonLayout.length; i++){
            for(int j=0; j<buttonLayout[i].length; j++){
                String buttonName=buttonLayout[i][j];
                addButton(buttonName, (ActionEvent event)->{
                    if(inputReceiver!=null){
                        inputReceiver.receiveInput(buttonName);
                    }
                });
            }
        }
    }
}