import analizator.ENfa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SyntaxAnalysisUtilsTest {

    // Example is from auditory exercises for the midterm.

    private GrammarRule sRule;
    private GrammarRule aRule;
    private GrammarRule bRule;

    private List<Symbol> symbols;
    private List<GrammarRule> rules;
    private Symbol startingSymbol;

    private ENfa enfa;

    @BeforeEach
    void setUp() {
        sRule = createGrammarRule("<S>", "b", "<A>", "<B>");
        aRule = createGrammarRule("<A>", "b", "<B>", "c");
        bRule = createGrammarRule("<B>", "b");

        symbols = createSymbolSet("<S>", "<A>", "<B>", "b", "c");
        rules = new ArrayList<>();
        startingSymbol = new Symbol("<S>");

        rules.add(sRule);
        rules.add(aRule);
        rules.add(bRule);

        enfa = SyntaxAnalysisUtils.convertRulesToENfa(rules, startingSymbol, symbols);
    }

    @Test
    void testInitialTransitions() {
        assertEquals(mapOfEntries(
                toEntry(ENfa.EPSILON, setOf(createLRItem(sRule, 0, Symbol.EPSILON.toString()).toString()))
        ), enfa.getTransitionsForState("q0"));
    }

    @Test
    void testBRuleWithEpsilonAfterTransitions() {
        assertEquals(mapOfEntries(
                toEntry("b", setOf(createLRItem(bRule, 1, Symbol.EPSILON.toString()).toString()))
        ), enfa.getTransitionsForState(createLRItem(bRule, 0, Symbol.EPSILON.toString()).toString()));

        assertEquals(null, enfa.getTransitionsForState(createLRItem(bRule, 1, Symbol.EPSILON.toString()).toString()));
    }

    @Test
    void testBRuleWithCAfterTransitions() {
        assertEquals(mapOfEntries(
                toEntry("b", setOf(createLRItem(bRule, 1, "c").toString()))
        ), enfa.getTransitionsForState(createLRItem(bRule, 0, "c").toString()));

        assertEquals(null, enfa.getTransitionsForState(createLRItem(bRule, 1, "c").toString()));
    }

    @Test
    void testARuleTransitions() {
        assertEquals(mapOfEntries(
                toEntry("b", setOf(createLRItem(aRule, 1, "b").toString()))
        ), enfa.getTransitionsForState(createLRItem(aRule, 0, "b").toString()));

        assertEquals(mapOfEntries(
                toEntry("<B>", setOf(createLRItem(aRule, 2, "b").toString())),
                toEntry(ENfa.EPSILON, setOf(createLRItem(bRule, 0, "c").toString()))
        ), enfa.getTransitionsForState(createLRItem(aRule, 1, "b").toString()));

        assertEquals(mapOfEntries(
                toEntry("c", setOf(createLRItem(aRule, 3, "b").toString()))
        ), enfa.getTransitionsForState(createLRItem(aRule, 2, "b").toString()));

        assertEquals(null, enfa.getTransitionsForState(createLRItem(aRule, 3, "b").toString()));
    }

    @Test
    void testSRuleTransitions() {
        assertEquals(mapOfEntries(
                toEntry("b", setOf(createLRItem(sRule, 1, Symbol.EPSILON.toString()).toString()))
        ), enfa.getTransitionsForState(createLRItem(sRule, 0, Symbol.EPSILON.toString()).toString()));

        assertEquals(mapOfEntries(
                toEntry("<A>", setOf(createLRItem(sRule, 2, Symbol.EPSILON.toString()).toString())),
                toEntry(ENfa.EPSILON, setOf(createLRItem(aRule, 0, "b").toString()))
        ), enfa.getTransitionsForState(createLRItem(sRule, 1, Symbol.EPSILON.toString()).toString()));

        assertEquals(mapOfEntries(
                toEntry("<B>", setOf(createLRItem(sRule, 3, Symbol.EPSILON.toString()).toString())),
                toEntry(ENfa.EPSILON, setOf(createLRItem(bRule, 0, Symbol.EPSILON.toString()).toString()))
        ), enfa.getTransitionsForState(createLRItem(sRule, 2, Symbol.EPSILON.toString()).toString()));

        assertEquals(null, enfa.getTransitionsForState(createLRItem(sRule, 3, Symbol.EPSILON.toString()).toString()));
    }

    List<Symbol> createSymbolSet(String ... symbols) {
        return Stream.of(symbols).map(Symbol::new).collect(Collectors.toList());
    }

    GrammarRule createGrammarRule(String from, String ... to) {
        return new GrammarRule(new Symbol(from), Stream.of(to).map(Symbol::new).collect(Collectors.toList()));
    }

    LR1Item createLRItem(GrammarRule rule, int dotIndex, String after) {
        return new LR1Item(rule, dotIndex, new Symbol(after));
    }

    <T> Set<T> setOf(T ... values) {
        return Stream.of(values).collect(Collectors.toSet());
    }

    <K, V> Map.Entry<K, V> toEntry(K key, V value) {
        return new Map.Entry<K, V>() {
            @Override
            public K getKey() {
                return key;
            }

            @Override
            public V getValue() {
                return value;
            }

            @Override
            public V setValue(V value) {
                return null;
            }

            @Override
            public boolean equals(Object o) {
                return false;
            }

            @Override
            public int hashCode() {
                return 0;
            }
        };
    }

    <K, V> Map<K, V> mapOfEntries(Map.Entry<K, V> ... entries) {
        return Stream.of(entries).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}