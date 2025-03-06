/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import javax.swing.Icon;

/**
 *
 * @author Admin
 */
public class Model_Card {
    String title;
    String values;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }


    public Model_Card() {
    }

    public Model_Card(String title, String values) {
        this.title = title;
        this.values = values;
    }
    
    
}
