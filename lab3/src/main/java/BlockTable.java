import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.HashMap;
import java.util.Map;

public class BlockTable {
    //map<imeVarijable, (tip, vrijednost)>
    private Map<String, VariableTypeValue> nameToTypeValueMap = new HashMap<>();
    // parent node's block table
    private BlockTable parent;


    /**
     * metoda za dohvat vrijednosti neke varijable
     *
     * @param varName
     * @return
     * @throws NullPointerException - ako ne nadje varijablu sve do root nodea !!!!!!VAZNO!!!!!!!
     */
    public String getVariableValue(String varName) {
        VariableTypeValue varInMap = nameToTypeValueMap.get(varName);
        if (varInMap == null) {
            return getVariableFromParentBlock(varName).getVarValue();   //potencijalno: NullPointerException (vidi javadoc metode: @throws)
        }
        return nameToTypeValueMap.get(varName).getVarValue();
    }

    /**
     *
     * metoda contains koja na kraju lokalne provjere pita roditelja
     * ako roditelj ima varijablu u svom BlockTable, onda vraca tu varijablu
     * inace pita svog roditelja itd. rekurzivno
     * @param varName
     * @return
     *  @throws NullPointerException - ako ne nadje varijablu sve do root nodea !!!!!!VAZNO!!!!!!!
     */
    public VariableTypeValue getVariableFromParentBlock(String varName) {
        Map<String, VariableTypeValue> parentVariableMap = parent.nameToTypeValueMap;

        if (parentVariableMap.containsKey(varName)) {
            return parentVariableMap.get(varName);
        }

        return parent.getVariableFromParentBlock(varName);
    }


    /**
     * metoda za dodavanje nove varijable / mijenjanje vrijednosti postojece varijable
     *
     * @throws IllegalArgumentException ako se pokuša za varijablu istog imena promijeniti njezin tip
     */

    public void addVariableToBlockTable(String varName, String varType, String varValue) {
        VariableTypeValue varTypeValue = nameToTypeValueMap.get(varName);

        if (varTypeValue  == null) {
            nameToTypeValueMap.put(varName, new VariableTypeValue(varType, varValue));
        } else {
            // if the variable is already recorded in the block table
            if (!varTypeValue.getVarType().equals(varType)) {
                throw new IllegalArgumentException("Cannot change variable type!");
            }
            // just change variable value
            varTypeValue.setVarValue(varValue);
        }
    }

    // helper class - contains variable type and value
    private static class VariableTypeValue {
        private String varType;
        private String varValue;

        public VariableTypeValue(String varType, String varValue) {
            this.varType = varType;
            this.varValue = varValue;
        }

        public String getVarType() {
            return varType;
        }

        public void setVarType(String varType) {
            this.varType = varType;
        }

        public String getVarValue() {
            return varValue;
        }

        public void setVarValue(String varValue) {
            this.varValue = varValue;
        }
    }
}
