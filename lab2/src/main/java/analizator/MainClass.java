package analizator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainClass {

    public static void main(String[] args) {
        //System.out.println("Heeeelo!");

        ENfa enfa = new ENfa("enfa");


        enfa = createEnfa1(enfa);
        //enfa = createEnfa2(enfa);
        //testCreatedEnfa(enfa);




        Dfa dfa = new Dfa(enfa);
        /*
        // testing DFA methods
        Set<String> dfaStates = dfa.getStates();
        System.out.println(dfaStates);

        for (String state : dfaStates) {
            System.out.println("state: "+state);
            Map<Character, String> transitionsForState = dfa.getTransitionsForState(state);
            if (transitionsForState == null) {
                System.out.println(transitionsForState);
                continue;
            }
            for (Map.Entry<Character,String> transEntry : transitionsForState.entrySet()) {
                System.out.println(transEntry.getKey() + " -> " + transEntry.getValue());
            }
        }

        System.out.println();
        Character[] triggers = new Character[]{'a', 'A', 'S', 'x', 'c'};
        for (String state : dfaStates) {
            System.out.println("state: "+state);
            for (Character t : triggers) {
                System.out.println(t + " -> " + dfa.getTransitionForStateByTrigger(state, t));
            }
        }
        */


    }


    private static void testCreatedEnfa(ENfa enfa) {
        enfa.step('a');
        enfa.step('x');
        enfa.step('a');

        Set<String> currentStatesSet = enfa.getCurrentActiveStates();
        for (String state : currentStatesSet) {
            System.out.println(state);
        }

    }


    private static ENfa createEnfa2(ENfa enfa) {
        enfa.addState("s0", false);
        enfa.addState("s1", false);
        enfa.addState("s2", false);
        enfa.addState("s3", true);
        enfa.addState("s4", false);
        enfa.addState("s5", false);
        enfa.addState("s6", true);
        enfa.addState("s7", false);
        enfa.addState("s8", false);
        enfa.addState("s9", false);
        enfa.addState("s10", true);
        enfa.addState("s11", false);
        enfa.addState("s12", false);
        enfa.addState("s13", true);
        enfa.addState("s14", false);
        enfa.addState("s15", true);

        enfa.setStartingState("s0");

        enfa.addEpsilonTransition("s0", "s1");

        enfa.addEpsilonTransition("s1", "s4");
        enfa.addEpsilonTransition("s1", "s5");
        enfa.addTransition("s1", 'S', "s2");

        enfa.addTransition("s2", 'c', "s3");


        enfa.addEpsilonTransition("s4", "s4");
        enfa.addEpsilonTransition("s4", "s5");
        enfa.addTransition("s4", 'S', "s7");

        enfa.addEpsilonTransition("s5", "s8");
        enfa.addEpsilonTransition("s5", "s9");
        enfa.addTransition("s5", 'A', "s6");

        enfa.addEpsilonTransition("s7", "s8");
        enfa.addEpsilonTransition("s7", "s9");
        enfa.addTransition("s7", 'A', "s10");

        enfa.addTransition("s8", 'a', "s11");

        enfa.addTransition("s9", 'a', "s14");

        enfa.addEpsilonTransition("s11", "s4");
        enfa.addEpsilonTransition("s11", "s5");
        enfa.addTransition("s11", 'S', "s12");

        enfa.addTransition("s12", 'b', "s13");

        enfa.addTransition("s14", 'b', "s15");




        return enfa;
    }

    private static ENfa createEnfa1(ENfa enfa) {
        enfa.addState("s0", false);
        enfa.addState("s1", false);
        enfa.addState("s2", true);
        enfa.addState("s3", false);
        enfa.addState("s4", false);
        enfa.addState("s5", false);
        enfa.addState("s6", true);
        enfa.addState("s7", true);
        enfa.addState("s8", false);
        enfa.addState("s9", false);
        enfa.addState("s10", true);

        enfa.setStartingState("s0");

        enfa.addEpsilonTransition("s0", "s1");
        enfa.addEpsilonTransition("s1", "s3");
        enfa.addTransition("s1", 'S', "s2");
        enfa.addTransition("s3", 'a', "s4");
        //enfa.addTransition("s3", 'S', "s7");
        enfa.addEpsilonTransition("s4", "s7");
        enfa.addEpsilonTransition("s4", "s8");
        enfa.addTransition("s4", 'A', "s5");
        enfa.addTransition("s5", 'c', "s6");
        enfa.addTransition("s8", 'x', "s9");
        enfa.addEpsilonTransition("s9", "s3");
        enfa.addTransition("s9", 'S', "s10");

        return enfa;
    }
}
