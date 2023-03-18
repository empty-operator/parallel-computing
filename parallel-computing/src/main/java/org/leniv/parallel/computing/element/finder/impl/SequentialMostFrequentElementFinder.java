package org.leniv.parallel.computing.element.finder.impl;

import static java.util.Map.Entry.comparingByValue;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import org.leniv.parallel.computing.element.finder.MostFrequentElementFinder;

import java.util.Collection;

public class SequentialMostFrequentElementFinder<E> implements MostFrequentElementFinder<E> {
    @Override
    public Result<E> find(Collection<E> elements) {
        return elements.stream()
                .collect(groupingBy(identity(), counting()))
                .entrySet()
                .stream()
                .max(comparingByValue())
                .map(Result::fromEntry)
                .orElseThrow();
    }
}
