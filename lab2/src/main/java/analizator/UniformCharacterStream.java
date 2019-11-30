import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class UniformCharacterStream {
    private InputStream stream;
    private ArrayList<UniformCharacter> lines = new ArrayList<>();

    private int currentindex = 0;

    public UniformCharacterStream(InputStream stream) {
        this.stream = stream;
        read();
    }

    private void read() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        try {
            while (reader.ready()) {
                String line = reader.readLine();
                String[] temp = line.split(" ");
                lines.add(new UniformCharacter(new Symbol(temp[0], true), Integer.parseInt(temp[1]),
                        line.substring(temp[0].length() + temp[1].length() + 2)));
            }
            lines.add(new UniformCharacter(Symbol.TAPE_END, 0, Symbol.TAPE_END.getSymbol()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UniformCharacter getNext() {
        UniformCharacter c = lines.get(currentindex);
        currentindex++;
        return c;
    }

    public UniformCharacter getCurrent() {
        return lines.get(currentindex);
    }

    public void step() {
        currentindex++;
    }

    public boolean hasNext() {
        return currentindex < lines.size();
    }

}
