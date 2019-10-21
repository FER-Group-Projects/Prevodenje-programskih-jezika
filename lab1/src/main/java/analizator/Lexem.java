import java.util.Objects;

public class Lexem {

    private final String tokenType;
    private final int lineNumber;
    private final String token;

    public Lexem(String tokenType, int lineNumber, String token) {
        this.tokenType = Objects.requireNonNull(tokenType, "Token type cannot be null.");
        this.lineNumber = lineNumber;
        this.token = Objects.requireNonNull(token, "Token cannot be null.");
    }

    public String getTokenType() {
        return tokenType;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return tokenType + " " + lineNumber + " " + token;
    }

}
