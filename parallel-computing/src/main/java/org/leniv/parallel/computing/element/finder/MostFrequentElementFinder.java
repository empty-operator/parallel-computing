package org.leniv.parallel.computing.element.finder;

import java.util.Collection;
import java.util.Map;

public interface MostFrequentElementFinder<E> {
    Result<E> find(Collection<E> elements);

    record Result<E>(E element, long frequency) {
        public static <E> Result<E> fromEntry(Map.Entry<E, Long> entry) {
            return new Result<>(entry.getKey(), entry.getValue());
        }
    }
}
