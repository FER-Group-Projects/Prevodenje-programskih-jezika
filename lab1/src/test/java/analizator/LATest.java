package analizator;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class LATest {

	private static final String S_POCETNO = "S_pocetno";
	private static final String S_KOMENTAR = "S_komentar";
	private static final String S_UNARNI = "S_unarni";

	private static final char[] ZNAMENKA = "0123456789".toCharArray();
	private static final char[] HEX_ZNAMENKA = "0123456789abcdefABCDEF".toCharArray();
	private static final char[] BJELINA = "\t\n ".toCharArray();
	private static final char[] SVI_ZNAKOVI = "(){}*\\$\t\n !\"#%&'+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz~".toCharArray();

	private Map<String, Map<ENfa, Action>> transitions;

	private static int actionNumber = 0;

	@Before
	public void beforeEachTest() {
		transitions = new HashMap<>();

		transitions.put(S_POCETNO, new HashMap<ENfa, Action>());
		transitions.put(S_KOMENTAR, new HashMap<ENfa, Action>());
		transitions.put(S_UNARNI, new HashMap<ENfa, Action>());

		// Match \t|\_
		transitions.get(S_POCETNO).put(matchSingleCharacter('\t', ' '), defaultAction());

		// Match \n
		transitions.get(S_POCETNO).put(matchSingleCharacter('\n'), newLineAction());

		// Match #|
		transitions.get(S_POCETNO).put(matchSequenceOfCharacters("#|"), enterStateAction(S_KOMENTAR));

		// Match |#
		transitions.get(S_KOMENTAR).put(matchSequenceOfCharacters("|#"), enterStateAction(S_POCETNO));

		// Match \n
		transitions.get(S_KOMENTAR).put(matchSingleCharacter('\n'), newLineAction());

		// Match {sviZnakovi}
		transitions.get(S_KOMENTAR).put(matchSingleCharacter(SVI_ZNAKOVI), defaultAction());

		// Match {broj}
		transitions.get(S_POCETNO).put(matchNumber(), tokenTypeAction("OPERAND"));

		// Match (
		transitions.get(S_POCETNO).put(matchSingleCharacter('('), tokenTypeAction("LIJEVA_ZAGRADA"));

		// Match )
		transitions.get(S_POCETNO).put(matchSingleCharacter(')'), tokenTypeAction("DESNA_ZAGRADA"));

		// Match -
		transitions.get(S_POCETNO).put(matchSingleCharacter('-'), tokenTypeAction("OP_MINUS"));

		// Match -{bjelina}*-
		ENfa enfa1 = new ENfa("-{bjelina}*-", "q0", false);

		enfa1.addState("q1", false);
		enfa1.addState("q2", true);

		enfa1.addTransition("q0", '-', "q1");

		for (char character : BJELINA) enfa1.addTransition("q1", character, "q1");
		enfa1.addTransition("q1", '-', "q2");

		Action action1 = new Action();

		action1.tokenType = "OP_MINUS";
		action1.enterState = S_UNARNI;
		action1.goBack = 1;
		action1.ordinalNumber = actionNumber++;

		transitions.get(S_POCETNO).put(enfa1, action1);

		// Match ({bjelina}*-

		ENfa enfa2 = new ENfa("({bjelina}*-", "q0", false);

		enfa2.addState("q1", false);
		enfa2.addState("q2", true);

		enfa2.addTransition("q0", '(', "q1");

		for (char character : BJELINA) enfa2.addTransition("q1", character, "q1");
		enfa2.addTransition("q1", '-', "q2");

		Action action2 = new Action();

		action2.tokenType = "LIJEVA_ZAGRADA";
		action2.enterState = S_UNARNI;
		action2.goBack = 1;
		action2.ordinalNumber = actionNumber++;

		transitions.get(S_POCETNO).put(enfa2, action2);

		// Match \t|\_
		transitions.get(S_UNARNI).put(matchSingleCharacter('\t', ' '), defaultAction());

		// Match \n
		transitions.get(S_UNARNI).put(matchSingleCharacter('\n'), newLineAction());

		// Match -
		Action action3 = new Action();

		action3.tokenType = "UMINUS";
		action3.enterState = S_POCETNO;
		action3.ordinalNumber = actionNumber++;

		transitions.get(S_UNARNI).put(matchSingleCharacter('-'), action3);

		// Match -{bjelina}*-
		Action action4 = new Action();

		action4.tokenType = "UMINUS";
		action4.goBack = 1;
		action4.ordinalNumber = actionNumber++;

		transitions.get(S_UNARNI).put(enfa1, action4);
	}

	private ENfa matchSingleCharacter(char... charactersToMatch) {
		ENfa enfa = new ENfa(charactersToMatch.toString(), "q0", false);

		enfa.addState("q1", true);

		for (char character : charactersToMatch) {
			enfa.addTransition("q0", character, "q1");
		}

		return enfa;
	}

	private ENfa matchSequenceOfCharacters(String charactersToMatch) {
		ENfa enfa = new ENfa(charactersToMatch, "q0", false);
		int stateNumber = 1;

		for (char character : charactersToMatch.toCharArray()) {
			enfa.addState("q" + stateNumber, false);
			enfa.addTransition("q" + (stateNumber - 1), character, "q" + stateNumber);

			++stateNumber;
		}

		enfa.setStateAcceptability("q" + (stateNumber - 1), true);

		return enfa;
	}

	private ENfa matchNumber() {
		ENfa enfa = new ENfa("{broj}", "q0", false);

		enfa.addState("q1", true);

		for (char character : ZNAMENKA) {
			enfa.addTransition("q0", character, "q1");
			enfa.addTransition("q1", character, "q1");
		}

		enfa.addState("q3", false);
		enfa.addState("q4", false);
		enfa.addState("q5", true);

		enfa.addTransition("q0", '0', "q3");
		enfa.addTransition("q3", 'x', "q4");

		for (char character : HEX_ZNAMENKA) {
			enfa.addTransition("q4", character, "q5");
			enfa.addTransition("q5", character, "q5");
		}

		return enfa;
	}

	private Action defaultAction() {
		Action action = new Action();

		action.ordinalNumber = actionNumber++;

		return action;
	}

	private Action newLineAction() {
		Action action = new Action();

		action.newLine = true;
		action.ordinalNumber = actionNumber++;

		return action;
	}

	private Action enterStateAction(String state) {
		Action action = new Action();

		action.enterState = state;
		action.ordinalNumber = actionNumber++;

		return action;
	}

	private Action tokenTypeAction(String tokenType) {
		Action action = new Action();

		action.tokenType = tokenType;
		action.ordinalNumber = actionNumber++;

		return action;
	}

	@Test
	public void testAnalyzingTestFile() throws URISyntaxException, IOException {
		Path inputProgramPath = Paths.get("src/test/resources/primjer.minus");
		String inputProgram = new String(Files.readAllBytes(inputProgramPath), "UTF8");

		Path outputPath = Paths.get("src/test/resources/minus_primjer_izlaz.txt");
		String output = new String(Files.readAllBytes(outputPath), "UTF8").trim();

		LA la = new LA(S_POCETNO, transitions, inputProgram);
		StringBuilder sb = new StringBuilder();

		for (Lexem lexem : la) if (lexem != null) sb.append(lexem.toString()).append('\n');

		assertEquals(output, sb.toString().trim());
	}

}
