package org.leniv.parallel.computing.element.finder.impl.problem;

import static java.lang.Math.ceilDiv;
import static java.lang.Math.min;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.IntStream;

public interface Problem<S> {
    S solve();

    S combineSolutions(S solution1, S solution2);

    int size();

    Problem<S> subProblem(int fromIndex, int toIndex);

    default List<Problem<S>> partition(int n) {
        int partitionSize = ceilDiv(size(), n);
        return IntStream.iterate(0, i -> i < size(), i -> i + partitionSize)
                .mapToObj(i -> subProblem(i, min(i + partitionSize, size())))
                .collect(toList());
    }
}
