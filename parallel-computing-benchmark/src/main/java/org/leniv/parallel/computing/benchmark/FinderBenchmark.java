package org.leniv.parallel.computing.benchmark;

import org.leniv.parallel.computing.element.finder.MostFrequentElementFinder;
import org.leniv.parallel.computing.element.finder.MostFrequentElementFinder.Result;
import org.leniv.parallel.computing.element.finder.impl.RecursiveParallelMostFrequentElementFinder;
import org.leniv.parallel.computing.element.finder.impl.SequentialMostFrequentElementFinder;
import org.leniv.parallel.computing.element.finder.impl.SimpleParallelMostFrequentElementFinder;
import org.leniv.parallel.computing.element.finder.impl.StreamParallelMostFrequentElementFinder;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 10)
@Measurement(iterations = 5, time = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class FinderBenchmark {
    public enum Type {
        SEQUENTIAL, SIMPLE_PARALLEL, RECURSIVE_PARALLEL, STREAM_PARALLEL
    }

    public enum Input {
        SMALL(10_000, 100),
        NORMAL(1_000_000, 10_000),
        LARGE(150_000_000, 1_000_000);

        final long size;
        final int maxValue;

        Input(long size, int maxValue) {
            this.size = size;
            this.maxValue = maxValue;
        }
    }

    @Param
    private Type type;

    @Param
    private Input input;

    private MostFrequentElementFinder<Integer> finder;
    private List<Integer> integers;

    @Setup
    public void setup() {
        finder = switch (type) {
            case SEQUENTIAL -> new SequentialMostFrequentElementFinder<>();
            case SIMPLE_PARALLEL -> new SimpleParallelMostFrequentElementFinder<>();
            case RECURSIVE_PARALLEL -> new RecursiveParallelMostFrequentElementFinder<>();
            case STREAM_PARALLEL -> new StreamParallelMostFrequentElementFinder<>();
        };
        integers = new Random().ints(input.size, 0, input.maxValue).boxed().toList();
    }

    @Benchmark
    public Result<?> find() {
        return finder.find(integers);
    }

    @TearDown
    public void tearDown() throws Exception {
        if (finder instanceof AutoCloseable closeable) {
            closeable.close();
        }
    }
}
