package services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class WordCounterServiceTest {
    private WordCounterService wordCounterService;
    private String[] forbiddenSymbols;

    @Before
    public void initialize() {
        wordCounterService = new WordCounterServiceImpl();
        forbiddenSymbols = new String[]
                {"!", "$", "^", "&", "?", "1995", "0", " ", "\n",
                        "\t", "+", "-", "\\", "*", "/", "@"};
    }

    @Test(expected = NullPointerException.class)
    public void testCount_NullWord() {
        wordCounterService.count(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCount_EmptyString() {
        wordCounterService.count("");
    }

    @Test
    public void testCount_ArgumentContainsForbiddenSymbols() {
        String word = "something";
        for (String forbiddenSymbol : forbiddenSymbols) {
            boolean passed = false;
            try {
                wordCounterService.count(word + forbiddenSymbol);
            } catch (IllegalArgumentException e) {
                passed = true;
            }
            if (!passed) {
                Assert.fail(wordCounterService.getClass() + ".count(\""
                        + word + forbiddenSymbol + "\") doesn't throw exception");
            }
        }
    }

    @Test
    public void testCountAndGetFrequency_MainFunctionality() {
        int expected = 1_000_000;
        String word = "something";
        for (long i = 0; i < expected; i++) {
            wordCounterService.count(word);
        }
        long result = wordCounterService.getFrequency(word);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCount_IgnoreCase() {
        String word = "SomEWoRd";
        wordCounterService.count(word);
        int result = wordCounterService.getFrequency(word.toLowerCase());
        int expected = 1;
        Assert.assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetFrequency_NullWord() {
        wordCounterService.getFrequency(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFrequency_EmptyString() {
        wordCounterService.getFrequency("");
    }

    @Test
    public void testGetFrequency_ArgumentContainsForbiddenSymbols() {
        String word = "something";
        for (String forbiddenSymbol : forbiddenSymbols) {
            boolean passed = false;
            try {
                wordCounterService.getFrequency(word + forbiddenSymbol);
            } catch (IllegalArgumentException e) {
                passed = true;
            }
            if (!passed) {
                Assert.fail(wordCounterService.getClass() + ".getFrequency(\""
                        + word + forbiddenSymbol + "\") doesn't throw exception");
            }
        }
    }

    @Test
    public void testCount_OneWordThreadSafety() {
        testThreadSafety(new String[]{"something"}, 1000, 2000);
    }

    @Test
    public void testCount_MultipleWordsThreadSafety() {
        String[] distinctWords = new String[]{"One", "two", "three"};
        testThreadSafety(distinctWords, 1000, 2000);
    }

    @Test
    public void testCount_HighLoad() {
        String[] distinctWords = new String[]{"One", "two", "three",
                "four", "five", "six", "seven", "eight", "nine"};
        testThreadSafety(distinctWords, 10000, 2000);
    }

    private void testThreadSafety(String[] distinctWords, int numberOfThreads,
                                  int numberOfCalls) {
        Thread[] thread = new Thread[numberOfThreads];
        CyclicBarrier cyclicBarrier = new CyclicBarrier(numberOfThreads);
        Runnable threadCode = () -> {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < numberOfCalls; i++) {
                for (String word : distinctWords) {
                    wordCounterService.count(word);
                }
            }
        };
        try {
            for (int i = 0; i < numberOfThreads; i++) {
                thread[i] = new Thread(threadCode);
                thread[i].start();
            }
            for (int i = 0; i < numberOfThreads; i++){
                thread[i].join();
            }
            int expected = numberOfCalls * numberOfThreads;
            for (String word : distinctWords) {
                int result = wordCounterService.getFrequency(word);
                Assert.assertEquals(expected, result);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}