package com.example.androidspellchecker.spellchecker;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.net.URL;

class Download extends AsyncTask<Void, Void, Void> {

    private String webPath = "https://dl.dropboxusercontent.com/u/20012997/dictionary.txt";
    private final String LOG_TAG = "My tag";
    private String localPath = "dictionary.txt";
    final String DIR_SD = "/SpellChecker";
    private OnTaskCompleted listener;

    public Download(OnTaskCompleted listener){
        this.listener=listener;
    }

    @Override
    protected Void doInBackground(Void... params) {
        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + DIR_SD);
        sdPath.mkdirs();
        File sdFile = new File(sdPath, localPath);
        try {
            String str = " ";
            URL url = new URL(webPath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sdFile));
            Log.d(LOG_TAG, "InputStream and OutputStream was opened");
            while ((str = bufferedReader.readLine()) != null) {
                bufferedWriter.write(str + "\n");
            }
            bufferedReader.close();
            bufferedWriter.close();
        } catch (IOException e) {
            Log.d("ERROR READING", "Can't read file" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        listener.onTaskCompleted();
    }
}
