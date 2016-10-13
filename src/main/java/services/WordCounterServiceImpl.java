package services;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class WordCounterServiceImpl implements WordCounterService{
    public static final String WORD_PATTERN = "[a-zA-Z]+";
    private ConcurrentMap<String, AtomicInteger> wordsContainer;

    public WordCounterServiceImpl() {
        wordsContainer = new ConcurrentHashMap<>();
    }

    public int count(String word) {
        checkWord(word);
        wordsContainer.putIfAbsent(word.toLowerCase(), new AtomicInteger(0));
        AtomicInteger frequency = wordsContainer.get(word.toLowerCase());
        return frequency.incrementAndGet();
    }

    @Override
    public int getFrequency(String word) {
        checkWord(word);
        AtomicInteger frequency = wordsContainer.get(word.toLowerCase());
        if (frequency == null) {
            return 0;
        }
        return frequency.get();
    }

    private void checkWord(String word) {
        if (word == null) {
            throw new NullPointerException();
        }
        if (!word.matches(WORD_PATTERN)) {
            throw new IllegalArgumentException();
        }
    }
}

