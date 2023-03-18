package org.leniv.parallel.computing.element.finder.impl;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import org.leniv.parallel.computing.element.finder.MostFrequentElementFinder;
import org.leniv.parallel.computing.element.finder.impl.problem.ComputeEachElementFrequencyProblem;
import org.leniv.parallel.computing.element.finder.impl.problem.FindMostFrequentElementProblem;
import org.leniv.parallel.computing.element.finder.impl.problem.Problem;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SimpleParallelMostFrequentElementFinder<E> implements MostFrequentElementFinder<E>, AutoCloseable {
    private final int parallelism = Runtime.getRuntime().availableProcessors();
    private final ExecutorService executorService = Executors.newFixedThreadPool(parallelism);

    @Override
    public Result<E> find(Collection<E> elements) {
        Map<E, Long> frequencies = solve(new ComputeEachElementFrequencyProblem<>(elements));
        Map.Entry<E, Long> mostFrequent = solve(new FindMostFrequentElementProblem<>(frequencies));
        return Result.fromEntry(mostFrequent);
    }

    private <S> S solve(Problem<S> problem) {
        List<Future<S>> solutions = problem.partition(parallelism).stream()
                .map(p -> (Callable<S>) p::solve)
                .collect(collectingAndThen(toList(), this::invokeAll));
        return solutions.stream()
                .map(Future::resultNow)
                .reduce(problem::combineSolutions)
                .orElseThrow();
    }

    private <V> List<Future<V>> invokeAll(Collection<Callable<V>> tasks) {
        try {
            return executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        executorService.close();
    }
}
