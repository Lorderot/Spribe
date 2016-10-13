package services;

/*Due to lack of information about business logic of this service
 * and absence of any constraints, assume that word is sequence of
 * English letters (a-z,A-Z) with at least one letter and without
 * any splitting symbols such as space.
*/

public interface WordCounterService {
    int count(String word) throws NullPointerException, IllegalArgumentException;

    int getFrequency(String word) throws NullPointerException, IllegalArgumentException;
}
