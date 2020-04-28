package dokapon;

import dokapon.entities.Config;
import dokapon.entities.InputPatch;
import dokapon.entities.PointerData;
import dokapon.entities.PointerTable;
import dokapon.services.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static dokapon.Constants.TRANSLATION_KEY_ENG;
import static dokapon.Constants.TRANSLATION_KEY_JAP;

public class Dokapon {

    static byte[] data;
    static Translator translator;
    static LatinLoader latinLoader;
    static SpriteWriter spriteWriter;
    static List<PointerTable> tables;
    static Config config;

    public static void main(String[] args) {
        latinLoader = new LatinLoader();
        spriteWriter = new SpriteWriter();
        translator = new Translator(latinLoader);
        config = JsonLoader.loadConfig();

        try {
            data = Files.readAllBytes(new File(config.getRomInput()).toPath());
        } catch (IOException ex) {
            Logger.getLogger(Dokapon.class.getName()).log(Level.SEVERE, null, ex);
        }
        data = DataWriter.fillDataWithPlaceHolders(data);
        latinLoader.loadLatin();
        spriteWriter.writeLatinChars(latinLoader.getLatinChars(), data);

        DataWriter.writeCodePatches(JsonLoader.loadCodePatches(), data, false);
        //DataWriter.writeCodePatches(JsonLoader.loadCodePatches(), data, true);

        tables = JsonLoader.loadTables();
        for (String s:JsonLoader.loadTranslationFiles()) {
            try {
                translator.loadTranslationFile(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        translator.checkTranslations(data);
        for (PointerTable table:tables) {
            DataReader.readTable(table, data);
        }
        for (PointerTable table:tables) {
            DataReader.generateEnglish(translator, table, data);
        }
        for (PointerTable table:tables) {
            DataWriter.writeEnglish(table, data);
        }

        for (InputPatch ip:JsonLoader.loadInputPatches()) {
            ip.generateCode(latinLoader.getLatinChars());
            if (!ip.isDebug()) ip.writePatch(data);
        }

        for (PointerTable table:tables) {
            if (table.getId()==5) {
                List<PointerData> dataJap = table.getDataJap();
                for (PointerData pd:dataJap) {
                    String printableString = DataWriter.getPrintableString(pd, translator, JsonLoader.loadJap());
                    //System.out.println(printableString);
                }
            }
        }

        /*try {
            DataReader.generateCredits();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        // PRINT DIALOG JAPANESE NAMES
        List<String> names = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        for (PointerTable table:tables) {
            //if (table.getId()==5) {
            List<PointerData> dataJap = table.getDataJap();
            for (PointerData pd:dataJap) {
                if (Utils.concat(pd.getData()).contains("b600")) {
                    String japanese = translator.getJapanese(Utils.concat(pd.getData()), JsonLoader.loadJap());
                    //String printableString = DataWriter.getPrintableString(pd, translator, JsonLoader.loadJap());
                    //System.out.println(printableString);
                    String name = japanese.substring(0, japanese.indexOf("┌"));
                    if (!names.contains(name)) {
                        names.add(name);
                        lines.add(japanese);
                    }
                }
                //System.out.println(printableString);
            }
            //}
        }
        //for (String s:lines) System.out.println(s);

        for (String s:new String[]{
                ""
        })
        System.out.println(
                translator.getJapanese(
                        s,
                        JsonLoader.loadJap()));

        DataWriter.saveData(config.getRomOutput(), data);
    }
}
