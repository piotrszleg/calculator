class Keyboard extends ButtonsPanel {
    static String[][] buttonLayout={
        {"(", ")", "%", "√"},
        {"7", "8", "9", "/"},
        {"4", "5", "6", "x"},
        {"1", "2", "3", "-"},
        {"0", ".", "=", "+"}
    };
    public Keyboard(InputReceiver inputReceiver) {
        super(inputReceiver, buttonLayout[0].length, buttonLayout.length);
        for(int i=0; i<buttonLayout.length; i++){
            for(int j=0; j<buttonLayout[i].length; j++){
                String buttonName=buttonLayout[i][j];
                addButton(buttonName);
            }
        }
    }
}