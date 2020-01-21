import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Controls extends Panel {
    void addButton(String name, ActionListener onClick){
        JButton button=new JButton(name);
        button.addActionListener(onClick);
        add(button);
    }
    String[] buttons={"CE", "<", ",", "f(x)"};
    public Controls(InputReceiver inputReceiver) {
        super();
        setLayout(new GridLayout(1, buttons.length));
        for(int i=0; i<buttons.length; i++){
                String buttonName=buttons[i];
                addButton(buttonName, (ActionEvent event)->{
                    if(inputReceiver!=null){
                        inputReceiver.receiveInput(buttonName);
                    }
                });
            
        }
    }
}