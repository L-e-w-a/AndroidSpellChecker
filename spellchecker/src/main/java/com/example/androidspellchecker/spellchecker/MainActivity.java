package com.example.androidspellchecker.spellchecker;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;


public class MainActivity extends Activity implements OnTaskCompleted {
    private EditText editText;
    private Button btnCheck;
    private SpellChecker  spellChecker;
    private String localPath = "dictionary.txt";
    final String DIR_SD = "/SpellChecker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        editText = (EditText)findViewById(R.id.edText);
        btnCheck = (Button)findViewById(R.id.btnCheck);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isDictionaryExist()) {
            new Download(this).execute();
            btnCheck.setEnabled(false);
        }else{
            spellChecker = new SpellChecker();
        }
    }

    public void check(View v){

        String input = editText.getText().toString();
        editText.setText(spellChecker.check(input));
    }
    @Override
    public void onTaskCompleted() {
        spellChecker = new SpellChecker();
        btnCheck.setEnabled(true);
    }

    private boolean isDictionaryExist() {
        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(sdPath.getAbsolutePath() + DIR_SD);
        sdPath.mkdirs();
        File sdFile = new File(sdPath, localPath);
        return sdFile.exists();
    }
}
