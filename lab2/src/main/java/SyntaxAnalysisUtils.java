import analizator.GrammarRule;
import analizator.Symbol;

import java.util.*;
import java.util.stream.Collectors;

public class SyntaxAnalysisUtils {

    public static ENfa convertRulesToENfa(List<GrammarRule> rules, Symbol startingSymbol, List<Symbol> symbols) {
        ENfa enfa = new ENfa("lrItems");
        
        addStartingTransitions(rules, startingSymbol, enfa);
        addShiftUpdateTransitions(rules, symbols, enfa);
        addBranchUpdateTransitions(rules, symbols, enfa);

        return enfa;
    }

    private static void addStartingTransitions(List<GrammarRule> rules, Symbol startingSymbol, ENfa enfa) {
        List<GrammarRule> rulesFromStartingState = rules
                .stream()
                .filter(r -> r.getFrom().equals(startingSymbol))
                .collect(Collectors.toList());

        enfa.addState("q0");
        enfa.setStartingState("q0");

        for (GrammarRule rule : rulesFromStartingState) {
            LR1Item lr1Item = new LR1Item(rule, 0, Symbol.EPSILON);

            enfa.addState(lr1Item.toString());
            enfa.addEpsilonTransition("q0", lr1Item.toString());
        }
    }

    private static void addShiftUpdateTransitions(List<GrammarRule> rules, List<Symbol> symbols, ENfa enfa) {
        List<Symbol> terminalSymbols = symbols
                .stream()
                .filter(Symbol::isTerminal)
                .collect(Collectors.toList());

        terminalSymbols.add(Symbol.EPSILON);

        for (GrammarRule rule : rules) {
            for (int dotIndex = 0; dotIndex < rule.getToList().size(); dotIndex++) {
                for (Symbol terminalSymbol : terminalSymbols) {
                    LR1Item from = new LR1Item(rule, dotIndex, terminalSymbol);
                    LR1Item to = new LR1Item(rule, dotIndex + 1, terminalSymbol);

                    if (!enfa.containsState(from.toString())) enfa.addState(from.toString());
                    if (!enfa.containsState(to.toString())) enfa.addState(to.toString());

                    enfa.addTransition(from.toString(), rule.getToList().get(dotIndex).getSymbol(), to.toString());
                }
            }
        }
    }

    private static Map<Symbol, Set<Symbol>> computeFirstSets(List<GrammarRule> rules, List<Symbol> symbols) {
        Map<Symbol, Set<Symbol>> firstSets = new HashMap<>();

        for (Symbol symbol : symbols) {
            firstSets.put(symbol, new HashSet<>());

            if (symbol.isTerminal()) {
                firstSets.get(symbol).add(symbol);
            }
        }

        firstSets.put(Symbol.EPSILON, new HashSet<>());
        firstSets.get(Symbol.EPSILON).add(Symbol.EPSILON);

        for (GrammarRule rule : rules) {
            if (rule.getToList().get(0).isTerminal()) {
                firstSets.get(rule.getFrom()).add(rule.getToList().get(0));
            }
        }

        while (true) {
            boolean anyChanges = false;

            for (GrammarRule rule : rules) {
                if (!rule.getToList().get(0).isTerminal()) {
                    Set<Symbol> firstSet = firstSets.get(rule.getFrom());
                    int sizeBefore = firstSet.size();

                    firstSet.addAll(firstSets.get(rule.getToList().get(0)));

                    if (sizeBefore != firstSet.size()) {
                        anyChanges = true;
                    }
                }
            }

            if (!anyChanges) break;
        }

        return firstSets;
    }

    private static void addBranchUpdateTransitions(List<GrammarRule> rules, List<Symbol> symbols, ENfa enfa) {
        Map<Symbol, Set<Symbol>> firstSets = computeFirstSets(rules, symbols);

        List<Symbol> terminalSymbols = symbols
                .stream()
                .filter(Symbol::isTerminal)
                .collect(Collectors.toList());

        terminalSymbols.add(Symbol.EPSILON);

        for (GrammarRule fromRule : rules) {
            List<Symbol> toList = fromRule.getToList();

            for (int dotIndex = 0; dotIndex < toList.size(); dotIndex++) {
                if (toList.get(dotIndex).isTerminal()) continue;

                Set<Symbol> fromSymbols = new HashSet<>(terminalSymbols);

                for (GrammarRule toRule : rules) {
                    if (!toRule.getFrom().equals(toList.get(dotIndex))) continue;

                    for (Symbol fromSymbol : fromSymbols) {
                        Set<Symbol> toSymbols;

                        if (toList.size() > dotIndex + 1) {
                            toSymbols = firstSets.get(toList.get(dotIndex + 1));
                        }
                        else {
                            toSymbols = firstSets.get(fromSymbol);
                        }

                        for (Symbol toSymbol : toSymbols) {
                            LR1Item fromItem = new LR1Item(fromRule, dotIndex, fromSymbol);
                            LR1Item toItem = new LR1Item(toRule, 0, toSymbol);

                            if (!enfa.containsState(fromItem.toString())) enfa.addState(fromItem.toString());
                            if (!enfa.containsState(toItem.toString())) enfa.addState(toItem.toString());

                            enfa.addEpsilonTransition(fromItem.toString(), toItem.toString());
                        }
                    }
                }
            }
        }
    }

}
