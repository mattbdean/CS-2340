package com.github.alphabet26.util;

/**
 * Formats the names of enum constants (or just constants in general). "Constant case" is defined
 * as all uppercase letters with underscores acting as separators (e.g. {@code MY_CONSTANT}.
 *
 * This interface defines two formats which can be thought of inverses of the other.
 * {@link #format(String)} formats CONSTANT_CASE into another case, and {@link #unformat(String)}
 * does the opposite. Consumers of this interface assume that
 * {@code format(unformat(str)).equals(str)} is true.
 */
public interface EnumFormatter {
    /**
     * Attempts to format the name of an enum (like {@code FOO_BAR} or {@code BAZ}) into
     * something more readable.
     * @param enumName is the enum name
     * @return something more readable
     */
    String format(String enumName);

    /**
     * Takes a formatted enum name and converts it back to its constant name
     * @param formattedName formatted name
     * @return the constant name
     */
    String unformat(String formattedName);
}
