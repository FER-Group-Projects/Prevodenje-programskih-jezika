import analizator.ENfa;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.*;

public class RegexENfaUtilTest {

    private static Set<String> testStates;
    private static Set<String> testAcceptableStates;
    private static Set<String> testActiveStates;
    private static Map<String, Map<Character, Set<String>>> testTransitions;
    private static String testStartingState;

    private static ENfa automata;
    private static Set<String> states;
    private static Set<String> acceptableStates;
    private static Set<String> activeStates;
    private static Map<String, Map<Character, Set<String>>> transitions;
    private static String startingState;

    @Before
    public void BeforeEachTest() {
        testStates = new HashSet<>();
        testAcceptableStates = new HashSet<>();
        testActiveStates = new HashSet<>();
        testTransitions = new HashMap<>();
        testStartingState = "";
    }

    private static void setTestStartingState(String state) {
        testStartingState = state;
    }

    private static void setTestStates(int count) {
        for (int i = 1; i <= count; i++) {
            testStates.add(Integer.toString(i));
        }
    }

    private static void setTestAcceptableStates(String... states) {
        Collections.addAll(testAcceptableStates, states);
    }

    private static void setTestActiveStates(String... states) {
        Collections.addAll(testActiveStates, states);
    }

    private static void getAutomataArchitecture() {
        states = automata.getAllStates();
        acceptableStates = automata.getAcceptableStates();
        activeStates = automata.getCurrentActiveStates();
        transitions = automata.getAllTransitions();
        startingState = automata.getStartingState();
    }

    private void checkAutomata() {
        assertEquals(states, testStates);
        assertEquals(acceptableStates, testAcceptableStates);
        assertEquals(activeStates, testActiveStates);
        assertEquals(transitions, testTransitions);
        assertEquals(testStartingState, startingState);
    }

    private static Set<String> toSet(String... strings) {
        Set<String> set = new HashSet<>();
        Collections.addAll(set, strings);
        return set;
    }

    private static Map<Character, Set<String>> toMap(Character trigger, String... strings) {
        Map<Character, Set<String>> map = new HashMap<>();
        map.put(trigger, toSet(strings));
        return map;
    }

    private static void addTransitions(String stateFrom, Character trigger, String... statesTo) {
        testTransitions.put(stateFrom, toMap(trigger, statesTo));
    }

    private static void addEpsilonTransitions(String stateFrom, String... statesTo) {
        testTransitions.put(stateFrom, toMap(ENfa.EPSILON, statesTo));
    }

    @Test
    public void testSplit1() {
        List<String> choices = RegexENfaUtil.findSubExpressions("(\\)a|b)\\|\\(|x*|y*");
        assertEquals(choices.size(), 3);
        assertEquals(choices.get(0), "(\\)a|b)\\|\\(");
        assertEquals(choices.get(1), "x*");
        assertEquals(choices.get(2), "y*");
    }

    @Test
    public void testSplit2() {
        List<String> choices = RegexENfaUtil.findSubExpressions("a|b");
        assertEquals(choices.size(), 2);
        assertEquals(choices.get(0), "a");
        assertEquals(choices.get(1), "b");
    }

    @Test
    public void testSplit3() {
        List<String> choices = RegexENfaUtil.findSubExpressions("x*");
        assertEquals(choices.size(), 0);
        for (String choice : choices) {
            System.out.println(choice);
        }
    }

    @Test
    public void testSplit4() {
        List<String> choices = RegexENfaUtil.findSubExpressions("(\\)a|b)\\|\\(");
        assertEquals(choices.size(), 0);
    }

    @Test
    public void testConstruct1() {
        setTestStartingState("1");
        setTestAcceptableStates("2");
        setTestStates(6);
        setTestActiveStates("1", "2", "3", "5", "6");

        addTransitions("3", 'x', "4");

        addEpsilonTransitions("1", "5");
        addEpsilonTransitions("4", "3", "6");
        addEpsilonTransitions("5", "3", "6");
        addEpsilonTransitions("6", "2");

        automata = RegexENfaUtil.regexToENKA("testConstruct1", "x*");
        getAutomataArchitecture();

        checkAutomata();
    }

    @Test
    public void testConstruct2() {
        setTestStartingState("1");
        setTestAcceptableStates("2");
        setTestStates(4);
        setTestActiveStates("1", "3");

        addTransitions("3", 'a', "4");

        addEpsilonTransitions("1", "3");
        addEpsilonTransitions("4", "2");

        automata = RegexENfaUtil.regexToENKA("testConstruct1", "a");
        getAutomataArchitecture();

        checkAutomata();
    }

    @Test
    public void testConstruct3() {
        setTestStartingState("1");
        setTestAcceptableStates("2");
        setTestStates(6);
        setTestActiveStates("1", "3");

        addTransitions("3", 'a', "4");
        addTransitions("5", 'b', "6");

        addEpsilonTransitions("1", "3");
        addEpsilonTransitions("4", "5");
        addEpsilonTransitions("6", "2");

        automata = RegexENfaUtil.regexToENKA("testConstruct3", "ab");
        getAutomataArchitecture();

        checkAutomata();
    }

    @Test
    public void testConstruct4() {
        setTestStartingState("1");
        setTestAcceptableStates("2");
        setTestStates(10);
        setTestActiveStates("1", "3", "5", "7", "9");

        addTransitions("5", 'a', "6");
        addTransitions("9", 'b', "10");

        addEpsilonTransitions("1", "3", "7");
        addEpsilonTransitions("3", "5");
        addEpsilonTransitions("4", "2");
        addEpsilonTransitions("6", "4");
        addEpsilonTransitions("7", "9");
        addEpsilonTransitions("8", "2");
        addEpsilonTransitions("10", "8");

        automata = RegexENfaUtil.regexToENKA("testConstruct4", "a|b");
        getAutomataArchitecture();

        checkAutomata();
    }

    @Test
    public void testConstruct5() {
        setTestStartingState("1");
        setTestAcceptableStates("2");
        setTestStates(12);
        setTestActiveStates("11", "1", "3", "5", "7", "9");

        addTransitions("11", 'b', "12");
        addTransitions("7", 'a', "8");

        addEpsilonTransitions("12", "10");
        addEpsilonTransitions("1", "3");
        addEpsilonTransitions("3", "5", "9");
        addEpsilonTransitions("4", "2");
        addEpsilonTransitions("5", "7");
        addEpsilonTransitions("6", "4");
        addEpsilonTransitions("8", "6");
        addEpsilonTransitions("9", "11");
        addEpsilonTransitions("10", "4");

        automata = RegexENfaUtil.regexToENKA("testConstruct5", "(a|b)");
        getAutomataArchitecture();

        checkAutomata();
    }

    @Test
    public void testConstruct6() {
        setTestStartingState("1");
        setTestAcceptableStates("2");
        setTestStates(8);
        setTestActiveStates("1", "3", "5", "6", "7");

        addTransitions("3", 'a', "4");
        addTransitions("7", 'b', "8");

        addEpsilonTransitions("1", "5");
        addEpsilonTransitions("4", "3", "6");
        addEpsilonTransitions("5", "3", "6");
        addEpsilonTransitions("6", "7");
        addEpsilonTransitions("8", "2");

        automata = RegexENfaUtil.regexToENKA("testConstruct6", "a*b");
        getAutomataArchitecture();

        checkAutomata();
    }

    @Test
    public void testConstruct7() {
        setTestStartingState("1");
        setTestAcceptableStates("2");
        setTestStates(10);
        setTestActiveStates("1", "2", "3", "5", "6", "7", "9", "10");

        addTransitions("3", 'a', "4");
        addTransitions("7", 'b', "8");

        addEpsilonTransitions("1", "5");
        addEpsilonTransitions("4", "3", "6");
        addEpsilonTransitions("5", "3", "6");
        addEpsilonTransitions("6", "9");
        addEpsilonTransitions("8", "7", "10");
        addEpsilonTransitions("9", "7", "10");
        addEpsilonTransitions("10", "2");

        automata = RegexENfaUtil.regexToENKA("testConstruct7", "a*b*");
        getAutomataArchitecture();

        checkAutomata();
    }

    @Test
    public void testConstruct8() {
        setTestStartingState("1");
        setTestAcceptableStates("2");
        setTestStates(14);
        setTestActiveStates("1", "2", "3", "4", "5", "7", "8", "9", "10", "11", "13", "14");

        addTransitions("5", 'a', "6");
        addTransitions("11", 'b', "12");

        addEpsilonTransitions("1", "3", "9");
        addEpsilonTransitions("3", "7");
        addEpsilonTransitions("4", "2");
        addEpsilonTransitions("6", "5", "8");
        addEpsilonTransitions("7", "5", "8");
        addEpsilonTransitions("8", "4");
        addEpsilonTransitions("9", "13");
        addEpsilonTransitions("10", "2");
        addEpsilonTransitions("12", "11", "14");
        addEpsilonTransitions("13", "11", "14");
        addEpsilonTransitions("14", "10");

        automata = RegexENfaUtil.regexToENKA("testConstruct8", "a*|b*");
        getAutomataArchitecture();

        checkAutomata();
    }

    @Test
    public void testConstruct9() {
        setTestStartingState("1");
        setTestAcceptableStates("2");
        setTestStates(18);
        setTestActiveStates("1", "3", "5", "7", "9", "11", "13", "15", "17");

        addTransitions("13", 'b', "14");
        addTransitions("17", 'c', "18");
        addTransitions("9", 'a', "10");

        addEpsilonTransitions("1", "3", "15");
        addEpsilonTransitions("3", "5");
        addEpsilonTransitions("4", "2");
        addEpsilonTransitions("5", "11", "7");
        addEpsilonTransitions("6", "4");
        addEpsilonTransitions("7", "9");
        addEpsilonTransitions("8", "6");
        addEpsilonTransitions("10", "8");
        addEpsilonTransitions("11", "13");
        addEpsilonTransitions("12", "6");
        addEpsilonTransitions("14", "12");
        addEpsilonTransitions("15", "17");
        addEpsilonTransitions("16", "2");
        addEpsilonTransitions("18", "16");

        automata = RegexENfaUtil.regexToENKA("testConstruct9", "(a|b)|c");
        getAutomataArchitecture();

        checkAutomata();
    }

    @Test
    public void testConstruct10() {
        setTestStartingState("1");
        setTestAcceptableStates("2");
        setTestStates(2);
        setTestActiveStates("1", "2");

        addEpsilonTransitions("1", "2");

        automata = RegexENfaUtil.regexToENKA("testConstruct10", "");
        getAutomataArchitecture();

        checkAutomata();
    }

    @Test
    public void testConstruct11() {
        setTestStartingState("1");
        setTestAcceptableStates("2");
        setTestStates(10);
        setTestActiveStates("1", "2", "3", "5", "7", "8", "9", "10");

        addTransitions("5", 'a', "6");

        addEpsilonTransitions("1", "3", "7");
        addEpsilonTransitions("3", "5");
        addEpsilonTransitions("4", "2");
        addEpsilonTransitions("6", "4");
        addEpsilonTransitions("7", "9");
        addEpsilonTransitions("8", "2");
        addEpsilonTransitions("9", "10");
        addEpsilonTransitions("10", "8");

        automata = RegexENfaUtil.regexToENKA("testConstruct10", "a|" + ENfa.EPSILON);
        getAutomataArchitecture();

        checkAutomata();
    }
}
