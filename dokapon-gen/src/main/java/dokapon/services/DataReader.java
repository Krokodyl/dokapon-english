package dokapon.services;

import dokapon.entities.PointerData;
import dokapon.entities.PointerRange;
import dokapon.entities.PointerTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dokapon.Constants.END_OF_LINE_CHARACTER_HEXA;

public class DataReader {

    public static PointerTable readTable(PointerTable table, byte[] data) {
        for (PointerRange range:table.getRanges()) {
            if (!table.isCounter()) {
                for (int i = range.getStart(); i <= range.getEnd(); i = i + 2) {
                    PointerData p = new PointerData();
                    int a = (data[i] & 0xFF);
                    int b = (data[i + 1] & 0xFF);
                    int c = b * 256 + a;
                    p.setValue(c);
                    p.setOffset(i);
                    String[] readData = readPointerData(c + range.getShift(), data);
                    p.setData(readData);
                    p.setOffsetData(c + range.getShift());
                    table.addPointerDataJap(p);
                }
            } else {
                int i = range.getStart();
                while (i <= range.getEnd()) {
                    int nb = data[i];
                    for (int k = 1; k <= nb * 2; k = k + 2) {
                        PointerData p = new PointerData();
                        int a = (data[i + k] & 0xFF);
                        int b = (data[i + k + 1] & 0xFF);
                        int c = b * 256 + a;
                        p.setValue(c);
                        p.setOffset(i + k);
                        String[] readData = readPointerData(c + range.getShift(), data);
                        p.setData(readData);
                        p.setOffsetData(c + range.getShift());
                        table.addPointerDataJap(p);
                    }
                    i = i + 1 + nb * 2;
                }
            }
        }
        return table;
    }

    public static PointerTable generateEnglish(Translator translator, PointerTable table, byte[] data) {
        Map<Integer, Integer> mapValues = new HashMap<Integer, Integer>();
        int offsetData = table.getNewDataStart();
        if (table.getNewDataStart()==0) return table;
        for (PointerData p : table.getDataJap()) {
            PointerData newP = new PointerData();
            newP.setOldPointer(p);
            String[] translation = translator.getTranslation(p);
            String[] menuData = translator.getMenuData(p);
            if (translation != null && translation.length > 0) {
                newP.setData(translation);
            }else {
                newP.setData(p.getData());
            }
            if (menuData != null) {
                newP.setMenuData(menuData);
            }
            newP.setOffset(p.getOffset());
            newP.setOffsetData(offsetData);
            newP.setOffsetOldMenuData(p.getOffsetData());
            int oldValue = p.getValue();
            if (!mapValues.containsKey(oldValue)) {
                int value = offsetData - table.getNewDataShift();
                newP.setValue(value);
                mapValues.put(oldValue, value);
                double l = newP.getData().length;
                int longueur = (int)l*2;
                if (table.getId()==4) {
                    longueur = (int) ((Math.ceil(l / 8)) * 16);
                    if (longueur%32==16) longueur+=16;
                }
                offsetData += longueur;
                if (table.getId()==5) {
                    if (offsetData%16==0) offsetData+=2;
                }
            } else {
                newP.setValue(mapValues.get(oldValue));
            }
            table.addPointerDataEng(newP);
        }
        return table;
    }

    private static String[] readPointerData(int offset, byte[] data) {
        boolean end = false;
        List<String> res = new ArrayList<String>();
        int i = offset;
        while (!end) {
            int a = (data[i] & 0xFF);
            int b = (data[i + 1] & 0xFF);
            String s = Integer.toHexString(b);
            s = Utils.padLeft(s,'0',2);
            s = Integer.toHexString(a) + s;
            s = Utils.padLeft(s,'0',4);
            if (s.equals(END_OF_LINE_CHARACTER_HEXA)) {
                end = true;
            }
            res.add(s);
            i = i + 2;
        }
        return res.toArray(new String[0]);
    }
}
