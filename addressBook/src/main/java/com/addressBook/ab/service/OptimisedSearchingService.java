package com.addressBook.ab.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class OptimisedSearchingService {

    private static final int N_GRAM_SIZE = 3;
    private final Map<String, Set<String>> invertedIndex = new HashMap<>();

    public void indexName(String name, String contactId) {
        Set<String> nGrams = generateNGrams(name.toLowerCase());
        //log.info( Gram set is {},nGrams);
        for (String gram : nGrams) {
            invertedIndex.computeIfAbsent(gram, k -> new HashSet<>()).add(contactId);
        }
    }

    public void removeName(String name, String contactId) {

        Set<String> nGrams = generateNGrams(name.toLowerCase());
        for (String gram : nGrams) {
            Set<String> ids = invertedIndex.get(gram);
            if (ids != null) {
                ids.remove(contactId);
                if (ids.isEmpty()) invertedIndex.remove(gram);
            }
        }
    }

    public Set<String> search(String query) {
        //log.info( query is {},query);
        query = query.toLowerCase();
        if (query.length() < N_GRAM_SIZE) {
            String finalQuery = query;
            return invertedIndex.entrySet().stream()
                    .filter(entry -> entry.getKey().contains(finalQuery))
                    .flatMap(entry -> entry.getValue().stream())
                    .collect(Collectors.toSet());
        }

        Set<String> result = null;
        Set<String> nGrams = generateNGrams(query);
        //log.info( Grams generated are {},nGrams);

        for (String gram : nGrams) {
            Set<String> ids = invertedIndex.get(gram);
            if (ids == null) return Collections.emptySet();
            if (result == null) {
                result = new HashSet<>(ids);
            } else {
                result.retainAll(ids);
            }
        }
        return result != null ? result : Collections.emptySet();
    }

    private Set<String> generateNGrams(String text) {
        Set<String> nGrams = new HashSet<>();
        int len = text.length();
        for (int i = 0; i <= len - N_GRAM_SIZE; i++) {
            nGrams.add(text.substring(i, i + N_GRAM_SIZE));
        }
        return nGrams;
    }
}
