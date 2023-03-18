package org.leniv.parallel.computing.element.finder.impl.problem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ComputeEachElementFrequencyProblem<E> implements Problem<Map<E, Long>> {
    private final List<E> elements;
    private final Map<E, Long> frequencies;

    public ComputeEachElementFrequencyProblem(Collection<E> elements) {
        this(new ArrayList<>(elements), new ConcurrentHashMap<>());
    }

    public ComputeEachElementFrequencyProblem(List<E> elements, Map<E, Long> frequencies) {
        this.elements = elements;
        this.frequencies = frequencies;
    }

    @Override
    public Map<E, Long> solve() {
        for (E element : elements) {
            frequencies.merge(element, 1L, Long::sum);
        }
        return frequencies;
    }

    @Override
    public Map<E, Long> combineSolutions(Map<E, Long> solution1, Map<E, Long> solution2) {
        return frequencies;
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public Problem<Map<E, Long>> subProblem(int fromIndex, int toIndex) {
        return new ComputeEachElementFrequencyProblem<>(elements.subList(fromIndex, toIndex), frequencies);
    }
}
