package org.leniv.parallel.computing;

import static java.lang.System.err;
import static java.lang.System.out;

import org.leniv.parallel.computing.element.finder.MostFrequentElementFinder.Result;
import org.leniv.parallel.computing.element.finder.impl.SequentialMostFrequentElementFinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Function;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws IOException {
        List<Integer> integers = getInput(prompt("Read input from file", Scanner::nextLine));
        printInput(integers);
        var finder = new SequentialMostFrequentElementFinder<Integer>();
        Result<Integer> result = finder.find(integers);
        out.printf("Result: %s%n", resultToText(result));
        saveResultToFile(result, prompt("Save result to file", Scanner::nextLine));
    }

    private static <T> T promptNextLine(String text, Function<Scanner, T> extractor) {
        T input = prompt(text, extractor);
        SCANNER.nextLine();
        return input;
    }

    private static <T> T prompt(String text, Function<Scanner, T> extractor) {
        out.printf("%s: %n", text);
        return extractor.apply(SCANNER);
    }

    private static List<Integer> getInput(String fileName) throws IOException {
        if (fileName.isBlank()) {
            err.println("File name was not specified, input will be generated.");
            return generateInput();
        }
        return readInputFromFile(fileName);
    }

    private static List<Integer> generateInput() throws IOException {
        long size = promptNextLine("Enter input size", Scanner::nextLong);
        int minValue = promptNextLine("Enter min value", Scanner::nextInt);
        int maxValue = promptNextLine("Enter max value", Scanner::nextInt);
        List<Integer> integers = generateIntegers(size, minValue, maxValue);
        saveInputToFile(integers, prompt("Save input to file", Scanner::nextLine));
        return integers;
    }

    private static List<Integer> generateIntegers(long size, int minValue, int maxValue) {
        return RANDOM.ints(size, minValue, maxValue).boxed().toList();
    }

    private static void saveInputToFile(List<Integer> integers, String fileName) throws IOException {
        if (fileName.isBlank()) {
            err.println("File name was not specified, input will not be saved.");
        } else {
            Files.write(Path.of(fileName), integers.stream().map(Object::toString).toList());
        }
    }

    private static List<Integer> readInputFromFile(String fileName) throws IOException {
        return Files.readAllLines(Path.of(fileName)).stream()
                .map(Integer::valueOf)
                .toList();
    }

    private static void printInput(List<Integer> integers) {
        if (integers.size() <= 100) {
            out.printf("Input: %s%n", integers);
        }
    }

    private static void saveResultToFile(Result<Integer> result, String fileName) throws IOException {
        if (fileName.isBlank()) {
            err.println("File name was not specified, result will not be saved.");
        } else {
            Files.writeString(Path.of(fileName), resultToText(result));
        }
    }

    private static String resultToText(Result<Integer> result) {
        return String.format("The most frequent element is %s, which occurs %s times.", result.element(), result.frequency());
    }
}
