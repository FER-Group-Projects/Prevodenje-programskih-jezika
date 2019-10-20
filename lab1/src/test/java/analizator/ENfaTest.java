package analizator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ENfaTest {
	
	static ENfa e;
	
	@Before
	public void BeforeEachTest() {
		e = new ENfa("e", "S1", true);
		e.addState("S2", true);
		e.addState("S3", false);
		e.addState("S4", false);
		e.addState("S5", false);
		e.addState("S6", false);
		e.addState("S7", false);
		e.addState("S8", true);
		e.addState("S9", false);
		e.addState("S10", true);
		e.addState("S11", false);
		e.addTransition("S1", 'a', "S2");
		e.addTransition("S1", 'b', "S3");
		e.addTransition("S1", ENfa.EPSILON, "S7");
		e.addTransition("S2", ENfa.EPSILON, "S4");
		e.addTransition("S2", 'b', "S5");
		e.addTransition("S2", 'a', "S6");
		e.addTransition("S3", 'e', "S6");
		e.addTransition("S3", 'd', "S9");
		e.addTransition("S7", ENfa.EPSILON, "S8");
		e.addTransition("S4", 'a', "S11");
		e.addTransition("S5", ENfa.EPSILON, "S11");
		e.addTransition("S6", ENfa.EPSILON, "S11");
		e.addTransition("S9", 'd', "S10");
		e.addTransition("S8", ENfa.EPSILON, "S10");
		e.addTransition("S10", 'c', "S11");
	}
	
	@Test
	public void test1() {
		assertEquals("e", e.getName());
		assertEquals("S1", e.getStartingState());
		assertTrue(e.getAcceptableStates().size()==4);
		assertTrue(e.getAllStates().size()==11);
	}
	
	@Test
	public void test2() {
		assertTrue(e.isInAcceptableState());
		assertEquals(4, e.getCurrentActiveStates().size());
	}
	
	@Test
	public void test3() {
		e.step('a');
		assertTrue(e.isInAcceptableState());
		assertEquals(2, e.getCurrentActiveStates().size());
		assertTrue(e.getCurrentActiveStates().contains("S2"));
		assertTrue(e.getCurrentActiveStates().contains("S4"));
	}
	
	@Test
	public void test4() {
		e.step('a');
		e.step('a');
		assertFalse(e.isInAcceptableState());
		assertEquals(2, e.getCurrentActiveStates().size());
		assertTrue(e.getCurrentActiveStates().contains("S6"));
		assertTrue(e.getCurrentActiveStates().contains("S11"));
	}

}
