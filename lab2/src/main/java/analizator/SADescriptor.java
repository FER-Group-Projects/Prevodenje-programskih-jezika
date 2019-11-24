package analizator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

//serialize to /analizator/sa-descriptor
public class SADescriptor implements Serializable {

    private static final long serialVersionUID = -1261702926857192083L;

    /**
     * Sync characters used for error recovery
     */
    public Set<Symbol> syncSymbolSet;

    public ArrayList<GrammarRule> grammarReductionRules;

    /**
     * PDAState -> (Symbol -> PDAAction)
     * Actions are type of ACCEPT(GrammarRule reductionRule), SHIFT(int nextState), REDUCE(GrammarRule reductionRule),
     * PUT(int nextState), REJECT
     */
    public Map<String, Map<Symbol, PDAAction>> actionTable;

}
