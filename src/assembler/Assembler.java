/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Tasneem
 */
public final class Assembler {

    /**
     * @param args the command line arguments
     */
    private AssemblerUI gui;
    private int size;
    private ArrayList<String> addressList = new ArrayList();
    private int locater;
    private HashTable SymTab = new HashTable();
    private ArrayList<String> objList = new ArrayList();
    private final HashTable opTab = new HashTable(0);
    private String ListFile = "";
    private String ObjectFile = "";
    public boolean errorFlag;
    public ArrayList<Literals> LiteralsList = new ArrayList();
    public ArrayList<ArrayList<Literals>> newLiteralList = new ArrayList();
    private int LitCounter = 0;
    private int length;
    private boolean ERRORORGNULL_FOUND=false;

    public Assembler(AssemblerUI gui) {
        this.gui = gui;
        errorFlag = false;
        try {
            locater();
            objectCode();
            LISTFILE();
            OBJECTFILE();
        } catch (ScriptException ex) {
        }
    }

    public void locater() throws ScriptException {

        String code[] = getGui().getInputField().getText().split("\\n");
        setSize(code.length);
        int templocorg = 0;
        int countorgop = 0;
        for (int i = 0; i < getSize(); i++) {
            Splitter test = new Splitter(code[i]);
            boolean found = false;
            boolean isNumber = true;
            boolean isNum = true;
            if (test.getOpcode().equalsIgnoreCase("START")) {
                addressList.add(test.getOperand());
                setLocater(Integer.parseInt(test.getOperand(), 16));

            } else {
                if (test.getOpcode().equalsIgnoreCase("EQU")) {
                    addressList.add("");
                    try {
                        Integer.parseInt(test.getOperand(), 16);
                    } catch (NumberFormatException e) {
                        isNumber = false;
                    }
                    if (isNumber == true) {
                        SymTab.setter(test.getLabel(), test.getOperand());
                    } else {
                        String hamada = SymTab.getvalue(test.getOperand());
                        if(hamada != null){
                            SymTab.setter(test.getLabel(), SymTab.getvalue(test.getOperand()));
                        }else{
                            getValuForExpression(test.getOperand());
                        }
                    }
                } else if (test.getOpcode().equalsIgnoreCase("ORG")) {
                    addressList.add("");
                    if (test.getOperand() != null) {
                        templocorg = locater;
                        countorgop++;

                        try {
                            Integer.parseInt(test.getOperand(), 16);
                        } catch (NumberFormatException e) {
                            isNum = false;
                        }
                        
                        if (isNum == true) {
                            locater = Integer.parseInt(test.getOperand(), 16);
                        } else {
                            if (SymTab.getvalue(test.getOperand()) !=null) {
                                locater = Integer.parseInt(SymTab.getvalue(test.getOperand()), 16);
                            } else {
                             test.setERRORORG_FOUND(true);
                            }
                        }
                    } else {
                        if (countorgop > 0) {
                            locater = templocorg;
                        }
                        else {
                            setERRORORGNULL_FOUND(true);
                        }
                    }

                } else {
                    addressList.add(Integer.toHexString(locater));

                    if (test.isCheckliterals() == true) {
                        String Name = test.getOperand();
                        if (newLiteralList.size() > 0) {
                            A:
                            for (int r = 0; r < newLiteralList.size(); r++) {
                                for (int t = 0; t < newLiteralList.get(r).size(); t++) {
                                    if (Name.equalsIgnoreCase((newLiteralList.get(r).get(t)).getName())) {
                                        found = true;
                                        break A;
                                    }
                                }
                            }
                            if (!found) {
                                Literals LitObject = new Literals(Name);
                                LiteralsList.add(LitObject);
                            }
                        } else {
                            Literals LitObject = new Literals(Name);
                            LiteralsList.add(LitObject);
                        }
                    }
                    if (test.getOpcode().equalsIgnoreCase("LTORG") || test.getOpcode().equalsIgnoreCase("END")) {
                        if (LiteralsList.size() > 0) {
                            addressList.add(Integer.toHexString(locater));
                            LiteralsList.get(0).setAddress(Integer.toHexString(locater));
                            for (int j = 1; j < LiteralsList.size(); j++) {
                                length = LiteralsList.get(j - 1).getLength();
                                locater = locater + length;
                                addressList.add(Integer.toHexString(locater));
                                LiteralsList.get(j).setAddress(Integer.toHexString(locater));
                            }
                            newLiteralList.add(LiteralsList);
                            LiteralsList = new ArrayList();
                        }
                    }

                    if (!test.getLabel().equalsIgnoreCase("")) {
                        if (getSymTab().getvalue(test.getLabel()) != null) {
                            errorFlag = true;
                            break;
                        }

                        getSymTab().setter(test.getLabel(), Integer.toHexString(getLocater()));

                    }
                    if (test.getOpcode().equalsIgnoreCase("RESB")) {
                        setLocater(getLocater() + Integer.parseInt(test.getOperand()));
                    } else if (test.getOpcode().equalsIgnoreCase("RESW")) {
                        setLocater(getLocater() + Integer.parseInt(test.getOperand()) * 3);
                    } else if (test.getOpcode().equalsIgnoreCase("BYTE")) {
                        setLocater(getLocater() + 1);
                    } else if (test.getOpcode().equalsIgnoreCase("ORG")) {
                        setLocater(getLocater());
                    } else {
                        setLocater(getLocater() + 3);
                    }
                }
            }
        }
    }

    public void objectCode() {
        String code[] = getGui().getInputField().getText().split("\\n");
        int counter = 0;
        setSize(code.length);
        for (int i = 0; i < size; i++) {
            Splitter test = new Splitter(code[i]);
            if (test.getOpcode().equalsIgnoreCase("START") || test.getOpcode().equalsIgnoreCase("RESW") || test.getOpcode().equalsIgnoreCase("RESB") || test.getOpcode().equalsIgnoreCase("ORG") || test.getOpcode().equalsIgnoreCase("EQU")) {
                objList.add("");
            } else if (test.getOpcode().equalsIgnoreCase("WORD") || test.getOpcode().equalsIgnoreCase("BYTE")) {
                String x = "";
                for (int j = 6; j > test.getOperand().length(); j--) {
                    x = x + "0";
                }
                objList.add(x + Integer.toHexString(Integer.parseInt(test.getOperand())));
            } else {
                if (test.checkLiteral() == true) {
                    String y;
                    for (int j = 0; j < newLiteralList.size(); j++) {
                        for (int l = 0; l < newLiteralList.get(j).size(); l++) {
                            if (test.getOperand().equalsIgnoreCase(newLiteralList.get(j).get(l).getName())) {
                                y = newLiteralList.get(j).get(l).getAddress();
                                objList.add(getOpTab().getvalue(test.getOpcode()) + y);
                            }

                        }
                    }
                } else if (test.getOpcode().equalsIgnoreCase("LTORG") || test.getOpcode().equalsIgnoreCase("END")) {
                    objList.add("");
                    if (newLiteralList.size() > 0) {
                        String value = "";
                        String z = "";
                        for (int r = 0; r < newLiteralList.get(counter).size(); r++) {
                            value = newLiteralList.get(counter).get(r).getValue();
                            for (int t = 6; t > value.length(); t--) {
                                z = z + "0";

                            }
                            objList.add(z + value);
                        }
                    }
                    counter++;
                } else if (test.getOperand().endsWith(",X") || test.getOperand().endsWith(",x")) {
                    String x = getOpTab().getvalue(test.getOpcode()) + SymTab.getvalue(test.getOperand().substring(0, test.getOperand().length() - 2));
                    objList.add(Integer.toHexString(Integer.parseInt(x, 16) | (int) Math.pow(2, 15)));
                } else {
                    objList.add(getOpTab().getvalue(test.getOpcode()) + SymTab.getvalue(test.getOperand()));
                }
            }
        }

    }

    public void LISTFILE() {
        String code[] = getGui().getInputField().getText().split("\\n");
        int Counter = 0;
        int countlines = 0;
        for (int i = 0; i < newLiteralList.size(); i++) {
            for (int j = 0; j < newLiteralList.get(i).size(); j++) {
                System.out.print(newLiteralList.get(i).get(j).getName() + " ");
            }
            System.out.println("");
        }
        System.out.println(objList);
        System.out.println(addressList);

        setSize(code.length);
        ListFile = "";

        for (int i = 0; i < size; i++) {
            Splitter test = new Splitter(code[i]);
            if (test.isERRORORG_FOUND() == true) {
                ListFile += (addressList.get(countlines) + "\t" + objList.get(countlines) + "\t" + code[i] + "\n");
                countlines++;
                ListFile += ("UNDEFINED OPERAND");
            } else if (test.getOpcode().equalsIgnoreCase("LTORG") || test.getOpcode().equalsIgnoreCase("END")) {
                ListFile += (addressList.get(countlines) + "\t" + objList.get(countlines) + "\t" + code[i] + "\n");
                countlines++;

                if (newLiteralList.size() > 0) {
                    for (int j = 0; j < newLiteralList.get(Counter).size(); j++) {
                        ListFile += (addressList.get(countlines) + "\t" + objList.get(countlines) + "\t" + "*" + "\t" + "=" + newLiteralList.get(Counter).get(j).getName()) + '\n';
                        countlines++;
                    }
                }
                Counter++;
            } else {
                ListFile += (addressList.get(countlines) + "\t" + objList.get(countlines) + "\t" + code[i] + "\n");
                countlines++;
            }
        }
        ListFile = ListFile.toUpperCase();
    }

    public void OBJECTFILE() {
        int length;
        String Name;
        String ADDRESS;
        String LENGTH;
        boolean flag = false;
        int counter = 0;
        int bObj = 1;
        int kObj = 0;
        int b = 1;
        String StartingAddress;
        int k;
        int recordlength = 0;
        String record = "";
        String code[] = getGui().getInputField().getText().split("\\n");
        setSize(code.length);
        length = Integer.parseInt(addressList.get(addressList.size() - 1), 16) - Integer.parseInt(addressList.get(0), 16);
        Splitter test = new Splitter(code[0]);
        Name = test.getLabel();
        if (Name.length() < 6) {
            for (int i = Name.length(); i < 6; i++) {
                Name += " ";
            }
        } else {
            Name = Name.substring(0, 6);
        }
        ADDRESS = addressList.get(0);
        for (int j = ADDRESS.length(); j < 6; j++) {
            ADDRESS = "0" + ADDRESS;
        }
        LENGTH = Integer.toHexString(length);
        for (int j = LENGTH.length(); j < 6; j++) {
            LENGTH = "0" + LENGTH;
        }
        ObjectFile = ("H" + "^" + Name + "^" + ADDRESS + "^" + LENGTH);
        while (b < objList.size()) {
            recordlength = 0;
            record = "";
            StartingAddress = addressList.get(b);
            for (int j = StartingAddress.length(); j < 6; j++) {
                StartingAddress = "0" + StartingAddress;
            }
            kObj = bObj;
            for (k = b; k < size - 1; k++) {
                Splitter TEST = new Splitter((code[k]));
                if (recordlength == 60) {
                    b = k;
                    bObj = kObj;
                    break;

                } else if (TEST.getOpcode().equalsIgnoreCase("RESW") || TEST.getOpcode().equalsIgnoreCase("RESB") || TEST.getOpcode().equalsIgnoreCase("ORG") || TEST.getOpcode().equalsIgnoreCase("LTORG")) {
                    if (TEST.getOpcode().equalsIgnoreCase("LTORG")) {
                        flag = true;
                        counter++;
                    }
                    b = k + 1;
                    bObj = kObj + 1;
                    break;
                } else {
                    if (flag) {
                        if (newLiteralList.size() > 0) {
                            for (int j = 0; j < newLiteralList.get(counter - 1).size(); j++) {
                                record += "^" + objList.get(kObj);
                                recordlength += objList.get(kObj).length();
                                kObj++;
                            }
                        }
                        flag = false;
                    }
                    record += "^" + objList.get(kObj);
                    recordlength += objList.get(kObj).length();
                    b = objList.size();
                }
                kObj++;
            }
            if (!record.equals("")) {
                String RECLEN = Integer.toHexString(recordlength / 2);
                if (RECLEN.length() == 1) {
                    RECLEN = "0" + RECLEN;
                }
                ObjectFile += ("\nT^" + StartingAddress + "^" + RECLEN + record);
            }
        }
        record = "";
        recordlength = 0;
        if (newLiteralList.size() > 0) {
            kObj++;
            for (int n = 0; n < newLiteralList.get(counter).size(); n++) {
                record += "^" + objList.get(kObj);
                recordlength += objList.get(kObj).length();
                kObj++;
            }
            if (!record.equals("")) {
                String RECLEN = Integer.toHexString(recordlength / 2);
                if (RECLEN.length() == 1) {
                    RECLEN = "0" + RECLEN;
                }
                String add = addressList.get(addressList.size() - 1);
                for (int j = add.length(); j < 6; j++) {
                    add = "0" + add;
                }
                ObjectFile += ("\nT^" + add + "^" + RECLEN + record);
            }
        }
        ObjectFile += ("\n" + "E^" + ADDRESS);
        ObjectFile = ObjectFile.toUpperCase();
    }

    public static void main(String[] args) {
        // TODO code application logic here
        AssemblerUI gui = new AssemblerUI();
        gui.setVisible(true);

    }

    /**
     * @return the gui
     */
    public AssemblerUI getGui() {
        return gui;
    }

    /**
     * @param gui the gui to set
     */
    public void setGui(AssemblerUI gui) {
        this.gui = gui;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    public int getLocater() {
        return locater;
    }

    /**
     * @param locater the locater to set
     */
    public void setLocater(int locater) {
        this.locater = locater;
    }

    /**
     * @return the SymTab
     */
    public HashTable getSymTab() {
        return SymTab;
    }

    /**
     * @param SymTab the SymTab to set
     */
    public void setSymTab(HashTable SymTab) {
        this.SymTab = SymTab;
    }

    /**
     * @return the opTab
     */
    public HashTable getOpTab() {
        return opTab;
    }

    public String getListFile() {
        return ListFile;
    }

    public String getObjectFile() {
        return ObjectFile;
    }

    /**
     * @return the LitCounter
     */
    public int getLitCounter() {
        return LitCounter;
    }

    /**
     * @param LitCounter the LitCounter to set
     */
    public void setLitCounter(int LitCounter) {
        this.LitCounter = LitCounter;
    }

    public void getValuForExpression(String Expression1) throws ScriptException {
        String temp = "";
        int valueInt = 0;
        String Type = "";
        char op = 0;
        System.out.println(Expression1);
        for (int i = 0; i < Expression1.length(); i++) {
            if (i == Expression1.length() || Expression1.charAt(i) == '+' || Expression1.charAt(i) == '-') {
                System.out.println(Type);
                try {
                    switch(op){
                        case '+':
                            valueInt += Integer.parseInt(temp);
                            break;
                        case '-':
                            valueInt -= Integer.parseInt(temp);
                            break;
                        default:
                            Type = "Abs";
                            valueInt = Integer.parseInt(temp);
                            break;
                    }
                } catch (Exception e) {
                    switch(op){
                        case '+':
                            valueInt += Integer.parseInt(SymTab.getvalue(temp), 16); 
                            break;
                        case '-':
                            valueInt -=Integer.parseInt(SymTab.getvalue(temp), 16);
                            break;
                        default:
                            Type = "Rel";
                            valueInt = Integer.parseInt(SymTab.getvalue(temp), 16);
                            break;
                    }
                }
                if(i < Expression1.length())op = Expression1.charAt(i);
                temp = "";
            } else {
                temp = temp + Expression1.charAt(i);
            }
        }
        String VAL="";
        for(int i = 0; i<4-Integer.toHexString(valueInt).length() ;++i){
            VAL += "0";
        }
        VAL += Integer.toHexString(valueInt);
        SymTab.setter(Expression1, VAL);
        System.out.println(VAL);
    }

    public static String CharToASCII(char character) {
        int x = (int) character;
        return Integer.toHexString(x);
    }

    private String fix(int i, String toHexString) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the ERRORORGNULL_FOUND
     */
    public boolean isERRORORGNULL_FOUND() {
        return ERRORORGNULL_FOUND;
    }

    /**
     * @param ERRORORGNULL_FOUND the ERRORORGNULL_FOUND to set
     */
    public void setERRORORGNULL_FOUND(boolean ERRORORGNULL_FOUND) {
        this.ERRORORGNULL_FOUND = ERRORORGNULL_FOUND;
    }
    
}
