package dokapon.services;


import dokapon.entities.Config;
import dokapon.characters.LatinChar;
import dokapon.characters.SpriteLocation;
import dokapon.entities.CodePatch;
import dokapon.characters.LatinSprite;
import dokapon.entities.PointerRange;
import dokapon.entities.PointerTable;
import dokapon.enums.CharSide;
import dokapon.enums.CharType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonLoader {

    static String file = "config.json";

    private static String loadJson() {
        InputStream is =
                JsonLoader.class.getClassLoader().getResourceAsStream( file);
        String jsonTxt = null;
        try {
            jsonTxt = IOUtils.toString( is );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonTxt;
    }

    public static List<String> loadTranslationFiles() {

        List<String> files = new ArrayList<>();
        JSONObject json = new JSONObject(loadJson());

        JSONArray c = json.getJSONArray("translations-files");
        Iterator<Object> iterator = c.iterator();
        while (iterator.hasNext()) {
            String next = (String) iterator.next();
            files.add(next);
        }

        return files;
    }

    public static Config loadConfig() {

        Config config = new Config();

        JSONObject json = new JSONObject(loadJson());

        JSONObject c = json.getJSONObject("config");
        config.setRomInput(c.getString("rom-input"));
        config.setRomOutput(c.getString("rom-output"));
        config.setFileDicoJap(c.getString("file.dico.jap"));
        config.setFileDicoLatin(c.getString("file.dico.latin"));
        config.setFileDicoNames(c.getString("file.dico.noms"));

        return config;
    }

    public static List<LatinChar> loadLatin() {
        List<LatinChar> latinChars = new ArrayList<>();

        JSONObject json = new JSONObject(loadJson());

        JSONArray array = json.getJSONArray("latin");
        Iterator<Object> iterator = array.iterator();
        while (iterator.hasNext()) {
            JSONObject next = (JSONObject) iterator.next();
            LatinChar c = new LatinChar();
            String value = next.getString("value");
            if (next.has("code")) {
                c.setCode(next.getString("code"));
            }
            String type = next.getString("type");
            c.setValue(value);
            c.setType(CharType.valueOf(type));
            if (next.has("sprite")) {
                JSONObject sprite = next.getJSONObject("sprite");
                if (sprite.has("image")) {
                    c.setSprite(new LatinSprite(sprite.getString("image")));
                } else {
                    c.setSprite(new LatinSprite(sprite.getString("image-top"),sprite.getString("image-bot")));
                }
            }
            if (next.has("location")) {
                JSONObject location = next.getJSONObject("location");
                c.setSpriteLocation(new SpriteLocation(Integer.parseInt(location.getString("offset"),16), CharSide.valueOf(location.getString("side"))));
            }
            latinChars.add(c);
        }
        return latinChars;
    }

    public static List<CodePatch> loadCodePatches() {
        List<CodePatch> codePatches = new ArrayList<>();

        JSONObject json = new JSONObject(loadJson());

        JSONArray array = json.getJSONArray("code-patches");
        Iterator<Object> iterator = array.iterator();
        while (iterator.hasNext()) {
            JSONObject next = (JSONObject) iterator.next();
            String code = next.getString("code");
            int offset = Integer.parseInt(next.getString("offset"),16);
            codePatches.add(new CodePatch(code, offset));
        }
        return codePatches;
    }

    public static List<PointerTable> loadTables() {
        List<PointerTable> tables = new ArrayList<>();

        JSONObject json = new JSONObject(loadJson());

        JSONArray array = json.getJSONArray("tables");
        Iterator<Object> iterator = array.iterator();
        while (iterator.hasNext()) {
            JSONObject next = (JSONObject) iterator.next();
            int id = next.getInt("id");
            PointerTable table = new PointerTable(id);
            table.setNewDataStart(next.getInt("new-data-start"));
            table.setNewDataShift(next.getInt("new-data-shift"));
            JSONArray pointersArray = next.getJSONArray("pointers");
            Iterator<Object> it = pointersArray.iterator();
            while (it.hasNext()) {
                JSONObject pointerObject = (JSONObject) it.next();
                PointerRange range = new PointerRange(
                        pointerObject.getInt("start"),
                        pointerObject.getInt("end"),
                        pointerObject.getInt("shift"));
                table.addPointerRange(range);
            }
            if (next.has("menu")) {
                table.setMenu(next.getBoolean("menu"));
            }
            if (next.has("counter")) {
                table.setCounter(next.getBoolean("counter"));
            }
            tables.add(table);
        }
        return tables;
    }

}
