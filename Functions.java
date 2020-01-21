import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Functions extends Panel {
    String[] functionNames={
        "sin", "cos", "tg", "ctg"
    };
    void addButton(String name, ActionListener onClick){
        JButton button=new JButton(name);
        button.addActionListener(onClick);
        add(button);
    }
    public Functions() {
        super();
        setLayout(new GridLayout(5, 4));
        for(int i=0; i<functionNames.length; i++){
                String buttonName=functionNames[i];
                addButton(buttonName, (ActionEvent event)->{
                    System.out.println(buttonName);
                });
            
        }
    }
}