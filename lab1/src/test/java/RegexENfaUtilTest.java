import analizator.ENfa;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.*;

public class RegexENfaUtilTest {

    public static Set<String> toSet(String... strings) {
        Set set = new HashSet();
        for (String s : strings) {
            set.add(s);
        }
        return set;
    }

    public static Map<Character, Set<String>> toMap(Character trigger, String... strings) {
        Map map = new HashMap();
        map.put(trigger, toSet(strings));
        return map;
    }

    public static void addTransitions(Map<String, Map<Character, Set<String>>> transitions, String stateFrom, Character trigger, String... statesTo) {
        transitions.put(stateFrom, toMap(trigger, statesTo));
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
        Set<String> testStates = new HashSet<>();
        Set<String> testAcceptableStates = new HashSet<>();
        Set<String> testActiveStates = new HashSet<>();
        Map<String, Map<Character, Set<String>>> testTransitions = new HashMap<>();
        String testStartingState = "1";
        testAcceptableStates.add("2");
        for (int i = 1; i <= 6; i++) {
            testStates.add(Integer.toString(i));
        }
        testActiveStates.add("1");
        testActiveStates.add("2");
        testActiveStates.add("3");
        testActiveStates.add("5");
        testActiveStates.add("6");

        addTransitions(testTransitions, "1", ENfa.EPSILON, "5");
        addTransitions(testTransitions, "3", 'x', "4");
        addTransitions(testTransitions, "4", ENfa.EPSILON, "3", "6");
        addTransitions(testTransitions, "5", ENfa.EPSILON, "3", "6");
        addTransitions(testTransitions, "6", ENfa.EPSILON, "2");

        ENfa automata = RegexENfaUtil.regexToENKA("testConstruct1", "x*");
        Set<String> states = automata.getAllStates();
        Set<String> acceptableStates = automata.getAcceptableStates();
        Set<String> activeStates = automata.getCurrentActiveStates();
        Map<String, Map<Character, Set<String>>> transitions = automata.getAllTransitions();

        assertTrue(states.equals(testStates));
        assertTrue(acceptableStates.equals(testAcceptableStates));
        assertTrue(activeStates.equals(testActiveStates));
        assertTrue(transitions.equals(testTransitions));
        assertTrue(testStartingState.equals(automata.getStartingState()));

    }

    @Ignore
    @Test
    public void testConstruct2() {
        ENfa automata = RegexENfaUtil.regexToENKA("testConstruct2", "a");
        assertEquals(automata.toString(), "testConstruct2\n" +
                "States:\n" +
                "1\n" +
                "2 true\n" +
                "3\n" +
                "4\n" +
                "Starting:\n" +
                "1\n" +
                "Transitions:\n" +
                "1 " + ENfa.EPSILON + " 3\n" +
                "3 a 4\n" +
                "4 " + ENfa.EPSILON + " 2\n" +
                "Active:\n" +
                "1\n" +
                "3\n");
    }

    @Ignore
    @Test
    public void testConstruct3() {
        ENfa automata = RegexENfaUtil.regexToENKA("testConstruct3", "ab");
        assertEquals(automata.toString(), "testConstruct3\n" +
                "States:\n" +
                "1\n" +
                "2 true\n" +
                "3\n" +
                "4\n" +
                "5\n" +
                "6\n" +
                "Starting:\n" +
                "1\n" +
                "Transitions:\n" +
                "1 " + ENfa.EPSILON + " 3\n" +
                "3 a 4\n" +
                "4 " + ENfa.EPSILON + " 5\n" +
                "5 b 6\n" +
                "6 " + ENfa.EPSILON + " 2\n" +
                "Active:\n" +
                "1\n" +
                "3\n");
    }

    @Ignore
    @Test
    public void testConstruct4() {
        ENfa automata = RegexENfaUtil.regexToENKA("testConstruct4", "a|b");
        assertEquals(automata.toString(), "testConstruct4\n" +
                "States:\n" +
                "1\n" +
                "2 true\n" +
                "3\n" +
                "4\n" +
                "5\n" +
                "6\n" +
                "7\n" +
                "8\n" +
                "9\n" +
                "10\n" +
                "Starting:\n" +
                "1\n" +
                "Transitions:\n" +
                "1 " + ENfa.EPSILON + " 3\n" +
                "1 " + ENfa.EPSILON + " 7\n" +
                "3 " + ENfa.EPSILON + " 5\n" +
                "4 " + ENfa.EPSILON + " 2\n" +
                "5 a 6\n" +
                "6 " + ENfa.EPSILON + " 4\n" +
                "7 " + ENfa.EPSILON + " 9\n" +
                "8 " + ENfa.EPSILON + " 2\n" +
                "9 b 10\n" +
                "10 " + ENfa.EPSILON + " 8\n" +
                "Active:\n" +
                "1\n" +
                "3\n" +
                "5\n" +
                "7\n" +
                "9\n");
    }

    @Ignore
    @Test
    public void testConstruct5() {
        ENfa automata = RegexENfaUtil.regexToENKA("testConstruct5", "(a|b)");
        assertEquals(automata.toString(), "testConstruct5\n" +
                "States:\n" +
                "11\n" +
                "1\n" +
                "12\n" +
                "2 true\n" +
                "3\n" +
                "4\n" +
                "5\n" +
                "6\n" +
                "7\n" +
                "8\n" +
                "9\n" +
                "10\n" +
                "Starting:\n" +
                "1\n" +
                "Transitions:\n" +
                "11 b 12\n" +
                "12 " + ENfa.EPSILON + " 10\n" +
                "1 " + ENfa.EPSILON + " 3\n" +
                "3 " + ENfa.EPSILON + " 5\n" +
                "3 " + ENfa.EPSILON + " 9\n" +
                "4 " + ENfa.EPSILON + " 2\n" +
                "5 " + ENfa.EPSILON + " 7\n" +
                "6 " + ENfa.EPSILON + " 4\n" +
                "7 a 8\n" +
                "8 " + ENfa.EPSILON + " 6\n" +
                "9 " + ENfa.EPSILON + " 11\n" +
                "10 " + ENfa.EPSILON + " 4\n" +
                "Active:\n" +
                "11\n" +
                "1\n" +
                "3\n" +
                "5\n" +
                "7\n" +
                "9\n");
    }

    @Ignore
    @Test
    public void testConstruct6() {
        ENfa automata = RegexENfaUtil.regexToENKA("testConstruct6", "a*b");
        assertEquals(automata.toString(), "testConstruct6\n" +
                "States:\n" +
                "1\n" +
                "2 true\n" +
                "3\n" +
                "4\n" +
                "5\n" +
                "6\n" +
                "7\n" +
                "8\n" +
                "Starting:\n" +
                "1\n" +
                "Transitions:\n" +
                "1 " + ENfa.EPSILON + " 5\n" +
                "3 a 4\n" +
                "4 " + ENfa.EPSILON + " 3\n" +
                "4 " + ENfa.EPSILON + " 6\n" +
                "5 " + ENfa.EPSILON + " 3\n" +
                "5 " + ENfa.EPSILON + " 6\n" +
                "6 " + ENfa.EPSILON + " 7\n" +
                "7 b 8\n" +
                "8 " + ENfa.EPSILON + " 2\n" +
                "Active:\n" +
                "1\n" +
                "3\n" +
                "5\n" +
                "6\n" +
                "7\n");
    }

    @Ignore
    @Test
    public void testConstruct7() {
        ENfa automata = RegexENfaUtil.regexToENKA("testConstruct7", "a*b*");
        assertEquals(automata.toString(), "testConstruct7\n" +
                "States:\n" +
                "1\n" +
                "2 true\n" +
                "3\n" +
                "4\n" +
                "5\n" +
                "6\n" +
                "7\n" +
                "8\n" +
                "9\n" +
                "10\n" +
                "Starting:\n" +
                "1\n" +
                "Transitions:\n" +
                "1 " + ENfa.EPSILON + " 5\n" +
                "3 a 4\n" +
                "4 " + ENfa.EPSILON + " 3\n" +
                "4 " + ENfa.EPSILON + " 6\n" +
                "5 " + ENfa.EPSILON + " 3\n" +
                "5 " + ENfa.EPSILON + " 6\n" +
                "6 " + ENfa.EPSILON + " 9\n" +
                "7 b 8\n" +
                "8 " + ENfa.EPSILON + " 7\n" +
                "8 " + ENfa.EPSILON + " 10\n" +
                "9 " + ENfa.EPSILON + " 7\n" +
                "9 " + ENfa.EPSILON + " 10\n" +
                "10 " + ENfa.EPSILON + " 2\n" +
                "Active:\n" +
                "1\n" +
                "2\n" +
                "3\n" +
                "5\n" +
                "6\n" +
                "7\n" +
                "9\n" +
                "10\n");
    }

    @Ignore
    @Test
    public void testConstruct8() {
        ENfa automata = RegexENfaUtil.regexToENKA("testConstruct8", "a*|b*");
        assertEquals(automata.toString(), "testConstruct8\n" +
                "States:\n" +
                "11\n" +
                "12\n" +
                "13\n" +
                "14\n" +
                "1\n" +
                "2 true\n" +
                "3\n" +
                "4\n" +
                "5\n" +
                "6\n" +
                "7\n" +
                "8\n" +
                "9\n" +
                "10\n" +
                "Starting:\n" +
                "1\n" +
                "Transitions:\n" +
                "11 b 12\n" +
                "12 " + ENfa.EPSILON + " 11\n" +
                "12 " + ENfa.EPSILON + " 14\n" +
                "13 " + ENfa.EPSILON + " 11\n" +
                "13 " + ENfa.EPSILON + " 14\n" +
                "14 " + ENfa.EPSILON + " 10\n" +
                "1 " + ENfa.EPSILON + " 3\n" +
                "1 " + ENfa.EPSILON + " 9\n" +
                "3 " + ENfa.EPSILON + " 7\n" +
                "4 " + ENfa.EPSILON + " 2\n" +
                "5 a 6\n" +
                "6 " + ENfa.EPSILON + " 5\n" +
                "6 " + ENfa.EPSILON + " 8\n" +
                "7 " + ENfa.EPSILON + " 5\n" +
                "7 " + ENfa.EPSILON + " 8\n" +
                "8 " + ENfa.EPSILON + " 4\n" +
                "9 " + ENfa.EPSILON + " 13\n" +
                "10 " + ENfa.EPSILON + " 2\n" +
                "Active:\n" +
                "11\n" +
                "1\n" +
                "13\n" +
                "2\n" +
                "3\n" +
                "14\n" +
                "4\n" +
                "5\n" +
                "7\n" +
                "8\n" +
                "9\n" +
                "10\n");
    }

    @Ignore
    @Test
    public void testConstruct9() {
        ENfa automata = RegexENfaUtil.regexToENKA("testConstruct9", "(a|b)|c");
        assertEquals(automata.toString(), "testConstruct9\n" +
                "States:\n" +
                "11\n" +
                "12\n" +
                "13\n" +
                "14\n" +
                "15\n" +
                "16\n" +
                "17\n" +
                "18\n" +
                "1\n" +
                "2 true\n" +
                "3\n" +
                "4\n" +
                "5\n" +
                "6\n" +
                "7\n" +
                "8\n" +
                "9\n" +
                "10\n" +
                "Starting:\n" +
                "1\n" +
                "Transitions:\n" +
                "11 " + ENfa.EPSILON + " 13\n" +
                "12 " + ENfa.EPSILON + " 6\n" +
                "13 b 14\n" +
                "14 " + ENfa.EPSILON + " 12\n" +
                "15 " + ENfa.EPSILON + " 17\n" +
                "16 " + ENfa.EPSILON + " 2\n" +
                "17 c 18\n" +
                "18 " + ENfa.EPSILON + " 16\n" +
                "1 " + ENfa.EPSILON + " 3\n" +
                "1 " + ENfa.EPSILON + " 15\n" +
                "3 " + ENfa.EPSILON + " 5\n" +
                "4 " + ENfa.EPSILON + " 2\n" +
                "5 " + ENfa.EPSILON + " 11\n" +
                "5 " + ENfa.EPSILON + " 7\n" +
                "6 " + ENfa.EPSILON + " 4\n" +
                "7 " + ENfa.EPSILON + " 9\n" +
                "8 " + ENfa.EPSILON + " 6\n" +
                "9 a 10\n" +
                "10 " + ENfa.EPSILON + " 8\n" +
                "Active:\n" +
                "11\n" +
                "1\n" +
                "13\n" +
                "3\n" +
                "15\n" +
                "5\n" +
                "17\n" +
                "7\n" +
                "9\n");
    }

    @Ignore
    @Test
    public void testConstruct10() {
        ENfa automata = RegexENfaUtil.regexToENKA("testConstruct10", "");
        assertEquals(automata.toString(), "testConstruct10\n" +
                "States:\n" +
                "1\n" +
                "2 true\n" +
                "Starting:\n" +
                "1\n" +
                "Transitions:\n" +
                "1 " + ENfa.EPSILON + " 2\n" +
                "Active:\n" +
                "1\n" +
                "2\n");
    }

    @Ignore
    @Test
    public void testConstruct11() {
        ENfa automata = RegexENfaUtil.regexToENKA("testConstruct10", "a|" + ENfa.EPSILON);
        assertEquals(automata.toString(), "testConstruct10\n" +
                "States:\n" +
                "1\n" +
                "2 true\n" +
                "3\n" +
                "4\n" +
                "5\n" +
                "6\n" +
                "7\n" +
                "8\n" +
                "9\n" +
                "10\n" +
                "Starting:\n" +
                "1\n" +
                "Transitions:\n" +
                "1 " + ENfa.EPSILON + " 3\n" +
                "1 " + ENfa.EPSILON + " 7\n" +
                "3 " + ENfa.EPSILON + " 5\n" +
                "4 " + ENfa.EPSILON + " 2\n" +
                "5 a 6\n" +
                "6 " + ENfa.EPSILON + " 4\n" +
                "7 " + ENfa.EPSILON + " 9\n" +
                "8 " + ENfa.EPSILON + " 2\n" +
                "9 " + ENfa.EPSILON + " 10\n" +
                "10 " + ENfa.EPSILON + " 8\n" +
                "Active:\n" +
                "1\n" +
                "2\n" +
                "3\n" +
                "5\n" +
                "7\n" +
                "8\n" +
                "9\n" +
                "10\n");
    }
}
