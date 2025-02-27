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
     * Template for formatting credential logs.
     * This supports only two placeholders: "{}: {}".
     * Example: "| Username: admin"
     */
    @Setter
    private String credentialMessageTemplate = "| {}: {}";

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

        return IntStream.range(0, objects.length / 2)
                .mapToObj(i -> MessageFormatter.arrayFormat(credentialMessageTemplate,
                        new Object[]{objects[i * 2], objects[i * 2 + 1]}).getMessage())
                .collect(Collectors.joining("\n"));
    }
}
