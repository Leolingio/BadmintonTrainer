package com.sensolic.badmintontrainer;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

public class InputFilterScore implements InputFilter {

    private int min, max;
    private EditText scoreThis;
    private EditText scoreOther;

    public InputFilterScore(int min, int max, EditText scoreThis, EditText scoreOther) {
        this.min = min;
        this.max = max;
        this.scoreThis = scoreThis;
        this.scoreOther = scoreOther;
    }

    public InputFilterScore(String min, String max, EditText scoreThis, EditText scoreOther) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
        this.scoreThis = scoreThis;
        this.scoreOther = scoreOther;
    }

    /**
     *  Checks if score is valid
     * @param score1
     * @param score2
     * @return true if score is valid, false if not
     */
    public static boolean checkScore(int score1, int score2){
        if(score1 < 0 || score2 < 0 || score1 > 30 || score2 > 30) return false;    // Score values not in range

        if(score1 < 20 && score2 == 21
                || score1 == 21 && score2 < 20) return true;    // Normal game till 21 points
        if(score1 == 30 && (score2 == 28 || score2 == 29)
                || score2 == 30 && (score1 == 28 || score1 == 29))  return true;    // Maximum points 30:29 or 30:28
        if(score1 > 19 && score2 > 19 && Math.abs(score1-score2) == 2)  return true;

        return false;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int inputThis = -1, inputOther = -1;

        if(source.toString().isEmpty()) return "";      // If characters are deleted

        try {
            inputThis = Integer.parseInt(dest.toString() + source.toString());
            inputOther = Integer.parseInt(scoreOther.getText().toString());
        } catch (NumberFormatException nfe) {
            // This should only happen if inputOther is empty
        }

        if (!isInRange(min, max, inputThis)) {
            // Input is not in between of min and max
            return "";
        }
        if (dest.toString().equals("0")) {
            // If 0 is already written, then don't add other numbers
            return "";
        }
        if(inputOther == -1 && (inputThis < 20) && (inputThis != 1)
                && (inputThis != 2) && (inputThis != 3)){
            // Auto complete other score
            if(Settings.autocompleteScore()) scoreOther.setText("21");
        } else if(inputOther != -1 && (inputThis >= 20 || inputOther >= 20)
                && inputThis >= 10 && !checkScore(inputThis,inputOther)){
                return "";
        }

        return null;
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
