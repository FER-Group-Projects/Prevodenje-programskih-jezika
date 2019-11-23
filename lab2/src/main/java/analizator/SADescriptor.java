package analizator;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

//serialize to /analizator/sa-descriptor
public class SADescriptor implements Serializable {

    private static final long serialVersionUID = -1261702926857192083L;

    /**
     * Sync characters used for error recovery
     */
    public Set<String> syncCharacterSet;

    /**
     * PDAState -> (Symbol -> PDAAction)
     * Actions are type of ACCEPT, SHIFT(int nextState), REDUCE(int nextState, GrammarRule reductionRule),
     * PUT(int nextState), REJECT
     */
    public Map<Integer, Map<Symbol, PDAAction>> actionTable;

}
