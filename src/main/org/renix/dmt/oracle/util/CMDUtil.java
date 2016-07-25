package org.renix.dmt.oracle.util;

public class CMDUtil {
    public static String[] getNewCmd(String title, String command) {
        String[] cmd = new String[3];
        cmd[0] = "cmd.exe";
        cmd[1] = "/c";
        cmd[2] = "start \"" + title + "\" " + command;
        return cmd;
    }
}
