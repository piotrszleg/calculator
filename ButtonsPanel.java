import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@FunctionalInterface
interface InputReceiver {
    void receiveInput(String input);
}

class ButtonsPanel extends Panel {
    InputReceiver inputReceiver;
    void addButton(String name){
        JButton button=new JButton(name);
        button.addActionListener((ActionEvent event)->{
            if(inputReceiver!=null){
                inputReceiver.receiveInput(name);
            }
        });
        add(button);
    }
    public void setLayoutSize(int width, int height){
        setLayout(new GridLayout(height, width));
    }
    public ButtonsPanel(InputReceiver inputReceiver, int width, int height) {
        super();
        this.inputReceiver=inputReceiver;
        setLayoutSize(width, height);
    }
}