package dokapon.entities;

public class CodePatch {

    String code;
    int offset;

    public CodePatch(String code, int offset) {
        this.code = code;
        this.offset = offset;
    }

    public byte[] writePatch(byte[] data) {
        String[] codeHexa = code.split(" ");
        int[] codeInt = new int[codeHexa.length];
        int z = 0;
        for (String s:codeHexa) {
            codeInt[z++]=Integer.parseInt(s, 16);
        }
        for (int i=0;i<codeInt.length;i++) data[offset+i]=(byte)codeInt[i];
        return data;
    }
}
