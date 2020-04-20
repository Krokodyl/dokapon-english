package dokapon.services;


import dokapon.entities.CodePatch;
import dokapon.entities.PointerData;
import dokapon.entities.PointerTable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataWriter {

    public static byte[] fillDataWithPlaceHolders(byte[] data) {
        int fullLength = Integer.parseInt("180000", 16)+Integer.parseInt("8000", 16)*10;
        if (data.length<fullLength) {
            byte[] dummy = new byte[fullLength];
            for (int k=0;k<data.length;k++) dummy[k] = data[k];
            for (int k=Integer.parseInt("180000", 16);k<fullLength;k++) dummy[k] = 0;
            data = dummy;
        }
        for (int i=0;i<10;i++) {
            int k = Integer.parseInt("180000", 16)+Integer.parseInt("8000", 16)*i;
            for (int j=0;j<Integer.parseInt("8000", 16);j++) {
                data[k+j] = (byte)(16 + i);
            }
        }
        return data;
    }

    public static byte[] writeCodePatches(List<CodePatch> patchList, byte[] data, boolean debug) {
        for (CodePatch cp:patchList) {
            if (cp.isDebug()==debug)
            cp.writePatch(data);
        }
        return data;
    }

    public static byte[] writeEnglish(PointerTable table, byte[] data) {
        for (PointerData p : table.getDataEng()) {
            int offset = p.getOffset();
            data[offset] = (byte) (p.getValue() % 256);
            data[offset + 1] = (byte) (p.getValue() / 256);
            int offsetData = p.getOffsetData();
            for (String s : p.getData()) {
                int a = Integer.parseInt(s.substring(0, 2), 16);
                int b = Integer.parseInt(s.substring(2, 4), 16);
                data[offsetData] = (byte) a;
                data[offsetData + 1] = (byte) b;
                offsetData += 2;
            }
            String[] menuData = p.getMenuData();
            if (menuData != null) {
                int i = p.getOffsetOldMenuData();
                for (String s : menuData) {
                    int a = Integer.parseInt(s.substring(0, 2), 16);
                    int b = Integer.parseInt(s.substring(2, 4), 16);
                    data[i] = (byte) a;
                    data[i + 1] = (byte) b;
                    i += 2;
                }
            }
        }
        return data;
    }

    public static void saveData(String romOutput, byte[] data) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(romOutput));
            stream.write(data);
            stream.close();
        } catch (IOException ex) {
            Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
