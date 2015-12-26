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
public final class Literals {
    private String Name;
    private final String Value;
    private final int Length;
    private String address;

    public Literals(String Name) {
        this.Name = Name;
        this.Value = value();
        this.Length=Length();
    }

    /**
     * @return the Name
     */
    public String getName() {
        return Name;
    }

    /**
     * @param Name the Name to set
     */
    public void setName(String Name) {
        this.Name = Name;
    }
    
    public String value(){
        String Temp;
        String value = "";
        String x;
        if(Name.startsWith("C'")||Name.startsWith("c'"))
        {
             Temp=(Name.substring(2,Name.length()-1)).toUpperCase();
             for(int i=0;i<Temp.length();i++)
             {
                 x=Integer.toHexString((int)Temp.charAt(i));
                 value=value+x;
                 
             }
             return value;
        }
        else if(Name.startsWith("X'")||Name.startsWith("x'"))
        {
         Temp=(Name.substring(2,Name.length()-1));
         value=Temp;
         return value;
        }
        else
            
        {
            Temp=Name;
            value=Integer.toHexString(Integer.parseInt(Temp));
            return value;
        }
            
    }
    public int Length(){
        
        String Temp;
        int length;
        if(Name.startsWith("C'")||Name.startsWith("c'"))
        {
            Temp=(Name.substring(2,Name.length()-1));
            length=Temp.length();
            return length;
        }
        else if(Name.startsWith("X'")||Name.startsWith("x'"))
        {
         Temp=(Name.substring(2,Name.length()-1));
         length=((Temp.length())/2);
         return length;
        }
        else
        {
            length=3;
            return length;
        }
            
        
        
    }
    
    

    /**
     * @return the Value
     */
    public String getValue() {
        return Value;
    }

    /**
     * @return the Length
     */
    public int getLength() {
        return Length;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
    
}
