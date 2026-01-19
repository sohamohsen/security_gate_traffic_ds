package com.research.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class CollectionUtil {

    private CollectionUtil() {
        // Utility class - prevent instantiation
    }

    /**
     * Check if collection is null or empty
     */
    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Check if map is null or empty
     */
    public static boolean isNullOrEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Check if collection is not null and not empty
     */
    public static boolean isNotNullOrEmpty(Collection<?> collection) {
        return !isNullOrEmpty(collection);
    }

    /**
     * Check if map is not null and not empty
     */
    public static boolean isNotNullOrEmpty(Map<?, ?> map) {
        return !isNullOrEmpty(map);
    }

    /**
     * Get first element or null if empty
     */
    public static <T> T firstOrNull(Collection<T> collection) {
        if (isNullOrEmpty(collection)) {
            return null;
        }
        return collection.iterator().next();
    }

    /**
     * Find element matching predicate
     */
    public static <T> T findFirst(Collection<T> collection, Predicate<T> predicate) {
        if (isNullOrEmpty(collection)) {
            return null;
        }

        for (T element : collection) {
            if (predicate.test(element)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Filter collection by predicate
     */
    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        if (isNullOrEmpty(collection)) {
            return List.of();
        }

        return collection.stream()
                .filter(predicate)
                .toList();
    }

    /**
     * Convert collection to comma-separated string
     */
    public static String join(Collection<?> collection, String delimiter) {
        if (isNullOrEmpty(collection)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Object item : collection) {
            if (sb.length() > 0) {
                sb.append(delimiter);
            }
            sb.append(item);
        }

        return sb.toString();
    }

    /**
     * Count elements matching predicate
     */
    public static <T> long count(Collection<T> collection, Predicate<T> predicate) {
        if (isNullOrEmpty(collection)) {
            return 0;
        }

        return collection.stream()
                .filter(predicate)
                .count();
    }

    /**
     * Check if any element matches predicate
     */
    public static <T> boolean any(Collection<T> collection, Predicate<T> predicate) {
        if (isNullOrEmpty(collection)) {
            return false;
        }

        return collection.stream()
                .anyMatch(predicate);
    }

    /**
     * Check if all elements match predicate
     */
    public static <T> boolean all(Collection<T> collection, Predicate<T> predicate) {
        if (isNullOrEmpty(collection)) {
            return true; // Vacuous truth
        }

        return collection.stream()
                .allMatch(predicate);
    }

    /**
     * Get size safely (returns 0 for null)
     */
    public static int safeSize(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

    /**
     * Get size safely for map
     */
    public static int safeSize(Map<?, ?> map) {
        return map == null ? 0 : map.size();
    }
}