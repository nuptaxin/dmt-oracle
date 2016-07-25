package org.renix.dmt.oracle.render;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class DisplayJFrame {
    public static void runJFrame(final JFrame jf, final String jFrameName,
            final int width, final int height) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                jf.setTitle(jFrameName);
                jf.setBounds(200, 100, width, height);
                // jf.setSize(width, height);
                jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jf.setVisible(true);
            }
        });
    }
}
