package analizator;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UCStreamTest {

    @Test
    public void testFileLoading() throws IOException {
        Path inputProgramPath = Paths.get("src/test/resources/test_examples/10minusLang_1/test.in");
        String inputProgram = new String(Files.readAllBytes(inputProgramPath), "UTF8");

        InputStream is = new FileInputStream("src/test/resources/test_examples/10minusLang_1/test.in");
        UniformCharacterStream ucstream = new UniformCharacterStream(is);

        while (ucstream.hasNext()) {
            System.out.println(ucstream.getNext());
        }
        //System.out.println(inputProgram);
    }
}
