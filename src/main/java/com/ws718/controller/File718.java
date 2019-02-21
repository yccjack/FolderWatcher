package com.ws718.controller;

import com.ws718.util.FolderWatcher;

import java.io.IOException;

/**
 * @author :MysticalYcc
 * @date :11:12 2019/2/20
 */
public class File718 {

    public static void main(String[] args) {
        String encoding = null;
        String file_extension = null;
        if (args.length > 0) {
            encoding = args[0];
            if (args.length > 1) {
                file_extension = args[1];
            }

        }
        try {
            new FolderWatcher("InFile", encoding, file_extension);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
