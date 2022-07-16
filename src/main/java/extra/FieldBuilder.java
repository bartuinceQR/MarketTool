package main.java.extra;

import main.java.panel.LargePanel;

import javax.swing.*;

//I didn't even need this
//Kinda hard to focus WITH ALL THE SCREAMING IN THE BACKGROUND
public class FieldBuilder {
    public String name = "Dummy";
    public int columns = 30;

    public FieldBuilder setName(String _name){
        this.name = _name;
        return this;
    }

    public FieldBuilder setColumns(int _columns){
        this.columns = _columns;
        return this;
    }

    public JTextField build(){
        return new JTextField(this.name, this.columns);
    }

}
