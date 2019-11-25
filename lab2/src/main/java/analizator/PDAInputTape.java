package analizator;

import java.io.InputStream;

public class PDAInputTape {
    private UniformCharacterStream stream;

    public PDAInputTape(InputStream stream) {
        this.stream = new UniformCharacterStream(stream);
    }
}
