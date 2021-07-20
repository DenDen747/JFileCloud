package com.denesgarda.JFileCloud.util;

import java.util.Arrays;

public class ArrayUtils {
    public static<T> T[] append(T[] array, T value) {
        T[] result = Arrays.copyOf(array, array.length + 1);
        result[result.length - 1] = value;
        return result;
    }
}
