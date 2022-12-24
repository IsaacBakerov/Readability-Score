package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String input = "";

        try (Scanner scanner = new Scanner(new File(args[0]))){
            while (scanner.hasNext()) {
                input += scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        long sentences = input.split("[.!?]").length;
        long characters = input.replaceAll("\\s", "").length();
        long words = input.split("\\s").length;
        long syllables = syllables(input)[0];
        long polysyllables = syllables(input)[1];

        System.out.println("The text is: \n" + input);
        System.out.printf("\nWords: %d\n", words);
        System.out.printf("Sentences: %d\n", sentences);
        System.out.printf("Characters: %d\n", characters);
        System.out.println("Syllables: " + syllables);
        System.out.println("Polysyllables: " + polysyllables);
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");

        Scanner sc = new Scanner(System.in);
        String scoreString = sc.nextLine();
        System.out.println();

        if (scoreString.equals("ARI")) {
            double ari = scoreAri(sentences, words, characters);
            printScoreAri(ari);
        } else if (scoreString.equals("FK")) {
            double fk = scoreFk(sentences, words, syllables);
            printScoreFk(fk);
        } else if (scoreString.equals("SMOG")) {
            double smog = scoreSmog(polysyllables, sentences);
            printScoreSmog(smog);
        } else if (scoreString.equals("CL")) {
            double cl = scoreCl(characters, words, sentences);
            printScoreCl(cl);
        } else if (scoreString.equals("all")) {
            double ari = scoreAri(sentences, words, characters);
            double fk = scoreFk(sentences, words, syllables);
            double smog = scoreSmog(polysyllables, sentences);
            double cl = scoreCl(characters, words, sentences);
            printScoreAri(ari);
            printScoreFk(fk);
            printScoreSmog(smog);
            printScoreCl(cl);
        }

    }

    public static void printScoreCl(double cl) {
        long age = Math.round(cl) + 6;
        System.out.print("Coleman-Liau index: ");
        System.out.print(cl);
        System.out.println(" (about " + age + "-year-olds).");
    }

    public static double scoreCl(long characters, long words, long sentences) {
        double characterCount = characters;
        double wordCount = words;
        double sentenceCount = sentences;
        double l = 100 * characterCount / wordCount;
        double s = 100 * sentenceCount / wordCount;
        double cl = 0.0588 * l - 0.296 * s - 15.8;

        return Math.round(cl * 100) / 100.0;
    }

    public static void printScoreSmog(double smog) {
        long age = Math.round(smog) + 6;
        System.out.print("Simple Measure of Gobbledygook: ");
        System.out.print(smog);
        System.out.println(" (about " + age + "-year-olds).");
    }

    public static double scoreSmog(long polysyllables, long sentences) {
        double polysyllableCount = polysyllables;
        double sentenceCount = sentences;
        double smog = 1.043 * Math.sqrt(polysyllableCount * 30 / sentenceCount) + 3.1291;

        return Math.round(smog * 100) / 100.0;
    }

    public static void printScoreFk(double fk) {
        long age = Math.round(fk) + 6;
        System.out.print("Flesch-Kincaid readability tests: ");
        System.out.print(fk);
        System.out.println(" (about " + age + "-year-olds).");
    }

    public static double scoreFk(long sentences, long words, long syllables) {
        double sentenceCount = sentences;
        double wordCount = words;
        double syllableCout = syllables;
        double fk = 0.39 * (wordCount / sentenceCount) + 11.8 * (syllableCout / wordCount) - 15.59;

        return Math.round(fk * 100) / 100.0;
    }

    public static void printScoreAri(double ari) {
        long age = Math.round(ari) + 6;
        System.out.print("Automated Readability Index: ");
        System.out.print(ari);
        System.out.println(" (about " + age + "-year-olds).");
    }

    public static double scoreAri(long sentences, long words, long characters) {
        double sentenceCount = sentences;
        double wordCount = words;
        double characterCount = characters;
        double ari = 4.71 * (characterCount / wordCount) + 0.5 * (wordCount / sentenceCount) - 21.43;

        return Math.round(ari * 100) / 100.0;
    }

    public static long[] syllables(String input) {
        long polysyllables = 0;
        long syllables = 0;
        for (String word : input.split(" ")) {
            int currentSyllabels = 0;
            word = word.replaceAll("[.,!?]", "");
            for (int i = 0; i < word.length(); i++) {
                if (i == word.length() - 1) {
                    if (String.valueOf(word.charAt(i)).matches("[eE]")) {
                        //nothing
                    } else if (String.valueOf(word.charAt(i)).matches("[aeiouyAEIOUY]")) {
                        currentSyllabels++;
                    }
                } else {
                    if (String.valueOf(word.charAt(i)).matches("[aeiouyAEIOUY]")) {
                        if (String.valueOf(word.charAt(i+1)).matches("[aeiouyAEIOUY]")) {
                            //nothing
                        } else {
                            currentSyllabels++;
                        }
                    }
                }
            }

            if (currentSyllabels > 2) {
                polysyllables++;
            }
            if (currentSyllabels == 0) {
                currentSyllabels = 1;
            }
            syllables += currentSyllabels;

        }
        long[] sylArr = new long[] {syllables, polysyllables};
        return sylArr;
    }
}
