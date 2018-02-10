package org.techvalleyhigh.frc5881.powerup.robot.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutonomousDecoder {
    public static boolean isValidIntRangeInput(String text) {
        Pattern re_valid = Pattern.compile(
                "# Validate comma separated integers/integer ranges.\n" +
                        "^             # Anchor to start of string.         \n" +
                        "[0-9]+        # Integer of 1st value (required).   \n" +
                        "(?:           # Range for 1st value (optional).    \n" +
                        "  -           # Dash separates range integer.      \n" +
                        "  [0-9]+      # Range integer of 1st value.        \n" +
                        ")?            # Range for 1st value (optional).    \n" +
                        "(?:           # Zero or more additional values.    \n" +
                        "  ,           # Comma separates additional values. \n" +
                        "  [0-9]+      # Integer of extra value (required). \n" +
                        "  (?:         # Range for extra value (optional).  \n" +
                        "    -         # Dash separates range integer.      \n" +
                        "    [0-9]+    # Range integer of extra value.      \n" +
                        "  )?          # Range for extra value (optional).  \n" +
                        ")*            # Zero or more additional values.    \n" +
                        "$             # Anchor to end of string.           ",
                Pattern.COMMENTS);
        Matcher m = re_valid.matcher(text);
        if (m.matches())    return true;
        else                return false;
    }
    public static ArrayList<Integer> getIntRanges(String text) {
        Pattern re_next_val = Pattern.compile(
                "# extract next integers/integer range value.    \n" +
                        "([0-9]+)      # $1: 1st integer (Base).         \n" +
                        "(?:           # Range for value (optional).     \n" +
                        "  -           # Dash separates range integer.   \n" +
                        "  ([0-9]+)    # $2: 2nd integer (Range)         \n" +
                        ")?            # Range for value (optional). \n" +
                        "(?:,|$)       # End on comma or string end.",
                Pattern.COMMENTS);
        Matcher m = re_next_val.matcher(text);
        ArrayList<Integer> resultArray = new ArrayList<>();
        while (m.find()) {
            /*
            m.group(1) is the beginning of the range
            m.group(2) is the end of the range
             */
            resultArray.add(Integer.parseInt(m.group(1)));
            if (m.group(2) != null) {
                for (int j = Integer.parseInt(m.group(1)) + 1; j <= Integer.parseInt(m.group(2)); j++) {
                    //Adds the number that j stands for into the array
                    resultArray.add(j);
                }
            }
        }
        //
        System.out.println("Auto Options: " + resultArray);
        //Returns the array so that the array can be utilized in other places
        return resultArray;
    }
 }
