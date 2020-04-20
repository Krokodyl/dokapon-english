package dokapon.services;

import dokapon.Constants;
import dokapon.characters.JapaneseChar;
import dokapon.characters.LatinChar;
import dokapon.entities.PointerData;
import dokapon.entities.Translation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Translator {

    private List<Translation> translations = new ArrayList<>();

    private LatinLoader latinLoader;

    public Translator(LatinLoader latinLoader) {
        this.latinLoader = latinLoader;
    }

    public void loadTranslationFile(String name) throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(Translator.class.getClassLoader().getResourceAsStream(name)), StandardCharsets.UTF_8));
        String line = br.readLine();
        Translation t = new Translation();
        while (line != null) {
            if (line.contains(Constants.TRANSLATION_KEY_VALUE_SEPARATOR)) {
                String[] split = line.split(Constants.TRANSLATION_KEY_VALUE_SEPARATOR);
                if (split.length>0) {
                    if (split[0].equals(Constants.TRANSLATION_KEY_OFFSETDATA)) {
                        t.setOffsetData(Integer.parseInt(split[1], 16));
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_MENUDATA)) {
                        t.setMenuData(split[1]);
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_ENG)) {
                        t.setTranslation(split[1]);
                    }
                }
            } else {
                if (t.getTranslation() != null && !t.getTranslation().trim().isEmpty()) {
                    translations.add(t);
                }
                t = new Translation();
            }
            line = br.readLine();
        }
    }

    public String[] getTranslation(PointerData p) {
        for (Translation t : translations) {
            if (t.getOffsetData() == p.getOffsetData() && t.getTranslation() != null && !t.getTranslation().isEmpty()) {
                String menuData = t.getMenuData();
                String eng = getCodesFromEnglish(t.getTranslation());
                if (menuData != null && !menuData.isEmpty()) {
                    eng = menuData + " " + eng;
                }
                return eng.split(" ");
            }
        }
        return null;
    }

    public String[] getMenuData(PointerData p) {
        for (Translation t : translations) {
            if (t.getOffsetData() == p.getOffsetData() && t.getTranslation() != null && !t.getTranslation().isEmpty()) {
                String menuData = t.getMenuData();
                if (menuData == null) {
                    return null;
                }
                return menuData.trim().split(" ");
            }
        }
        return null;
    }

    private String getCodesFromEnglish(String eng) {
        String res = "";
        boolean skip = false;
        String skipped = "";
        boolean openQuote = true;
        for (char c : eng.toCharArray()) {
            if (c == '{') {
                skip = true;
                skipped = "";
            }
            if (!skip) {
                LatinChar e = getLatinChar(c + "");
                if (e == null) {
                    if (c=='"' && openQuote) {
                        e = getLatinChar("{O-QUOTES}");
                        openQuote = false;
                    }
                    if (c=='"' && !openQuote) {
                        e = getLatinChar("{C-QUOTES}");
                        openQuote = true;
                    }
                }
                if (e == null) {
                    res = res += e.getCode() + " ";
                }
                res = res += e.getCode() + " ";

            }
            if (skip) {
                skipped += c;
            }
            if (c == '}') {
                skip = false;
                if (containsTranslation(skipped)) {
                    res += getLatinChar(skipped).getCode() + " ";
                } else {
                    res += skipped;
                }
            }
        }
        res = res.replace("{NL}", Constants.NEW_LINE_CHARACTER_HEXA+" ");
        res = res.replace("{EL}", Constants.END_OF_LINE_CHARACTER_HEXA+" ");
        res = res.replace("{", "");
        res = res.replace("}", " ");
        return res.trim();
    }

    public String getJapanese(String codes, List<JapaneseChar> japaneseChars) {
        String[] split = codes.split(" ");
        String res = "";
        for (String s:split) {
            boolean found = false;
            for (JapaneseChar jc:japaneseChars) {
                if (jc.getCode().equals(s)) {
                    found = true;
                    res+=jc.getValue();
                }
            }
            if (!found) {
                res+="{"+s+"}";
            }
        }
        return res;
    }

    private LatinChar getLatinChar(String c) {
        for (LatinChar e : latinLoader.getLatinChars()) {
            if (e.getValue().equals(c)) {
                return e;
            }
        }
        return null;
    }

    private boolean containsTranslation(String s) {
        for (LatinChar e : latinLoader.getLatinChars()) {
            if (e.getValue().equals(s)) {
                return true;
            }
        }
        return false;
    }

}
