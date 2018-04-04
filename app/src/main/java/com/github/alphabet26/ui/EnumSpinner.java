package com.github.alphabet26.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.github.alphabet26.R;
import com.github.alphabet26.util.EnumFormatter;
import com.github.alphabet26.util.TitleCaseEnumFormatter;

/**
 * The EnumSpinner class is a custom Spinner that shows the values of a specified Enum. An enum
 * class must be specified through the {@code enumClass} property in the XML. Make sure to use the
 * fully qualified name, not its simple name (e.g. "com.example.Foo" instead of "Foo").
 *
 * @param <T> The type that corresponds to the {@code enumClass} attribute
 */
public final class EnumSpinner<T extends Enum<T>> extends AppCompatSpinner {
    private Class<T> enumClass;

    /** All values in the given enum */
    private T[] enumValues;

    /** Transforms values from UPPER_SNAKE_CASE to some other case and vice versa */
    private EnumFormatter formatter;

    /** If true, setAdapter() will work normally. Set to false when an enum class is specified. */
    private boolean setAdapterAllowed = true;

    public EnumSpinner(Context context) {
        super(context);
    }

    @SuppressWarnings("unchecked")
    public EnumSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        // TODO make configurable via attributes or setter
        this.formatter = new TitleCaseEnumFormatter();

        TypedArray a = context.getTheme().obtainStyledAttributes(
            attrs, R.styleable.EnumSpinner, 0, 0);

        String enumClassName;
        try {
            enumClassName = a.getString(R.styleable.EnumSpinner_enumClass);
            if (enumClassName == null) {
                throw new IllegalArgumentException("enumClass not specified");
            }
        } finally {
            a.recycle();
        }

        try {
            this.enumClass = (Class<T>) Class.forName(enumClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Could not find class " + enumClassName, e);
        }

        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException("Not an enum: " + enumClassName);
        }

        this.enumValues = enumClass.getEnumConstants();
        String[] formattedValues = new String[this.enumValues.length];

        for (int i = 0; i < enumValues.length; i++) {
            formattedValues[i] = formatter.format(enumValues[i].name());
        }

        setAdapter(new ArrayAdapter<>(
            context, android.R.layout.simple_spinner_dropdown_item, formattedValues));
        setAdapterAllowed = false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getSelectedItem() {
        Object selected = super.getSelectedItem();

        // There's nothing selected
        if (selected == null) {
            return null;
        }

        // We gave the adapter Strings, the selected value should be a string
        if (!(selected instanceof String)) {
            throw new IllegalStateException("Expected a String, got " + selected);
        }

        T enumValue = null;
        String enumName = this.formatter.unformat((String) selected);

        // Try to identify the value that corresponds with the given enum name
        for (T val : enumValues) {
            if (val.name().equals(enumName)) {
                enumValue = val;
                break;
            }
        }

        if (enumValue == null) {
            throw new IllegalArgumentException("Expected to find enum value " + enumName +
                " in class " + enumClass.getName());
        }

        return enumValue;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        if (setAdapterAllowed) {
            super.setAdapter(adapter);
        } else {
            throw new IllegalStateException(
                "May not manually specify an adapter when given an Enum");
        }
    }
}
