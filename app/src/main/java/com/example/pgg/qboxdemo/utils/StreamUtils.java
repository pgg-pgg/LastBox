package com.example.pgg.qboxdemo.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by pgg on 2018/5/5.
 * 读取res/raw中的json文件
 */

public class StreamUtils {

    public static String get(Context context,int id){
        InputStream stream=context.getResources().openRawResource(id);
        return read(stream);
    }

    private static String read(InputStream stream) {
        return read(stream,"utf-8");
    }

    private static String read(InputStream stream, String encode) {
        if (stream!=null){
            try {
                BufferedReader reader=new BufferedReader(new InputStreamReader(stream,encode));
                StringBuilder sb=new StringBuilder();
                String line=null;
                while ((line=reader.readLine())!=null){
                    sb.append(line+"\n");
                }
                stream.close();
                return sb.toString();
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return "";
    }
}
