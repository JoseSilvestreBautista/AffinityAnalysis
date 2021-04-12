package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Main {

  public static void main(String[] args) throws IOException {

    File sampleFile = new File("src/com/sampleFile.csv");
    BufferedReader reader;

    if (!sampleFile.exists()) {
      System.out.println("The Files was not read!");
      System.exit(1);
    }

    reader = new BufferedReader(new FileReader(sampleFile));
    String line = null;
    int totalBooks = 0;

    String[] books = reader.readLine().split(",");
    int numberOfBooks = books.length;

    List<int[]> sampleResults = new ArrayList<>();

    while ((line = reader.readLine()) != null) {
      sampleResults.add(Arrays.stream(line.split(","))
          .mapToInt(Integer::parseInt)
          .toArray());
      totalBooks++;

    }
    reader.close();

    Map<String, Integer> fullResult = new HashMap<>();
    Map<HashSet<String>, Integer> vaildResults = new HashMap<>();

    for (int[] sample : sampleResults) {
      for (int premise = 0; premise < numberOfBooks; premise++) {
        if (sample[premise] == 1) {
          fullResult.put(
              books[premise], fullResult.getOrDefault(books[premise], 0) + 1);

        }
        for (int conclusoin = 0; conclusoin < numberOfBooks; conclusoin++) {
          if (conclusoin == premise) {
            continue;
          }
          if (sample[conclusoin] == 1) {
            vaildResults.put(
                new HashSet<String>(Arrays.asList(
                    books[premise], books[conclusoin])), vaildResults.getOrDefault(
                    new HashSet<String>(Arrays.asList(books[premise], books[conclusoin])), 0) + 1
            );
          }
        }


      }
    }

    for (HashSet<String> bookSet : vaildResults.keySet()) {
      List<String> bookList = bookSet.stream().collect(Collectors.toList());

      double confidence = (double) fullResult.get(bookList.get(0)) / vaildResults.get(bookSet);
      double support = (double) vaildResults.get(bookSet) / totalBooks;

      System.out.printf("We show a confidence of %f that person who"
              + " bought %s will also buy %s%n      and a support of %f that " +
              "a person will buy these items together at all.%n",
          confidence, bookList.get(0), bookList.get(1), support);

    }
  }


}
