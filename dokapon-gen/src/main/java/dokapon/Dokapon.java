package dokapon;

import dokapon.entities.Config;
import dokapon.entities.PointerTable;
import dokapon.services.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        latinLoader.loadLatin();
        spriteWriter.writeLatinChars(latinLoader.getLatinChars(), data);

        DataWriter.writeCodePatches(JsonLoader.loadCodePatches(), data);

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
        DataWriter.saveData(config.getRomOutput(), data);
    }
}
