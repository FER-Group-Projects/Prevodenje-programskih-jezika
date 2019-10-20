package analizator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LA implements Iterable<Lexem>, Iterator<Lexem> {

    private String currentState;

    private Map<String, Map<ENfa, Action>> enfaActionMap;

    private char[] inputProgram;

    private int startIndex;

    // End index is exclusive.
    private int endIndex;
    private int lineNumber;
    private int indexInCurrentLine;

    public LA(String startingState, Map<String, Map<ENfa, Action>> enfaActionMap, String inputProgram) {
        this.currentState = Objects.requireNonNull(startingState, "Starting state cannot be null.");
        this.enfaActionMap = Objects.requireNonNull(enfaActionMap, "Map of E-NFA actions cannot be null.");
        this.inputProgram = Objects.requireNonNull(inputProgram, "Input program cannot be null.").toCharArray();

        this.startIndex = 0;
        this.endIndex = 0;
        this.lineNumber = 1;
        this.indexInCurrentLine = 0;
    }

    private Lexem extractLexem() {
        Map<ENfa, Action> transitions = enfaActionMap.get(currentState);
        Set<ENfa> enfas = transitions.keySet();

        resetAllENfas(enfas);

        ENfa lastSatisfiedEnfa = null;
        int lastSatisfiedIndex = startIndex;
        int lastIndexInCurrentLine = indexInCurrentLine;

        while (true) {
            if (endIndex == inputProgram.length) {
                break;
            }

            if (areAllENfasStuck(enfas)) {
                endIndex = lastSatisfiedIndex;
                break;
            }

            ENfa satisfiedEnfa = getENfaWithLowestOrdinalNumber(transitions, enfas);

            if (satisfiedEnfa != null) {
                lastSatisfiedEnfa = satisfiedEnfa;
                lastSatisfiedIndex = endIndex;
            }

            // Send next character to all ENfas
            for (ENfa enfa : enfas) {
                if (enfa.isStuck()) continue;

                enfa.step(inputProgram[endIndex]);
            }

            ++endIndex;
            indexInCurrentLine++;
        }

        if (lastSatisfiedEnfa == null) {
            System.err.println("Could not analyze inputProgram[line " + lineNumber + ", character at index " + lastIndexInCurrentLine + "] = '" + inputProgram[startIndex] + "'. Dropping it.");
            ++startIndex;

            endIndex = startIndex;
            indexInCurrentLine = lastIndexInCurrentLine + 1;

            return null;
        } else {
            return performAction(transitions.get(lastSatisfiedEnfa));
        }
    }

    private Lexem performAction(Action action) {
        Objects.requireNonNull(action, "Cannot perform a null action.");

        Lexem lexem = null;

        if (action.goBack != null) {
            endIndex = startIndex + action.goBack;
        }

        if (action.tokenType != null) {
            lexem = new Lexem(action.tokenType, lineNumber, new String(inputProgram, startIndex, endIndex - startIndex));
        }

        if (action.enterState != null) {
            currentState = action.enterState;
        }

        if (action.newLine) {
            ++lineNumber;
            indexInCurrentLine = 0;
        }

        startIndex = endIndex;

        return lexem;
    }

    private ENfa getENfaWithLowestOrdinalNumber(Map<ENfa, Action> transitions, Set<ENfa> enfas) {
        ENfa satisfiedEnfa = null;

        for (ENfa enfa : enfas) {
            if (enfa.isInAcceptableState() && (satisfiedEnfa == null || transitions.get(enfa).ordinalNumber <= transitions.get(satisfiedEnfa).ordinalNumber)) {
                satisfiedEnfa = enfa;
            }
        }

        return satisfiedEnfa;
    }

    private boolean areAllENfasStuck(Set<ENfa> enfas) {
        for (ENfa enfa : enfas) {
            if (!enfa.isStuck()) {
                return false;
            }
        }

        return true;
    }

    private void resetAllENfas(Set<ENfa> enfas) {
        for (ENfa enfa : enfas) {
            enfa.reset();
        }
    }

    @Override
    public Iterator<Lexem> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return startIndex != inputProgram.length;
    }

    @Override
    public Lexem next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Lexical analyzer has reached end of input program.");
        }

        return extractLexem();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not implemented and should not be used.");
    }

    // Static methods
    public static final String PATH_TO_DESCRIPTOR = "analizator/descriptor";

    public static void main(String[] args) {
        String inputProgram = readInputStreamIntoString(System.in);
        LADescriptor laDescriptor = loadLADescriptorFromFile(PATH_TO_DESCRIPTOR);

        LA la = new LA(laDescriptor.startingState, laDescriptor.enfaActionMap, inputProgram);

        for (Lexem lexem : la) {
            if (lexem != null) {
                System.out.println(lexem);
            }
        }
    }

    private static LADescriptor loadLADescriptorFromFile(String pathToDescriptor) {
        Path path = Paths.get(pathToDescriptor);

        if (Files.notExists(path)) {
            throw new IllegalArgumentException("File " + pathToDescriptor + " does not exist.");
        }

        try (FileInputStream fis = new FileInputStream(pathToDescriptor);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (LADescriptor) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // https://stackoverflow.com/a/5445161
    private static String readInputStreamIntoString(InputStream inputStream) {
        try (Scanner scanner = new Scanner(inputStream)) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

}
