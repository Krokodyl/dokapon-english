package dokapon;

import dokapon.entities.Config;
import dokapon.entities.InputPatch;
import dokapon.entities.PointerTable;
import dokapon.services.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
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

        System.out.println(
                translator.getJapanese(
                        "af00 9214 ea85 b000 0030 4214 6785 fc00 2a00 b300 8d00 9b00 6400 6a00 b500 2600 2901 2a01 1000 4700 2100 0200 3900 1400 b400 ffff ",
                        JsonLoader.loadJap()));

        DataWriter.saveData(config.getRomOutput(), data);
    }
}
