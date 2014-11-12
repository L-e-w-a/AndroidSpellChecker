package com.example.androidspellchecker.spellchecker;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import java.util.LinkedList;
import java.util.TreeSet;

import static android.graphics.Color.*;


public class SpellChecker{
    private int count;
    public static final int FIRST_TIME = 1;
    private char[] symbols = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private LinkedList<String> listOfFirstVariation;
    private Vocabulary vocabulary = new Vocabulary();
    private TreeSet<String> variations = new TreeSet<String>();
    private SpannableStringBuilder  builder = new SpannableStringBuilder();
    private String [] inputStringArray;

    public SpellChecker(){
        vocabulary.read();
    }

    public SpannableStringBuilder check(String input) {
        builder.clear();
        parseString(input);

        for (int i = 0; i < inputStringArray.length; i++)
            if (isInVocabulary(inputStringArray[i])) {
                builder.append(paintText(inputStringArray[i], GREEN));
                builder.append(" ");
            } else {
                builder.append(getSuggestions(inputStringArray[i]));
            }

        return builder;
    }

    private SpannableString getSuggestions(String input){
        SpannableString stringOfVariation;
        //generate first time
        listOfFirstVariation = new LinkedList<String>();
        generateVariation(input);
        //generate second time
        if(variations.isEmpty()){
            for(String variant : listOfFirstVariation){
                generateVariation(variant);
            }
        }
        if (variations.isEmpty())
            stringOfVariation = paintText(" {" + input + "?} ", RED);
        else {
            StringBuilder builder = new StringBuilder();
            if (isUpperCaseFirstLetter(input)) {
                for (String current : variations)
                    builder.append(capitalize(current) + " ");
            } else {
                for (String current : variations)
                    builder.append(current + " ");
            }
            stringOfVariation = paintText(" {" + builder.toString() + "} ", YELLOW);
        }
        listOfFirstVariation.clear();
        variations.clear();
        return stringOfVariation;
    }

    private void generateVariation(String input) {
        ++count;
        input = input.toLowerCase();
        rearrangedLettersSuggestion(input, count);
        missingLettersSuggestion(input, count);
        extraLetterSuggestion(input, count);
    }

    private boolean isInVocabulary(String input){
        return vocabulary.contains(input);
    }

    private void rearrangedLettersSuggestion(String input, int count) {
        for (int i = 0; i < input.length() - 1 ; i++) {
            String rearangedletter = input.substring(0, i) + input.substring(i + 1, i + 2) + input.substring(i, i + 1) + input.substring(i + 2);
            if(isInVocabulary(rearangedletter)) variations.add(rearangedletter);
            if(count == FIRST_TIME) listOfFirstVariation.add(rearangedletter);
        }
    }

    private void missingLettersSuggestion(String input, int count) {
        for (int i = 0; i < symbols.length; i++) {
            for (int j = 0; j < input.length() + 1 ; j++) {
                String missingLetter = input.substring(0, j) + symbols[i] + input.substring(j, input.length());
                if(isInVocabulary(missingLetter))
                    variations.add(missingLetter);
                if(count == FIRST_TIME) listOfFirstVariation.add(missingLetter);
            }
        }
    }

    private void extraLetterSuggestion(String input, int count) {
        for (int i = 0; i < input.length(); i++) {
            String extraLetter = input.substring(0, i) + input.substring(i + 1, input.length());
            if(isInVocabulary(extraLetter)) variations.add(extraLetter);
            if(count == FIRST_TIME) listOfFirstVariation.add(extraLetter);
        }
    }

    private boolean isUpperCaseFirstLetter(String input) {
        return Character.isUpperCase(input.charAt(0));
    }

    private String capitalize(String input){
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }
    private void parseString(String input) {
        inputStringArray = input.split(" ");
    }
    private SpannableString paintText(String text, int textColor){
       SpannableString paintText= new SpannableString(text);
        paintText.setSpan(new ForegroundColorSpan(textColor), 0, text.length(), 0);
        return paintText;
    }

}
