class FunctionsPanel extends ButtonsPanel {
    public FunctionsPanel(InputReceiver inputReceiver) {
        super(inputReceiver, 5, 4);
        FunctionsList functionsList=FunctionsList.getInstance();
        int buttonsCount=functionsList.oneArgumentFunctions.size()+functionsList.twoArgumentsFunctions.size()
                        +functionsList.constants.size();
        setSize(5, (int)Math.ceil((float)buttonsCount/5));
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