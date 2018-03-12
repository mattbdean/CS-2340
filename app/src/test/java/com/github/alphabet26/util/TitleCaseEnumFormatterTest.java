package com.github.alphabet26.util;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class TitleCaseEnumFormatterTest {
    private TitleCaseEnumFormatter formatter;

    @Before
    public void setUp() {
        this.formatter = new TitleCaseEnumFormatter();
    }

    @Test
    public void shouldSatisfyContract() {
        String[] testStrings = new String[] { "FOO", "FOO_BAR", "FOO_BAR_BAZ" };

        for (String testString : testStrings) {
            assertThat(formatter.unformat(formatter.format(testString))).isEqualTo(testString);
        }
    }

    @Test
    public void format_shouldMakeNameTitleCase() {
        assertThat(formatter.format("FOO")).isEqualTo("Foo");
        assertThat(formatter.format("FOO_BAR")).isEqualTo("Foo Bar");
        assertThat(formatter.format("FOO_BAR_BAZ")).isEqualTo("Foo Bar Baz");
    }

    @Test
    public void unformat_shouldMakeFormattedStringConstantCase() {
        assertThat(formatter.unformat("Foo")).isEqualTo("FOO");
        assertThat(formatter.unformat("foo")).isEqualTo("FOO");
        assertThat(formatter.unformat("Foo Bar")).isEqualTo("FOO_BAR");
        assertThat(formatter.unformat("Foo Bar Baz")).isEqualTo("FOO_BAR_BAZ");
    }
}
