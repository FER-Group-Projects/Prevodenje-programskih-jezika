import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

class CheckersTest {

    private void testImplicitCasts(Type from, EnumSet<Type> allowed) {
        EnumSet<Type> notAllowed = EnumSet.complementOf(allowed);

        for (Type allowedImplicitCast : allowed) {
            assertTrue(Checkers.checkImplicitCast(from, allowedImplicitCast),
                    "Implicit cast from " + from + " to " + allowedImplicitCast );
        }

        for (Type notAllowedImplicitCast : notAllowed) {
            assertFalse(Checkers.checkImplicitCast(from, notAllowedImplicitCast),
                    "Implicit cast from " + from + " to " + notAllowedImplicitCast + " should not be allowed.");
        }
    }

    @Test
    void checkImplicitCastCharToInt() {
        assertTrue(Checkers.checkImplicitCast(Type.CHAR, Type.INT));
    }

    @Test
    void checkImplicitCastIntToChar() {
        assertFalse(Checkers.checkImplicitCast(Type.INT, Type.CHAR));
    }

    @Test
    void checkImplicitCastInt() {
        testImplicitCasts(Type.INT, EnumSet.of(Type.INT, Type.CONST_INT));
    }

    @Test
    void checkImplicitCastConstInt() {
        testImplicitCasts(Type.CONST_INT, EnumSet.of(Type.INT, Type.CONST_INT));
    }

    @Test
    void checkImplicitCastChar() {
        testImplicitCasts(Type.CHAR, EnumSet.of(Type.INT, Type.CONST_INT, Type.CHAR, Type.CONST_CHAR));
    }

    @Test
    void checkImplicitCastConstChar() {
        testImplicitCasts(Type.CONST_CHAR, EnumSet.of(Type.INT, Type.CONST_INT, Type.CHAR, Type.CONST_CHAR));
    }

    @Test
    void checkImplicitCastArrayChar() {
        testImplicitCasts(Type.ARRAY_CHAR, EnumSet.of(Type.ARRAY_CHAR, Type.CONST_ARRAY_CHAR));
    }

    @Test
    void checkImplicitCastConstArrayChar() {
        testImplicitCasts(Type.CONST_ARRAY_CHAR, EnumSet.of(Type.CONST_ARRAY_CHAR));
    }

    @Test
    void checkImplicitCastArrayInt() {
        testImplicitCasts(Type.ARRAY_INT, EnumSet.of(Type.ARRAY_INT, Type.CONST_ARRAY_INT));
    }

    @Test
    void checkImplicitCastConstArrayInt() {
        testImplicitCasts(Type.CONST_ARRAY_INT, EnumSet.of(Type.CONST_ARRAY_INT));
    }

    @Test
    void checkImplicitCastVoid() {
        testImplicitCasts(Type.VOID, EnumSet.of(Type.VOID));
    }

    @Test
    void checkImplicitCastFunction() {
        testImplicitCasts(Type.FUNCTION, EnumSet.of(Type.FUNCTION));
    }

}