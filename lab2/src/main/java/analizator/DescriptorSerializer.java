import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DescriptorSerializer {
    public static final String GENERATOR_PATH_TO_SA_DESCRIPTOR = "analizator/sa-descriptor";
    public static final String ANALYZER_PATH_TO_SA_DESCRIPTOR = "sa-descriptor";

    public static void serialize(SADescriptor descriptor)throws IOException{
        FileOutputStream fileOutputStream = new FileOutputStream(GENERATOR_PATH_TO_SA_DESCRIPTOR);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(descriptor);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    public static SADescriptor deserialize(){
        Path path = Paths.get(ANALYZER_PATH_TO_SA_DESCRIPTOR);

        if (Files.notExists(path)) {
            throw new IllegalArgumentException("File " + ANALYZER_PATH_TO_SA_DESCRIPTOR + " does not exist.");
        }

        try (FileInputStream fis = new FileInputStream(ANALYZER_PATH_TO_SA_DESCRIPTOR);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (SADescriptor) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
