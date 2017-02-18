package com.universalstudios.orlandoresort.view.textview;

import android.databinding.BindingAdapter;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Factory of input filters for EditText that can be used via data binding.
 * Add the following to the EditText in the layout XML to use these filters:
 *      app:inputFilters="@{InputFilters.arr(InputFilters.excludeChars("!#$%&*+/=?^_{|}~"), InputFilters.length(10))}"
 * @author tjudkins
 * @since 9/29/2016
 */
public class InputFilters {
    public static InputFilter[] arr(InputFilter... values) {
        return values;
    }

    @BindingAdapter("inputFilters")
    public static void setInputFilters(EditText et, InputFilter[] filters) {
        if (null == filters || filters.length <= 0) return;
        // Apply the filters to control the input
        ArrayList<InputFilter> curInputFilters = new ArrayList<>(Arrays.asList(et.getFilters()));
        for (int i = filters.length-1; i >= 0; i--) {
            curInputFilters.add(filters[i]);
        }
        InputFilter[] newInputFilters = curInputFilters.toArray(new InputFilter[curInputFilters.size()]);
        et.setFilters(newInputFilters);
    }

    public static InputFilter getEmailAddressInputFilter(){
        return excludeChars("+");
    }

    public static InputFilter excludeChars(final String excludedChars) {
        return new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (null == excludedChars || excludedChars.isEmpty()) return source;
                boolean keepOriginal = true;
                if (source instanceof SpannableStringBuilder) {
                    SpannableStringBuilder sourceAsSpannableBuilder = (SpannableStringBuilder)source;
                    for (int i = end - 1; i >= start; i--) {
                        if (excludedChars.contains(source.subSequence(i, i+1))) {
                            sourceAsSpannableBuilder.delete(i, i+1);
                            keepOriginal = false;
                        }
                    }
                    return keepOriginal ? null : source;
                } else {
                    StringBuilder filteredStringBuilder = new StringBuilder();
                    for (int i = start; i < end; i++) {
                        char currentChar = source.charAt(i);
                        if (!excludedChars.contains(source.subSequence(i, i+1))) {
                            filteredStringBuilder.append(currentChar);
                        } else {
                            keepOriginal = false;
                        }
                    }
                    return keepOriginal ? null : filteredStringBuilder.toString();
                }
            }
        };
    }

    public static InputFilter allowedChars(final String allowedChars) {
        return new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                // No allowed chars (shouldn't happen, but need to check)
                if (null == allowedChars || allowedChars.isEmpty()) return null;
                boolean keepOriginal = true;
                if (source instanceof SpannableStringBuilder) {
                    SpannableStringBuilder sourceAsSpannableBuilder = (SpannableStringBuilder)source;
                    for (int i = end - 1; i >= start; i--) {
                        if (!allowedChars.contains(source.subSequence(i, i+1))) {
                            sourceAsSpannableBuilder.delete(i, i+1);
                            keepOriginal = false;
                        }
                    }
                    return keepOriginal ? null : source;
                } else {
                    StringBuilder filteredStringBuilder = new StringBuilder();
                    for (int i = start; i < end; i++) {
                        char currentChar = source.charAt(i);
                        if (allowedChars.contains(source.subSequence(i, i+1))) {
                            filteredStringBuilder.append(currentChar);
                        } else {
                            keepOriginal = false;
                        }
                    }
                    return keepOriginal ? null : filteredStringBuilder.toString();
                }
            }
        };
    }

    public static InputFilter length(int l) {
        return new InputFilter.LengthFilter(l);
    }
}
