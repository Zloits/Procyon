package me.zloits.procyon.util;

import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.slf4j.helpers.MessageFormatter;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class LogUtil {

    /**
     * Formats credentials as key-value pairs in a structured log format.
     * Each pair is formatted using the `credentialMessageTemplate`.
     *
     * @param objects Key-value pairs (must be even in number)
     * @return A formatted string representing credentials
     * @throws IllegalArgumentException if the number of arguments is odd
     */
    public String formatCredentials(Object... objects)  {
        if (objects.length % 2 != 0) throw new IllegalArgumentException("Unable to print " + Arrays.toString(objects) + ". Arguments must be even.");

        int maxKeyLength = IntStream.range(0, objects.length / 2)
                .map(i -> objects[i * 2].toString().length())
                .max()
                .orElse(0);

        return IntStream.range(0, objects.length / 2)
                .mapToObj(i -> {
                    String key = objects[i * 2].toString();
                    String value = objects[i * 2 + 1].toString();
                    return String.format("%-" + maxKeyLength + "s : %s", key, value);
                })
                .collect(Collectors.joining("\n"));
    }
}
