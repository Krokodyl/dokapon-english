package dokapon.services;

import dokapon.enums.CharSide;

import static dokapon.Constants.*;

public class Utils {

    public static String padRight(String s,char c,int length) {
        while (s.length()<length) {
            s+=c;
        }
        return s;
    }
    public static String padLeft(String s,char c,int length) {
        while (s.length()<length) {
            s=c+s;
        }
        return s;
    }

    public static String concat(String[] array) {
        String res = "";
        for (String s:array) res+=s+" ";
        return res.trim();
    }

    public static String getCharCodeFromOffset(int offset, CharSide side) {
        String suffix = "00";
        int left,right;
        int offsetRef = OFFSET_FIRST_CHAR_00;
        if (offset>=OFFSET_FIRST_CHAR_01) {
            offsetRef = OFFSET_FIRST_CHAR_01;
            suffix = "01";
        }
        if (offset>=OFFSET_FIRST_CHAR_02) {
            offsetRef = OFFSET_FIRST_CHAR_02;
            suffix = "02";
            int pos = (offset - offsetRef);
            left = pos / Integer.parseInt("400",16);
            pos = pos - (left*Integer.parseInt("400",16));
            if ((pos >= Integer.parseInt("200",16))) {
                pos = pos - Integer.parseInt("100",16);
                right = pos / Integer.parseInt("20",16);
            }
            else right = pos / Integer.parseInt("20",16);
            String res = left +""+ right;
            if (side==CharSide.ONE) {
                return Integer.toHexString(left) + "" + Integer.toHexString(right) + suffix;
            }
        } else {
            int pos = (offset - offsetRef);
            left = pos / Integer.parseInt("200",16);
            pos = pos - (left*Integer.parseInt("200",16));
            right = pos / Integer.parseInt("20",16);
            String res = left +""+ right;
        }
        if (side==CharSide.ONE) {
            return Integer.toHexString(left) +""+ Integer.toHexString(right*2) + suffix;
        } else {
            return Integer.toHexString(left) +""+ Integer.toHexString(right*2+1) + suffix;
        }
    }

}
