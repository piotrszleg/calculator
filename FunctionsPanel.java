class FunctionsPanel extends ButtonsPanel {
    static final int width=4;
    public FunctionsPanel(InputReceiver inputReceiver) {
        super(inputReceiver, width, width);// height will be changed later
        FunctionsList functionsList=FunctionsList.getInstance();
        int buttonsCount=functionsList.oneArgumentFunctions.size()+functionsList.twoArgumentsFunctions.size()
                        +functionsList.constants.size();
        setLayoutSize(width, (int)Math.ceil((float)buttonsCount/width));
        for(String name : functionsList.oneArgumentFunctions.keySet()){
            addButton(name);
        }
        for(String name : functionsList.twoArgumentsFunctions.keySet()){
            addButton(name);
        }
        for(String name : functionsList.constants.keySet()){
            addButton(name);
        }
    }
}