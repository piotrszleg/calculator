import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Display extends Panel {
    JLabel inputLabel;
    JLabel previewLabel;
    public Display() {
        super();
        Font bigFont=new Font(Font.SERIF, Font.PLAIN, 50);
        inputLabel=new JLabel("");
        inputLabel.setFont(bigFont);
        add(inputLabel);
        previewLabel=new JLabel("");
        add(previewLabel);
    }

    public void setInput(String text){
        inputLabel.setText(text);
    }
    public void setPreview(String text){
        previewLabel.setText(text);
    }
}