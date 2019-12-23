import java.util.ArrayList;
import java.util.List;

public class PostfiksIzraz extends Node {

    @Override
    public Node analyze() {
        if (rightSideType == -1) determineRightSideType();


        switch (rightSideType) {
            case 0:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(0);
                } else {
                    Properties rightSideCharProperties = rightSide.get(0).properties;
                    properties.setTip(rightSideCharProperties.getTip());
                    properties.setlIzraz(rightSideCharProperties.getlIzraz());
                }
                break;
            case 1:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(0);
                } else if (currentRightSideIndex == 1) {
                    currentRightSideIndex++;
                    Type postfiksIzrazTip = rightSide.get(0).properties.getTip();
                    if (!(postfiksIzrazTip == Type.ARRAY_INT || postfiksIzrazTip == Type.CONST_ARRAY_INT || postfiksIzrazTip == Type.ARRAY_CHAR || postfiksIzrazTip == Type.CONST_ARRAY_CHAR))
                        errorHappened();
                    return rightSide.get(2);
                } else if (currentRightSideIndex == 2) {
                    currentRightSideIndex += 2;
                    Type izrazTip = rightSide.get(2).properties.getTip();
                    if (!Checkers.checkImplicitCast(izrazTip, Type.INT))
                        errorHappened();

                    Properties rightSideCharProperties = rightSide.get(0).properties;
                    Type postfiksIzrazTip = rightSideCharProperties.getTip();
                    Type typeToSet = null;
                    switch (postfiksIzrazTip) {
                        case ARRAY_INT:
                            typeToSet = Type.INT;
                            break;
                        case CONST_ARRAY_INT:
                            typeToSet = Type.CONST_INT;
                            break;
                        case ARRAY_CHAR:
                            typeToSet = Type.CHAR;
                            break;
                        case CONST_ARRAY_CHAR:
                            typeToSet = Type.CONST_CHAR;
                            break;
                    }
                    properties.setTip(typeToSet);
                    boolean notConstPrefixedTypeToSet = typeToSet != Type.CONST_CHAR && typeToSet != Type.CONST_INT;
                    properties.setlIzraz(notConstPrefixedTypeToSet ? 1 : 0);
                }
                break;
            case 2:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex++;
                    return rightSide.get(0);
                } else if (currentRightSideIndex == 1) {
                    Properties rightSideCharProperties = rightSide.get(0).properties;

                    //TODO: check assumption: pov = rightSideCharProperties.pov ?
                    if (!(rightSideCharProperties.getTip() == Type.FUNCTION && rightSideCharProperties.getTipovi().equals(new ArrayList<>())))
                        errorHappened();

                    //TODO: assumption: pov = rightSideCharProperties.pov ?
                    properties.setTip(rightSideCharProperties.getTip());
                    properties.setlIzraz(0);
                }

                break;
            case 3:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex += 2;
                    return rightSide.get(0);
                } else if (currentRightSideIndex == 2) {
                    currentRightSideIndex += 2;
                    return rightSide.get(2);
                } else {
                    Properties rightSideCharProperties = rightSide.get(2).properties;
                    List<Type> argTypes = rightSideCharProperties.getTipovi();
                    Type argListType = rightSideCharProperties.getTip();

                    if (argListType != Type.FUNCTION)
                        errorHappened();

                    for (Type argType : argTypes) {
                        if (!Checkers.checkImplicitCast(argType,argListType))
                            errorHappened();

                    }
                    //TODO: check assumption: pov = rightSideCharProperties.pov ?

                    //TODO: assumption: pov = rightSideCharProperties.pov ?
                    properties.setTip(rightSideCharProperties.getTip());
                    properties.setlIzraz(0);
                }
                break;
            case 4: case 5:
                if (currentRightSideIndex == 0) {
                    currentRightSideIndex += 2;
                    return rightSide.get(0);
                } else {
                    Properties rightSideCharProperties = rightSide.get(0).properties;
                    if (!(rightSideCharProperties.getlIzraz() == 1 && Checkers.checkImplicitCast(rightSideCharProperties.getTip(), Type.INT)))
                        errorHappened();
                }
                break;
        }
        return null;
    }

    @Override
    public String toText() {
        return LeftSideNames.POSTFIKS_IZRAZ;
    }

    @Override
    public void determineRightSideType() {
        int len = rightSide.size();
        if (len == 1) {
            rightSideType = 0;
        } else if (len == 4) {
            String idn1 = ((UniformCharacter) rightSide.get(1)).getIdentifier();
            String idn2 = ((UniformCharacter) rightSide.get(3)).getIdentifier();
            if (idn1.equals(Identifiers.L_UGL_ZAGRADA) && idn2.equals(Identifiers.D_UGL_ZAGRADA))
                rightSideType = 1;
            else if (idn1.equals(Identifiers.L_ZAGRADA) && idn2.equals(Identifiers.D_ZAGRADA))
                rightSideType = 3;
        } else if (len == 3) {
            String idn1 = ((UniformCharacter) rightSide.get(1)).getIdentifier();
            String idn2 = ((UniformCharacter) rightSide.get(2)).getIdentifier();
            if (idn1.equals(Identifiers.L_ZAGRADA) && idn2.equals(Identifiers.D_ZAGRADA))
                rightSideType = 2;
        } else if (len == 2) {
            String idn = ((UniformCharacter) rightSide.get(1)).getIdentifier();
            if (idn.equals(Identifiers.OP_INC))
                rightSideType = 4;
            else if (idn.equals(Identifiers.OP_DEC))
                rightSideType = 5;
        }

    }
}

