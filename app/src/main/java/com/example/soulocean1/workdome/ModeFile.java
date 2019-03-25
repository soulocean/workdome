package com.example.soulocean1.workdome;

import android.app.Activity;
import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ModeFile extends Activity {

    public int getMode()  {

        FileInputStream in;
        BufferedReader reader = null;
        StringBuffer content = new StringBuffer();
        try {
            in = openFileInput("modec");
            reader = new BufferedReader(new InputStreamReader(in));
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String s = content.toString();
        int c = Integer.parseInt(s);
        return c;

    }


    public void setMode(int mode) {
        FileOutputStream out;
        BufferedWriter writer = null;

        try {
            out = openFileOutput("modec", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(mode + "");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
