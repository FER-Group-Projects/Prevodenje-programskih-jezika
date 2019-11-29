import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import analizator.GrammarRule;
import analizator.Symbol;

class LR1GrammarToLR1DfaTest {

	@Test
	void test() {
		List<GrammarRule> grList = new ArrayList<>();
		
		grList.add(new GrammarRule(new Symbol("<S>"), Arrays.asList(new Symbol("b"), new Symbol("<A>"), new Symbol("<B>"))));
		grList.add(new GrammarRule(new Symbol("<A>"), Arrays.asList(new Symbol("b"), new Symbol("<B>"), new Symbol("c"))));
		grList.add(new GrammarRule(new Symbol("<B>"), Arrays.asList(new Symbol("b"))));
		
		ENfa a3 = SyntaxAnalysisUtils.convertRulesToENfa(grList, new Symbol("<S>"), 
				Arrays.asList(new Symbol("b"), new Symbol("<A>"), new Symbol("<B>"), new Symbol("<S>"), new Symbol("c")));
		Dfa a4 = new Dfa(a3);
		
		assertEquals(9, a4.getStates().size());
		
		System.out.println("LR Dfa states number : " + a4.getStates().size());
		System.out.println();
		for(String s : a4.getStates()) {
			System.out.println("State : " + s);
			System.out.println("LR1 items : " + a4.getEnfaStatesForDfaState(s));
			System.out.println("Triggered by : " + a4.getTransitionsForState(s));
			System.out.println();
		}
	}

}
