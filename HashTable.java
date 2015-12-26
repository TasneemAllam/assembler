/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

/**
 *
 * @author Tasneem
 */
public class HashTable {

    private int n = (int) 1e4 + 7;
    private String[] SYMOPTable;

    public HashTable() {
        this.SYMOPTable = new String[n];
    }

    public HashTable(int setterHash) {
        this.SYMOPTable = new String[n];
        setterHash();
    }

    public void setter(String operation, String opcode) {
        int Hash = Hashkey(operation);
        SYMOPTable[Hash] = opcode;
    }

    public String getvalue(String operation) {
        int Hash = Hashkey(operation);
        return SYMOPTable[Hash];
    }

    private int Hashkey(String operation) {
        int Hashkey = 0;
        int charv = 0;
        for (int i = 0; i < operation.length(); i++) {
            if (operation.charAt(i) >= 'a' && operation.charAt(i) <= 'z') {
                charv = operation.charAt(i) - 'a' + 1;
            }
            if (operation.charAt(i) >= 'A' && operation.charAt(i) <= 'Z') {
                charv = operation.charAt(i) - 'A' + 1;
            }
            Hashkey += charv * (Math.pow(10, operation.length() - i - 1));
        }
        return Hashkey1(Hashkey);
    }

    private int Hashkey1(int Hashkey) {
        int Hashkey1 = Hashkey % n;
        return Hashkey1;
    }

    private void setterHash() {
        setter("add", "18");
        setter("and", "40");
        setter("comp", "28");
        setter("div", "24");
        setter("j", "3C");
        setter("jeq", "30");
        setter("jgt", "34");
        setter("jlt", "38");
        setter("jsub", "48");
        setter("lda", "00");
        setter("ldch", "50");
        setter("ldl", "08");
        setter("ldx", "04");
        setter("mul", "20");
        setter("or", "44");
        setter("rd", "D8");
        setter("rsub", "4C");
        setter("sta", "0C");
        setter("stch", "54");
        setter("stl", "14");
        setter("stx", "10");
        setter("sub", "1c");
        setter("td", "E0");
        setter("tix", "2c");
        setter("wd", "DC");

    }

}
