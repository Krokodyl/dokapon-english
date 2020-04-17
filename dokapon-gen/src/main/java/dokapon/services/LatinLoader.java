package dokapon.services;

import dokapon.Constants;
import dokapon.characters.LatinChar;
import dokapon.characters.LatinSprite;
import dokapon.characters.SpriteLocation;
import dokapon.enums.CharSide;
import dokapon.enums.CharType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static dokapon.Constants.*;

public class LatinLoader {


    private List<LatinChar> latinChars = new ArrayList<>();

    public List<LatinChar> getLatinChars() {
        return latinChars;
    }

    public void loadLatin() {
        latinChars.addAll(JsonLoader.loadLatin());

        String japArmor = "JAP=な  し    {NL}{ARMOR}ぬののふく  {NL}{ARMOR}ローブ    {NL}{ARMOR}あさのふく  {NL}{ARMOR}マント    {NL}{ARMOR}パディット  {NL}{ARMOR}レザーアーマー{NL}{ARMOR}キルボアール {NL}{ARMOR}リングメイル {NL}{ARMOR}スケイルメイル{NL}{ARMOR}ライトアーマー{NL}{ARMOR}くさりかたびら{NL}{ARMOR}スプリント  {NL}{ARMOR}ホーバーク  {NL}{ARMOR}ハーフプレート{NL}{ARMOR}プレートメイル{NL}{ARMOR}ナイトアーマー{NL}{ARMOR}バトルスーツ {NL}{ARMOR}オーラアーマー{NL}{ARMOR}マジックローブ{NL}{ARMOR}チキンマント {NL}{ARMOR}エルブンマント{NL}{ARMOR}ライフガード {NL}{ARMOR}リフレクス  {NL}{ARMOR}ダークアーマー{NL}{ARMOR}シルバーメイル{NL}{ARMOR}デモンアーマー{NL}{ARMOR}ケイオスメイル{NL}{ARMOR}{NL}{ARMOR}{NL}{ARMOR}{NL}{ARMOR}{NL}{ARMOR}{NL}{ARMOR}{EL}";
        List<LatinChar> armorDoubleLatinChars = generateLatinCharTypeDouble(japArmor, "tables/armors-jap.txt", LENGTH_ARMOR_NAMES);
        for (LatinChar lc:armorDoubleLatinChars) {
            if (!latinChars.contains(lc)) latinChars.add(lc);
        }
        String japWeapon = "JAP=な  し    {NL}{WEAP}ナイフ    {NL}{WEAP}ダガー    {NL}{WEAP}グラディウス {NL}{WEAP}ハンドアクス {NL}{WEAP}レイピア   {NL}{WEAP}スピア    {NL}{WEAP}せスタス   {NL}{WEAP}メイス    {NL}{WEAP}フレイル   {NL}{WEAP}サーベル   {NL}{WEAP}トライデント {NL}{WEAP}バトルフィスト{NL}{WEAP}シミター   {NL}{WEAP}ロングソード {NL}{WEAP}バトルアクス {NL}{WEAP}ロングスピア {NL}{WEAP}せルフボウ  {NL}{WEAP}クロスボウ  {NL}{WEAP}ウォーハンマー{NL}{WEAP}エストック  {NL}{WEAP}ロングボウ  {NL}{WEAP}リピーター  {NL}{WEAP}ヌンチャク  {NL}{WEAP}シャムシール {NL}{WEAP}グレートアクス{NL}{WEAP}モール    {NL}{WEAP}グレートソード{NL}{WEAP}アーバレスト {NL}{WEAP}パルチザン  {NL}{WEAP}ハルベルト  {NL}{WEAP}エルブンボウ {NL}{WEAP}きこうけん  {NL}{WEAP}フレイムソード{NL}{WEAP}らんぶのつるぎ{NL}{WEAP}サンダーアクス{NL}{WEAP}シルバーソード{NL}{WEAP}たいまのやり {NL}{WEAP}スリープアロー{NL}{WEAP}げんむのつえ {NL}{WEAP}ポイズンブロウ{NL}{WEAP}ソウルイーター{NL}{WEAP}オーラナックル{NL}{WEAP}ごうりゅうけん{NL}{WEAP}あんこくけん {NL}{WEAP}{NL}{WEAP}{NL}{WEAP}{EL}";
        List<LatinChar> weaponDoubleLatinChars = generateLatinCharTypeDouble(japWeapon, "tables/weapons-jap.txt", LENGTH_WEAPON_NAMES);
        for (LatinChar lc:weaponDoubleLatinChars) {
            if (!latinChars.contains(lc)) latinChars.add(lc);
        }

        initializeCodesAndLocations();
    }

    private void initializeCodesAndLocations() {
        List<SpriteLocation> locations = generateAllPossibleSpriteLocations();
        for (LatinChar lc:latinChars) {
            if (locations.contains(lc.getSpriteLocation())) {
                locations.remove(lc.getSpriteLocation());
            }
        }
        for (LatinChar lc:latinChars) {
            if (lc.getType()!= CharType.NO_SPRITE && lc.getSpriteLocation()==null && !locations.isEmpty()) {
                SpriteLocation spriteLocation = locations.get(0);
                lc.setSpriteLocation(spriteLocation);
                locations.remove(spriteLocation);
            }
            if (lc.getCode()==null) {
                String code = Utils.getCharCodeFromOffset(lc.getSpriteLocation().getOffset(), lc.getSpriteLocation().getSide());
                lc.setCode(code);
            }
        }
    }

    private List<SpriteLocation> generateAllPossibleSpriteLocations() {
        List<SpriteLocation> locations = new ArrayList<>();
        int off = OFFSET_FIRST_CHAR_00;
        int line = 0;
        while (off < Integer.parseInt("e0000",16)) {
            if (off>=OFFSET_FIRST_CHAR_02) {
                locations.add(new SpriteLocation(off, CharSide.ONE));
            }
            else {
                locations.add(new SpriteLocation(off, CharSide.ONE));
                locations.add(new SpriteLocation(off, CharSide.TWO));
            }
            off += 32;
            line++;
            if (line%8==0) off += 256;
        }
        return locations;
    }

    private List<LatinChar> generateLatinCharTypeDouble(String japanese, String filename, int maxLength) {
        List<LatinChar> res = new ArrayList<>();
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream(filename)), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(japanese);
        japanese = japanese.replace(TRANSLATION_KEY_JAP,TRANSLATION_KEY_ENG);
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = entry.getKey().toString();
            key = Utils.padRight(key,' ',maxLength);
            String val = entry.getValue().toString();
            if (val.length()<maxLength) {
                val = Utils.padRight(val,' ',maxLength);
            }
            if (val.length()==maxLength) {
                japanese = japanese.replace(key,val);
            }
            else {
                String doubleChar = generateDoubleCharName(val, maxLength);
                japanese = japanese.replace(key,doubleChar);
                res.addAll(generateDoubleChars(val, maxLength));
            }
        }
        System.out.println(japanese);
        return res;
    }

    private List<LatinChar> generateDoubleChars(String name, int maxLength) {
        List<LatinChar> res = new ArrayList<>();
        name = name.trim().toUpperCase();
        String[] split = name.split(" ");
        String top = split[0];
        String bot = split[1];
        if (top.length()>maxLength || bot.length()>maxLength) {
            System.out.println("ERROR");
        }
        else {
            int length = top.length();
            if (length<bot.length()) length = bot.length();
            for (int k=0;k<length;k++) {
                String topChar = " ";
                if (k<top.length()) topChar = top.charAt(k)+"";
                String botChar = " ";
                if (k<bot.length()) botChar = bot.charAt(k)+"";
                String pair = topChar+botChar;
                LatinChar dc = new LatinChar();
                dc.setValue("{DC-"+pair+"}");
                dc.setIgValue(pair);
                initDoubleCharInfo(dc);
                res.add(dc);
            }
        }
        return res;
    }

    // Assuming s contains a space
    private static String generateDoubleCharName(String name, int maxLength) {
        String res = "";
        name = name.trim().toUpperCase();
        String[] split = name.split(" ");
        String top = split[0];
        String bot = split[1];
        if (top.length()>maxLength || bot.length()>maxLength) {
            System.out.println("ERROR");
        }
        else {
            int length = top.length();
            if (length<bot.length()) length = bot.length();
            for (int k=0;k<length;k++) {
                String topChar = " ";
                if (k<top.length()) topChar = top.charAt(k)+"";
                String botChar = " ";
                if (k<bot.length()) botChar = bot.charAt(k)+"";
                String pair = topChar+botChar;
                res += "{DC-"+pair+"}";
            }
            for (int k=0;k<maxLength-length;k++) res += " ";
        }
        return res;
    }

    private void initDoubleCharInfo(LatinChar latinChar) {
        latinChar.setType(CharType.DOUBLE);
        char c = latinChar.getIgValue().charAt(0);
        int val = c-'A';
        String prefix = "";
        if (val<10) prefix = "0";
        String top = "images/double-chars/straight/char-"+prefix+val+".png";
        c = latinChar.getIgValue().charAt(1);
        val = c-'A';
        prefix = "";
        if (val<10) prefix = "0";
        String bot = "images/double-chars/straight/char-"+prefix+val+".png";
        latinChar.setSprite(new LatinSprite(top, bot));
    }
}
