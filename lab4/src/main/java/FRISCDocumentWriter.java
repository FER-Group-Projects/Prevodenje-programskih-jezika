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
		init();
	}

	
	
	private void init() {
		sb.append("\\tMOVE 40000, R7\\n\\tCALL F_MAIN\\n\\tHALT\\n\\n; f: (R0, R1) -> (R6=R0*R1)\\nMUL\\tPUSH R0\\n\\tPUSH R1\\n\\tPUSH R2\\n\\tPUSH R3\\n\\tPUSH R4\\n\\tMOVE 0, R2\\n\\tMOVE 1, R3\\n\\tMOVE 0, R4\\t\\t\\t; preparing R4 (if R0 NEGATIVE and R1 POSITIVE or R0 POSITIVE and R1 NEGATIVE => R4=1, R4=0 otherwise)\\n\\tOR R0, R4, R4\\n\\tXOR R1, R4, R4\\n\\tROTL R4, 1, R4\\n\\tAND R4, 1, R4\\n\\tCMP R0, 0\\t\\t\\t; R0 = abs(R0)\\n\\tJP_SGT C1\\n\\tJP_EQ E1\\n\\tSUB R2, R0, R0\\nC1\\tCMP R1, 0\\t\\t\\t; R1 = abs(R1)\\n\\tJP_SGT L1\\n\\tJP_EQ E1\\n\\tSUB R2, R1, R1\\nL1\\tADD R0, R2, R2\\n\\tSUB R1, R3, R1\\n\\tJP_NZ L1\\n\\tCMP R4, 1\\t\\t\\t; if R4==1 => negate the output, do nothing otherwise\\n\\tJP_NE E1\\n\\tMOVE 0, R4\\n\\tSUB R4, R2, R2\\nE1\\tMOVE R2, R6\\n\\tPOP R4\\n\\tPOP R3\\n\\tPOP R2\\n\\tPOP R1\\n\\tPOP R0\\n\\tRET\\n\\t\\n; f: (R0, R1) -> (R6=R0/R1)\\nDIV\\tPUSH R0\\n\\tPUSH R1\\n\\tPUSH R2\\n\\tPUSH R3\\n\\tPUSH R4\\n\\tMOVE 0, R2\\n\\tMOVE 1, R3\\n\\tMOVE 0, R4\\t\\t\\t; preparing R4 (if R0 NEGATIVE and R1 POSITIVE or R0 POSITIVE and R1 NEGATIVE => R4=1, R4=0 otherwise)\\n\\tOR R0, R4, R4\\n\\tXOR R1, R4, R4\\n\\tROTL R4, 1, R4\\n\\tAND R4, 1, R4\\n\\tCMP R0, 0\\t\\t\\t; R0 = abs(R0)\\n\\tJP_SGT C2\\n\\tJP_EQ E22\\n\\tSUB R2, R0, R0\\nC2\\tCMP R1, 0\\t\\t\\t; R1 = abs(R1)\\n\\tJP_SGT L2\\n\\tJP_EQ E22\\n\\tSUB R2, R1, R1\\nL2\\tSUB R0, R1, R0\\n\\tJP_N E21\\n\\tADD R2, R3, R2\\n\\tJP L2\\nE21\\tCMP R4, 1\\t\\t\\t; if R4==1 => negate the output, do nothing otherwise\\n\\tJP_NE E22\\n\\tMOVE 0, R4\\n\\tSUB R4, R2, R2\\nE22\\tMOVE R2, R6\\n\\tPOP R4\\n\\tPOP R3\\n\\tPOP R2\\n\\tPOP R1\\n\\tPOP R0\\n\\tRET\\n\\n; f: (R0, R1) -> (R6=R0%R1), CONDITION : R0>=0 && R1>0\\nMOD\\tPUSH R0\\nL3\\tSUB R0, R1, R0\\n\\tCMP R0, 0\\n\\tJP_SGT L3\\n\\tJP_EQ E3\\n\\tADD R0, R1, R0\\nE3\\tMOVE R0, R6\\n\\tPOP R0\\n\\tRET\\n\\n");
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
			Files.write(friscFilePath, sb.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
		} catch (IOException | URISyntaxException e) {
			System.err.println(e.getClass().getCanonicalName() + " : " + e.getMessage());
		}
	}

}
