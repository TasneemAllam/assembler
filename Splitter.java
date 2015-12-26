/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import static jdk.nashorn.internal.objects.NativeString.trim;

/**
 *
 * @author Tasneem
 */
public class Splitter {

    private String instruction;
    private String label;
    private String opcode;
    private String operand;
    private boolean error;
    private boolean checksplitter;
    private boolean checkliterals;
    private boolean ERRORORG_FOUND=false;
    private boolean ERROREQU_FOUND=false;
    

   

    public Splitter(String instruction) {
        this.instruction = instruction;
        checksplitter = checksplitter();
        checkliterals=checkLiteral();
        splitInstruction();
    }

    public boolean checksplitter() {
        if (getInstruction().length()>15)
            if (getInstruction().charAt(8) != ' ' || getInstruction().charAt(15) != ' ' && (getInstruction().charAt(16) != ' ' || getInstruction().charAt(16) != '=')) {
                return false;
            } else {
                return true;
            }
        else
             if (getInstruction().charAt(8) != ' ')
                 return false;
             else
                 return true;
        
    }
        public boolean checkLiteral(){
            if(getInstruction().length()>15)
                if(getInstruction().charAt(16)=='=')
                {
                    return true;
                }
                else 
                {
                    return false;
                }
            else
                return false;
        }
    

    public void splitInstruction() {
        String label1 = getInstruction().substring(0, 8);
        String opcode1;
        String operand1;
        if (getInstruction().length() < 15) {
            opcode1 = getInstruction().substring(9, getInstruction().length());
            setOperand(null);
            if ((label1.startsWith(" ") && !label1.equals("        ")) || (opcode1.startsWith(" "))) {
                setError(true);
            } else {
                setLabel(trim(label1));
                setOpcode(trim(opcode1));
                setError(false);
            }
        } else {
            opcode1 = getInstruction().substring(9, 15);
            if (getInstruction().length() <= 35) {
                operand1 = getInstruction().substring(17, getInstruction().length());
            } else {
                operand1 = getInstruction().substring(17, 35);
            }
            if ((label1.startsWith(" ") && !label1.equals("        ")) || (opcode1.startsWith(" ")) || (operand1.startsWith(" ") && !operand1.equals("                  "))) {
                setError(true);
            } else {
                setLabel(trim(label1));
                setOpcode(trim(opcode1));
                setOperand(trim(operand1));
                setError(false);
            }
        }
    }

    /**
     * @return the instruction
     */
    public String getInstruction() {
        return instruction;
    }

    /**
     * @param instruction the instruction to set
     */
    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the opcode
     */
    public String getOpcode() {
        return opcode;
    }

    /**
     * @param opcode the opcode to set
     */
    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    /**
     * @return the operand
     */
    public String getOperand() {
        return operand;
    }

    /**
     * @param operand the operand to set
     */
    public void setOperand(String operand) {
        this.operand = operand;
    }

    /**
     * @return the error
     */
    public boolean isError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * @return the checksplitter
     */
    public boolean isChecksplitter() {
        return checksplitter;
    }

    /**
     * @param checksplitter the checksplitter to set
     */
    public void setChecksplitter(boolean checksplitter) {
        this.checksplitter = checksplitter;
    }

    /**
     * @return the checkliterals
     */
    public boolean isCheckliterals() {
        return checkliterals;
    }

    /**
     * @param checkliterals the checkliterals to set
     */
    public void setCheckliterals(boolean checkliterals) {
        this.checkliterals = checkliterals;
    }

    public void setERRORORG_FOUND(boolean ERRORORG_FOUND) {
        this.ERRORORG_FOUND = ERRORORG_FOUND;
    }

    public boolean isERRORORG_FOUND() {
        return ERRORORG_FOUND;
    }

    public boolean isERROREQU_FOUND() {
        return ERROREQU_FOUND;
    }

    public void setERROREQU_FOUND(boolean ERROREQU_FOUND) {
        this.ERROREQU_FOUND = ERROREQU_FOUND;
    }

   
    
     
     

}
