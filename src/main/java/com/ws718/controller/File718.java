package com.ws718.controller;

import com.ws718.ClientApplication;
import com.ws718.util.FolderWatcher;

import java.io.IOException;

/**
 * @author :MysticalYcc
 * @date :11:12 2019/2/20
 */
public class File718 {
    private static String encoding = System.getProperty("file.encoding");
    private static String file_extension = null;
    private static Integer startUp = 1;

    public static void main(String[] args) {
        if (args.length > 0) {
            for (String str : args) {
                if (str.equals("0") || str.equals("1")) {
                    startUp = Integer.parseInt(str);
                } else if (str.startsWith(".")) {
                    file_extension = str;
                } else {
                    encoding = str;
                }
            }
        }
        if (startUp == 1) {
            try {
                new FolderWatcher("InFile", encoding, file_extension);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String[] params = new String[0];
            ClientApplication.main(params);
        }
    }
}
