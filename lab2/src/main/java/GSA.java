import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import analizator.ActionType;
import analizator.GrammarRule;
import analizator.PDAAction;
import analizator.SADescriptor;
import analizator.Symbol;

public class GSA {
	
	public static final String PATH_TO_DESCRIPTOR = "analizator/descriptor";
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		List<Symbol> symbols = new ArrayList<>();
		Set<Symbol> syncSymbols = new HashSet<>();
		List<GrammarRule> grammarRules = new ArrayList<>();
		
		symbols.add(new Symbol("<S'>"));
		
		// INPUT
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		Arrays.stream(reader.readLine().substring(3).split("\\s+")).forEach(s -> symbols.add(new Symbol(s)));
		Arrays.stream(reader.readLine().substring(3).split("\\s+")).forEach(s -> symbols.add(new Symbol(s)));
		Arrays.stream(reader.readLine().substring(5).split("\\s+")).forEach(s -> syncSymbols.add(new Symbol(s)));
		
		String line = null, leftSide = null;
		while((line = reader.readLine()) != null) {
			if(!line.startsWith(" ")) {
				leftSide = line;
				continue;
			}
			
			List<Symbol> toList = new ArrayList<>();
			Arrays.stream(line.substring(1).split(" ")).forEach(s -> toList.add(new Symbol(s)));
			grammarRules.add(new GrammarRule(new Symbol(leftSide), toList));
		}
		
		reader.close();
		
		grammarRules.add(0, new GrammarRule(new Symbol("<S'>"), Arrays.asList(symbols.get(1))));
		
		// CREATE DFA
		Dfa lr1dfa = new Dfa(SyntaxAnalysisUtils.convertRulesToENfa(grammarRules, symbols.get(0), symbols));
		
		// CREATE ACTION-NEXT_STATE TABLE
		Map<String, Map<Symbol, PDAAction>> actionTable = new HashMap<>();

		// BFS for populating the action-next_state table
		Set<String> inQueue = new HashSet<>();
		Queue<String> toVisit = new LinkedList<>();
		toVisit.add(lr1dfa.getStartingState());
		inQueue.add(lr1dfa.getStartingState());
		
		while(!toVisit.isEmpty()) {
			String next = toVisit.poll();
			if(!actionTable.containsKey(next)) {
				actionTable.put(next, new HashMap<>());
			}
			
			for(String lr1 : lr1dfa.getEnfaStatesForDfaState(next)) {
				LR1Item item;
				try {
					item = LR1Item.parseItem(lr1);
				} catch (IllegalArgumentException ex) {
					continue;
				}
				if(!item.isFinalItem()) continue;
				
				int grIndex = grammarRules.indexOf(item.getGrammarRule());
				PDAAction old = actionTable.get(next).get(item.getAfter());
				if(old!=null && (old.getActionType()==ActionType.SHIFT || old.getActionType()==ActionType.REDUCE && old.getNumber()<grIndex)) continue;
				actionTable.get(next).put(item.getAfter(), new PDAAction(ActionType.REDUCE, grIndex));
			}
			
			if(containsStartingLR1Item(lr1dfa.getEnfaStatesForDfaState(next), symbols.get(1))) {
				actionTable.get(next).put(Symbol.EPSILON, new PDAAction(ActionType.ACCEPT, 0));
			}
			
			Map<String, String> transitions = lr1dfa.getTransitionsForState(next);
			if(transitions==null) continue;

			for(Entry<String, String> entry : transitions.entrySet()) {
				if(!inQueue.contains(entry.getValue())) {
					inQueue.add(entry.getValue());
					toVisit.add(entry.getValue());
				}
				
				if(entry.getKey().startsWith("<")) {
					actionTable.get(next).put(new Symbol(entry.getKey()), new PDAAction(ActionType.PUT, Integer.parseInt(entry.getValue().substring(1))));
				} else {
					actionTable.get(next).put(new Symbol(entry.getKey()), new PDAAction(ActionType.SHIFT, Integer.parseInt(entry.getValue().substring(1))));
				}	
			}
			
		}
		
		SADescriptor saDescriptor = new SADescriptor();
		saDescriptor.actionTable = actionTable;
		saDescriptor.grammarReductionRules = grammarRules;
		saDescriptor.syncSymbolSet = syncSymbols;
		
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PATH_TO_DESCRIPTOR));
		oos.writeObject(saDescriptor);
		oos.flush();
		oos.close();
		
	}
	
	private static boolean containsStartingLR1Item(Set<String> lr1Items, Symbol startingSymbol) {
		LR1Item start = new LR1Item(new GrammarRule(new Symbol("<S'>"), Arrays.asList(startingSymbol)), 1, Symbol.EPSILON);
	
		for(String s : lr1Items) {
			LR1Item itm;
			try {
				itm = LR1Item.parseItem(s);
			} catch (IllegalArgumentException ex) {
				continue;
			}
			if(itm.equals(start))
				return true;
		}
		
		return false;
	}

}
