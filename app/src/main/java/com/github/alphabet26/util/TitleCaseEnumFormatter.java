package com.github.alphabet26.util;

import java.util.Locale;

/**
 * Formats enum names in title case, e.g. "Hello Darkness My Old Friend"
 */
public final class TitleCaseEnumFormatter implements EnumFormatter {
    @Override
    public String format(String enumName) {
        String[] parts = enumName.split("_");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            builder
                .append(Character.toUpperCase(parts[i].charAt(0)))
                .append(parts[i].substring(1).toLowerCase(Locale.getDefault()));

            if (i != (parts.length - 1)) {
                builder.append(" ");
            }
        }

        return builder.toString();
    }

    @Override
    public String unformat(String formattedName) {
        String[] parts = formattedName.split(" ");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            builder.append(parts[i].toUpperCase(Locale.getDefault()));

            if (i != (parts.length - 1)) {
                builder.append("_");
            }
        }

        return builder.toString();
    }
}
