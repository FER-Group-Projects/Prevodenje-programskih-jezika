package analizator;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

//serialize to /analizator/sa-descriptor
public class SADescriptor implements Serializable {

    private static final long serialVersionUID = -1261702926857192083L;

    /**
     * Sync characters used for error recovery
     */
    public Set<Symbol> syncSymbolSet;

    public List<GrammarRule> grammarReductionRules;

    /**
     * PDAState -> (Symbol -> PDAAction)
     * Actions are type of ACCEPT(GrammarRule reductionRule), SHIFT(int nextState), REDUCE(GrammarRule reductionRule),
     * PUT(int nextState), REJECT
     */
    public Map<String, Map<Symbol, PDAAction>> actionTable;

    public String startingState;

    @Override
    public String toString() {
    	return String.format("SyncSymbolSet : %s%n%nGrammarReductionRules : %s%n%nActionTable : %s%n%n%n",
    			syncSymbolSet.toString(), grammarReductionRules.toString(), actionTable.toString());
    }

}
