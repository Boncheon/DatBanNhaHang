/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package raven.MonAn;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author nguye
 */
public class TableActionCellEdittor extends DefaultCellEditor{
private TableActionEvent event;
 public TableActionCellEdittor(TableActionEvent event){
     super(new JCheckBox());
     this.event = event;
     
 }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
       PanelAction action = new PanelAction();
       action.initEvent(event, row);
       action.setBackground(table.getSelectionBackground());
       return action;

    }
    
}
