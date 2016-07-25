package org.renix.dmt.oracle.render;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class CellRenderer extends JTextField implements TableCellRenderer {

    private CellToolTip toolTip;

    public CellRenderer(JTable table) {


    }

    public Component getTableCellRendererComponent(JTable table, Object value,

    boolean isSelected, boolean hasFocus, int row, int column) {
        

        setBorder(null);
        if (row%2==0){
            setBackground(Color.LIGHT_GRAY );
            setToolTipText((value == null) ? "" : value.toString());
        }else{
            setBackground(null);
            setToolTipText((value == null) ? "" : value.toString());
        }
        if(column==2){
            setHorizontalAlignment(JTextField.RIGHT);
        }else{
            setHorizontalAlignment(JTextField.LEFT);
        }

        
        setText((value == null) ? "" : value.toString());

        return this;

    }
    
//    public void addToolTipBtnListener(ActionListener l) {  
//        toolTipBtnListener = l;  
//   }  
    
    public JToolTip createToolTip() {  
        if (toolTip == null) {  
             toolTip = new CellToolTip();  
//             toolTip.getButton().addActionListener(toolTipBtnListener);  
        }  
         
        return toolTip;  
   }  

}
