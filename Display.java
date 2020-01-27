import javax.swing.*;
import java.awt.*;

class Display extends JPanel {
    JLabel inputLabel;
    JLabel previewLabel;
    void center(JLabel label){
        label.setHorizontalAlignment(JLabel.CENTER);
    }
    public Display() {
        super();
        setLayout(new GridLayout(2, 1));
        Font bigFont=new Font(Font.SERIF, Font.PLAIN, 50);
        inputLabel=new JLabel(" ");
        inputLabel.setFont(bigFont);
        inputLabel.setHorizontalAlignment(JLabel.CENTER);
        add(inputLabel);
        previewLabel=new JLabel(" ");
        previewLabel.setHorizontalAlignment(JLabel.CENTER);
        center(previewLabel);
        add(previewLabel);
    }
    public void setInput(String text){
        if(text.length()==0){
            text=" ";    
        }
        inputLabel.setText(text);
    }
    public void setPreview(String text){
        if(text.length()==0){
            text=" ";    
        }
        previewLabel.setText(text);
    }
}