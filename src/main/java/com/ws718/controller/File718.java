package com.ws718.controller;

import com.ws718.ClientApplication;
import com.ws718.util.FolderWatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author :MysticalYcc
 * @date :11:12 2019/2/20
 */
public class File718 {

    public File718() {
        File file = new File(System.getProperty("user.dir") + "/resource/conf.properties");
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            // 使用properties对象加载输入流
            properties.load(in);
            //获取key对应的value值
            file_extension = properties.getProperty("fileExtension");
            timeout = properties.getProperty("timeout") == null ? 100 : Integer.parseInt(properties.getProperty("timeout"));
            startUp = properties.getProperty("startUp") == null ? 1 : Integer.parseInt(properties.getProperty("startUp"));
        } catch (Exception e) {
            System.out.println("加载配置文件出错");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static long timeout = 100;
    private static String encoding = System.getProperty("file.encoding");
    private static String file_extension = ".txt";
    private static Integer startUp = 1;

    public static void main(String[] args) {
        new File718();
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
