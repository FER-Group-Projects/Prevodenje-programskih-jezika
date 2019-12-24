

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
    public static boolean checkCharacterArray(String charConstValuesStrArr) {

        char[] chars = charConstValuesStrArr.toCharArray();


        // check each char const in the array - all the way up to NOT including last character
        int i=0;
        while (i < charConstValuesStrArr.length()-1) {
            char currentChar = chars[i];
            char nextChar = chars[i+1];
            Boolean isValidCharConst = null;
            if (currentChar == '\\') {
                isValidCharConst = checkCharacterConst(Character.toString(currentChar) + Character.toString(nextChar));
                // two characters checked - escaping and escaped char
                i += 2;
            } else {
                isValidCharConst = checkCharacterConst(Character.toString(currentChar));
                i += 1;
            }
            if (!isValidCharConst)
                return false;
        }

        // check last character - it cannot be escape char (\) because there is nothing to be esacpe (no more characters after the last character)
        if (chars[chars.length-1] == '\\')
            return  false;

        return true;
    }

    public static boolean checkInt(String varValueStr){
        try {
            // Integer range in Java is same as for ppjC (checked javadoc at https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html#parseInt(java.lang.String))
            // -2147483648 <= v <= 2147483647
            Integer.parseInt(varValueStr);
            return true;
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
    // Is this possible (true/false):(castType) varAType?
    public static boolean checkExplicitCast(Type varAType, Type castType){

        // EXPLICIT: char <- int
        // e.g. char <- (char) int
        // e.g. int <- (char) int
        // possible variants considering implicit casting: int/const(int)/char/const(char) <- (char/const(char)) int/const(int)/char/const(char)
        if (checkImplicitCast(varAType,Type.INT) && checkImplicitCast(castType, Type.CHAR))
            return true;
        // IMPLICIT: varAType can be implicitly cast to castType
        else if (checkImplicitCast(varAType, castType))
            return true;

        // all other cases - neither can be A implicitly or explicitly cast to castType
        return false;
    }

    // checks if variable of type A can be explicitly casted to variable of type B using (castType)
    // varBType <- (castType) varAType
    public static boolean checkExplicitCast(Type varAType, Type varBType, Type castType){
        // first mandatory check:
        // e.g. char <- (int) char: castType = int cannot be implicity cast to varBType = char
        if (!checkImplicitCast(castType, varBType))
            return false;

        // EXPLICIT: char <- int
        // e.g. char <- (char) int
        // e.g. int <- (char) int
        // possible variants considering implicit casting: int/const(int)/char/const(char) <- (char/const(char)) int/const(int)/char/const(char)
        if (checkImplicitCast(varAType,Type.INT) && checkImplicitCast(castType, Type.CHAR))
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
    public static boolean checkExplicitCast(Type varAType, Type varBType, List<Type> castTypes){
        if (castTypes.size() == 1)
            return checkExplicitCast(varAType, varBType, castTypes.get(0));

        // first mandatory check
        if (!checkImplicitCast(castTypes.get(0), varBType))
            return false;

        // read NOTE above the method
        Collections.reverse(castTypes);

        int castListSize = castTypes.size();



        // save inter-results
        Type[] interResults = new Type[castListSize-1];

        Type currentType = varAType;
        for (int i=0; i < castListSize-1; i++) {

            if (checkImplicitCast(currentType, Type.INT) && checkImplicitCast(castTypes.get(i), Type.CHAR)) {
                currentType = Type.CHAR;
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
    public static boolean checkImplicitCast(Type varAType, Type varBType){
        // T = int / char

        // equal types - no casting, just true
        if (varAType.equals(varBType))
            return true;
        else if (checkImplicitConstIntChar(varAType, varBType))
            return true;
        // char/const(char) -> int/const(int)
        else if (checkImplicitConstIntChar(varAType, Type.CHAR) && checkImplicitConstIntChar(varBType, Type.INT))
            return true;
        // niz(T) -> niz(const(T))
        else if ((varAType.equals(Type.ARRAY_CHAR) && varBType.equals(Type.CONST_ARRAY_CHAR)) || (varAType.equals(Type.ARRAY_INT) && varBType.equals(Type.CONST_ARRAY_INT)))
            return true;

        // all other cases of implicit casting - UNALLOWED
        return false;
    }

    private static boolean checkImplicitConstIntChar(Type varAType, Type varBType) {
        if (varAType == varBType)
            return true;
        // const(T) -> T
        if ((varAType.equals(Type.CONST_CHAR) && varBType.equals(Type.CHAR)) || (varAType.equals(Type.CONST_INT) && varBType.equals(Type.INT)))
            return true;
        // T -> const(T)
        else if ((varAType.equals(Type.CHAR) && varBType.equals(Type.CONST_CHAR)) || (varAType.equals(Type.INT) && varBType.equals(Type.CONST_INT)))
            return true;
        return false;
    }
}
