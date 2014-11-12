package com.example.androidspellchecker.spellchecker;

import android.os.Environment;
import android.util.Log;
import java.io.*;
import java.util.TreeSet;


public class Vocabulary {
    private final String LOG_TAG = "My tag";
    private String localPath = "dictionary.txt";
    final String DIR_SD = "/SpellChecker";
    private TreeSet<String> words;

    public Vocabulary() {
        words = new TreeSet<String>();
    }


    public void add(String input) {
        input = input.toLowerCase();
        words.add(input);
    }


    public boolean contains(String input) {
        return words.contains(input.toLowerCase());
    }

    public void read() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD Card is not avalible" + Environment.getExternalStorageState());
            return;
        }
        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + DIR_SD);
        sdPath.mkdirs();
        File sdFile = new File(sdPath, localPath);
        readFile(sdFile);

    }

    private void readFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = " ";
            while ((line = reader.readLine()) != null) {
                add(line);
            }
        } catch (IOException e) {
            Log.d(LOG_TAG, "Can't read file" + e.getMessage());
            e.printStackTrace();
        }
    }
}
