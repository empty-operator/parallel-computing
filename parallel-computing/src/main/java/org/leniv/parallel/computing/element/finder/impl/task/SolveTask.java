package org.leniv.parallel.computing.element.finder.impl.task;

import org.leniv.parallel.computing.element.finder.impl.problem.Problem;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SolveTask<E, R> extends RecursiveTask<R> {
    private static final int LOAD_FACTOR = 4;
    private final Problem<R> problem;
    private final int threshold;

    public SolveTask(Problem<R> problem) {
        this.problem = problem;
        this.threshold = problem.size() / (getParallelism() * LOAD_FACTOR);
    }

    public SolveTask(Problem<R> problem, SolveTask<E, R> parent) {
        this.problem = problem;
        this.threshold = parent.threshold;
    }

    private static int getParallelism() {
        return (inForkJoinPool() ? getPool() : ForkJoinPool.commonPool()).getParallelism();
    }

    @Override
    protected R compute() {
        int size = problem.size();
        if (size <= threshold) {
            return problem.solve();
        }
        int mid = size / 2;
        var task1 = new SolveTask<>(problem.subProblem(0, mid), this);
        var task2 = new SolveTask<>(problem.subProblem(mid, size), this);
        invokeAll(task1, task2);
        R result1 = task1.join();
        R result2 = task2.join();
        return problem.combineSolutions(result1, result2);
    }
}
