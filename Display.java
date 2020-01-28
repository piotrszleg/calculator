import javax.swing.*;
import java.awt.*;

class Display extends JPanel {
    JTextArea input;
    JTextArea preview;
    JTextArea createText(){
        JTextArea area=new JTextArea(" ");
        area.setLineWrap(true);
        area.setEditable(false);
        area.setMargin(new Insets(20,20,20,20));
        add(area);
        return area;
    }
    public Display() {
        super();
        setLayout(new GridLayout(2, 1));
        Font bigFont=new Font(Font.SERIF, Font.PLAIN, 50);
        input=createText();
        input.setFont(bigFont);
        preview=createText();
    }
    public void setInput(String text){
        input.setText(text);
    }
    public void setPreview(String text){
        preview.setText(text);
    }
}