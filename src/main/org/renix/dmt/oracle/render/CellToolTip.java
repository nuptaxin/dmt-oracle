package org.renix.dmt.oracle.render;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.JToolTip;

/**
 * @author renzx
 *
 */
@SuppressWarnings("serial")
public class CellToolTip extends JToolTip {

    private JTextPane jtp;
    String tipText;

    public CellToolTip() {
        super();
        jtp = new JTextPane();
        jtp.setBorder(BorderFactory.createEtchedBorder());
        jtp.setBackground(Color.CYAN);
        jtp.setSize(200, 100);
        add(jtp);

    }

    public void setTipText(String tipText) {
        jtp.setText(tipText);
    }

    public Dimension getPreferredSize() {
        return new Dimension(200, 100);
    }

}
