import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FRISCDocumentWriter {
	
	private static FRISCDocumentWriter fdw;
	
	public static FRISCDocumentWriter getFRISCDocumentWriter() {
		if(fdw==null) {
			fdw = new FRISCDocumentWriter();
		}
		return fdw;
	}
	
	StringBuilder sb = new StringBuilder();
	
	private FRISCDocumentWriter() {
		initStart();
		initMul();
		initDiv();
	}

	private void initStart() {
		sb.append("\tMOVE 40000, R7\n" +
				  "\tCALL F_MAIN\n" +
				  "\tHALT\n");
	}

	private void initMul() {
		sb.append("\nMUL\tPUSH R1\n" + 
				"\tPUSH R2\n" + 
				"\tPUSH R3\n" + 
				"\tMOVE 0, R2\n" + 
				"\tMOVE 1, R3\n" + 
				"L1\tADD R0, R2, R2\n" + 
				"\tSUB R1, R3, R1\n" + 
				"\tJP_NZ L1\n" + 
				"\tMOVE R2, R0\n" + 
				"\tPOP R3\n" + 
				"\tPOP R2\n" + 
				"\tPOP R1\n" + 
				"\tRET\n");
	}

	private void initDiv() {
		sb.append("\nDIV\tPUSH R1\n" + 
				"\tPUSH R2\n" + 
				"\tPUSH R3\n" + 
				"\tMOVE 0, R2\n" + 
				"\tMOVE 1, R3\n" + 
				"L2\tSUB R0, R1, R0\n" + 
				"\tJP_N JR\n" + 
				"\tADD R2, R3, R2\n" + 
				"\tJP_Z JR\n" + 
				"\tJP L2\n" + 
				"JR\tMOVE R2, R0\n" + 
				"\tPOP R3\n" + 
				"\tPOP R2\n" + 
				"\tPOP R1\n" + 
				"\tRET\n\n");
	}
	
	public void add(String label, String instruction) {
		if(label.isEmpty() && instruction.isEmpty()) {
			sb.append("\n");
			return;
		}
		sb.append(label + "\t" + instruction + "\n");
	}
	
	public void write(String fileName) {
		try {
			Path friscFilePath = Paths.get(GeneratorKoda.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			friscFilePath = friscFilePath.resolve(fileName);
			Files.writeString(friscFilePath, sb.toString(), StandardCharsets.UTF_8, StandardOpenOption.CREATE);
		} catch (IOException | URISyntaxException e) {
			System.err.println(e.getClass().getCanonicalName() + " : " + e.getMessage());
		}
	}

}
