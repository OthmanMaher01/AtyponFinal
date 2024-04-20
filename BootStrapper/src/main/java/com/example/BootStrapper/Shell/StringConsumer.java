package com.example.BootStrapper.Shell;

import java.util.function.Consumer;

class StringConsumer implements Consumer<String>{
    private StringBuilder theString;
    public StringConsumer(){
        theString=new StringBuilder();
    }
    @Override
    public void accept(String s) {
        theString.append(s);
    }
    public String getString(){
        return theString.toString();
    }
}