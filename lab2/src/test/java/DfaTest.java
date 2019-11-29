import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DfaTest {

	@Test
	void test() {
		ENfa a1 = new ENfa("a1", "q0", true);
		a1.addState("q1", true);
		a1.addState("q2", false);
		a1.addState("q3", true);
		a1.addState("q4", false);
		a1.addState("q5", false);
		a1.addState("q6", false);
		a1.addState("q7", false);
		
		a1.addEpsilonTransition("q0", "q1");
		a1.addEpsilonTransition("q1", "q2");
		a1.addEpsilonTransition("q0", "q2");
		
		a1.addTransition("q1", "a", "q3");
		
		a1.addEpsilonTransition("q3", "q4");
		a1.addEpsilonTransition("q3", "q5");
		
		a1.addTransition("q4", "b", "q6");
		a1.addTransition("q4", "c", "q7");
		
		Dfa a2 = new Dfa(a1);
		
		assertEquals(4, a2.getStates().size());
		
//		for(String s : a2.getStates()) {
//			System.out.println("State : " + s);
//			System.out.println("Triggered by : " + a2.getTransitionsForState(s));
//			System.out.println();
//		}
	}

}
