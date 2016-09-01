package org.renix.dmt.oracle.render;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;

/**
 * @author renzx
 */
@SuppressWarnings("serial")
public class ButtonEditor extends DefaultCellEditor {

    protected JButton button;

    private String label;

    private boolean isPushed;

    public ButtonEditor(JCheckBox checkBox, final String part, final String table, final int rowNum) {

        super(checkBox);

        button = new JButton();

        button.setOpaque(true);

        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                // fireEditingStopped();
                // String path = expProp.getFile() + "/"
                // + expProp.getUserId() + "_" + part + "_"
                // + expProp.getStartDate() + "_"
                // + expProp.getEndDate();
                // String str = "exp "
                // + expProp.getUserId()
                // + " "
                // + "file="
                // + path
                // + ".dmp log="
                // + path
                // + ".log buffer=819200000 tables=("
                // + table
                // + ") query=\"where "
                // + part
                // + ">=to_date("
                // + expProp.getStartDate()
                // + ",'yyyyMMdd-HHmmss') "
                // + "and "
                // + part
                // + "<to_date("
                // + expProp.getEndDate()
                // + ",'yyyyMMdd-HHmmss') "
                // + " \" "
                // +
                // "compress=yes consistent=no grants=no indexes=yes rows=yes triggers=yes constraints=yes";
                JTextPane jpt = new JTextPane();
                // jpt.setText(str);
                JScrollPane jsp = new JScrollPane(jpt) {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(480, 320);
                    }
                };
                JOptionPane.showMessageDialog(null, jsp, "haha", JOptionPane.INFORMATION_MESSAGE);

            }

        });

    }

    public Component getTableCellEditorComponent(JTable table, Object value,

    boolean isSelected, int row, int column) {

        if (isSelected) {

            button.setForeground(table.getSelectionForeground());

            button.setBackground(table.getSelectionBackground());

        } else {

            button.setForeground(table.getForeground());

            button.setBackground(table.getBackground());

        }

        label = (value == null) ? "" : value.toString();

        button.setText(label);

        isPushed = true;

        return button;

    }

    public Object getCellEditorValue() {

        if (isPushed) {

            // JOptionPane.showMessageDialog(button, label + ": Ouch!");

        }

        isPushed = false;

        return new String(label);

    }

    public boolean stopCellEditing() {

        isPushed = false;

        return super.stopCellEditing();

    }

    protected void fireEditingStopped() {

        super.fireEditingStopped();

    }

}
