
import java.util.*;

public class Checkers {

    private static final List<String> allowedEscapeChars;

    static {
        allowedEscapeChars = new ArrayList<>();
        allowedEscapeChars.add("\\t");  // char = \t
        allowedEscapeChars.add("\\n");  // char = \n
        allowedEscapeChars.add("\\0");  // char = \0
        allowedEscapeChars.add("\\'");  // char = \'
        allowedEscapeChars.add("\\\"");  // char = \"
        allowedEscapeChars.add("\\\\");  // char = \\
    }

    // check that attribute "vrijednost" of uniform character "ZNAK" is allowed
    public static boolean checkCharacterConst(String charConstValueStr) {

        if (charConstValueStr.length() > 1)
            return allowedEscapeChars.contains(charConstValueStr);

        // if ZNAK contains only 1 character (length = 1) - check if it is in allowed range for ppjC "char" type
        return checkChar(charConstValueStr);
    }

    // check that attribute "vrijednost" of uniform character "NIZ_ZNAKOVA" is allowed
    public static boolean checkCharacterArray(String[] charConstValuesStrArr) {

        // niz(const(char)) array must end with char = \0
        if (!charConstValuesStrArr[charConstValuesStrArr.length-1].equals("\\0"))
            return false;

        // check each char const in the array
        for (String charConst : charConstValuesStrArr) {
            if (!checkCharacterConst(charConst))
                return false;
        }

        return true;
    }

    public static boolean checkInt(String varValueStr){
        try {
            // Integer range in Java is same as for ppjC (checked javadoc at https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html#parseInt(java.lang.String))
            // -2147483648 <= v <= 2147483647
            int varVal = Integer.parseInt(varValueStr);
            return -2147483648 <= varVal && varVal <= 2147483647;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean checkChar(String varValueStr){
        try {
            int varValue = Integer.parseInt(varValueStr);
            return varValue >= 0 && varValue <= 255;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    // checks if variable of type A can be explicitly casted to variable of type B using (castType)
    // varBType <- (castType) varAType
    public static boolean checkExplicitCast(String varAType, String varBType, String castType){
        // first mandatory check:
        // e.g. char <- (int) char: castType = int cannot be implicity cast to varBType = char
        if (!checkImplicitCast(castType, varBType))
            return false;

        // EXPLICIT: char <- int
        // e.g. char <- (char) int
        // e.g. int <- (char) int
        // possible variants considering implicit casting: int/const(int)/char/const(char) <- (char/const(char)) int/const(int)/char/const(char)
        if (checkImplicitCast(varAType,"int") && checkImplicitCast(castType, "char"))
            return true;
        // e.g. const(char) <- (SOMETHING THAT CAN BE IMPLICITY CAST TO const(char)) char
        // note:
        // -castType can be implicitly cast to varBType (checked in first mandatory if check)
        // -plus: varAType can be implicitly cast to castType
        else if (checkImplicitCast(varAType, castType))
            return true;

        // all other cases - neither can be A implicitly cast to castType or explicitly for the case char <- int
        return false;
    }

    // checks if variable of type A can be explicitly casted to variable of type B using ONE OR MORE castTypes - list of (castType)
    // varBType <- (castType1) (castType2) (castType3) varAType
    // NOTE: elements of castTypes list are added from left to right but the order of casting is REVERSED.
    public static boolean checkExplicitCast(String varAType, String varBType, List<String> castTypes){
        if (castTypes.size() == 1)
            return checkExplicitCast(varAType, varBType, castTypes.get(0));

        // first mandatory check
        if (!checkImplicitCast(castTypes.get(0), varBType))
            return false;

        // read NOTE above the method
        Collections.reverse(castTypes);

        int castListSize = castTypes.size();



        // save inter-results
        String[] interResults = new String[castListSize-1];

        String currentType = varAType;
        for (int i=0; i < castListSize-1; i++) {

            if (checkImplicitCast(currentType, "int") && checkImplicitCast(castTypes.get(i), "char")) {
                currentType = "char";
            } else {
                currentType = castTypes.get(i);
            }
            interResults[i] = currentType;
        }

        // check inferred inter results (types) with explicit casts
        if (!checkExplicitCast(varAType, interResults[0], castTypes.get(0)))
            return false;
        for (int i=1; i < castListSize-1; i++) {
            if (!checkExplicitCast(interResults[i-1], interResults[i], castTypes.get(i)))
                return false;
        }

        return checkExplicitCast(interResults[castListSize-2], varBType, castTypes.get(castListSize-1));
    }

    // checks if variable of type A can be implicitly casted to variable of type B
    // varBType <- varAType
    public static boolean checkImplicitCast(String varAType, String varBType){
        // T = int / char

        // equal types - no casting, just true
        if (varAType.equals(varBType))
            return true;
        else if (checkImplicitConstIntChar(varAType, varBType))
            return true;
        // char/const(char) -> int/const(int)
        else if (checkImplicitConstIntChar(varAType, "char") && checkImplicitConstIntChar(varBType, "int"))
            return true;
        // niz(T) -> niz(const(T))
        else if ((varAType.equals("niz(char)") && varBType.equals("niz(const(char))")) || (varAType.equals("niz(int)") && varBType.equals("niz(const(int))")))
            return true;

        // all other cases of implicit casting - UNALLOWED
        return false;
    }

    private static boolean checkImplicitConstIntChar(String varAType, String varBType) {
        // const(T) -> T
        if ((varAType.equals("const(char)") && varBType.equals("char")) || (varAType.equals("const(int)") && varBType.equals("int")))
            return true;
        // T -> const(T)
        else if ((varAType.equals("char") && varBType.equals("const(char)")) || (varAType.equals("int") && varBType.equals("const(int)")))
            return true;
        return false;
    }
}
