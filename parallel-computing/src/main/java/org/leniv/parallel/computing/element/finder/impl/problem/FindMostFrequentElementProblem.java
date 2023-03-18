package org.leniv.parallel.computing.element.finder.impl.problem;

import static java.util.Map.Entry.comparingByValue;

import java.util.List;
import java.util.Map;

public class FindMostFrequentElementProblem<E> implements Problem<Map.Entry<E, Long>> {
    private final List<Map.Entry<E, Long>> frequencies;

    public FindMostFrequentElementProblem(Map<E, Long> frequencies) {
        this(frequencies.entrySet().stream().toList());
    }

    public FindMostFrequentElementProblem(List<Map.Entry<E, Long>> frequencies) {
        this.frequencies = frequencies;
    }

    @Override
    public Map.Entry<E, Long> solve() {
        return frequencies.stream().max(comparingByValue()).orElseThrow();
    }

    @Override
    public Map.Entry<E, Long> combineSolutions(Map.Entry<E, Long> solution1, Map.Entry<E, Long> solution2) {
        return solution1.getValue() > solution2.getValue() ? solution1 : solution2;
    }

    @Override
    public int size() {
        return frequencies.size();
    }

    @Override
    public Problem<Map.Entry<E, Long>> subProblem(int fromIndex, int toIndex) {
        return new FindMostFrequentElementProblem<>(frequencies.subList(fromIndex, toIndex));
    }
}
