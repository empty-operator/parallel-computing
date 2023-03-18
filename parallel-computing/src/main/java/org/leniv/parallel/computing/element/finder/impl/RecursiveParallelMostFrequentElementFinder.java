package org.leniv.parallel.computing.element.finder.impl;

import org.leniv.parallel.computing.element.finder.MostFrequentElementFinder;
import org.leniv.parallel.computing.element.finder.impl.problem.ComputeEachElementFrequencyProblem;
import org.leniv.parallel.computing.element.finder.impl.problem.FindMostFrequentElementProblem;
import org.leniv.parallel.computing.element.finder.impl.task.SolveTask;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public class RecursiveParallelMostFrequentElementFinder<E> implements MostFrequentElementFinder<E> {
    private final ForkJoinPool pool = ForkJoinPool.commonPool();

    @Override
    public Result<E> find(Collection<E> elements) {
        Map<E, Long> frequencies = pool.invoke(new SolveTask<>(new ComputeEachElementFrequencyProblem<>(elements)));
        Map.Entry<E, Long> mostFrequent = pool.invoke(new SolveTask<>(new FindMostFrequentElementProblem<>(frequencies)));
        return Result.fromEntry(mostFrequent);
    }
}
