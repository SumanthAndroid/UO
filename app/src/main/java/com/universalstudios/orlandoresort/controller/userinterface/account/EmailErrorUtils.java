package com.universalstudios.orlandoresort.controller.userinterface.account;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.view.email_validation.EmailValidationStatusType;

import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.EmailValidationResult;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.ValidateEmailAddressResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;

/**
 * Utiliy class for handing email entry validation.
 *
 * Created by Tyler Ritchie on 10/6/16.
 */

public class EmailErrorUtils {

    private static final String EMAIL_UNDELIVERABLE_CERTAINTY = "undeliverable";
    private static final String IGNORE_STRING = "Ignore";
    private static final String SPACE = " ";

    /**
     * Returns an error message from Tridion if the result's status type calls for an error message.
     *
     * @param result The result object from the {@link ValidateEmailAddressResponse}
     * @return The error message, or null if there is no error
     */
    public static String getErrorMessageIfExists(EmailValidationResult result, Context context) {
        TridionConfig tridionConfig = IceTicketUtils.getTridionConfig();

        EmailValidationStatusType statusType =
                EmailValidationStatusType.fromStatusString(result.getVerboseOutput());
        switch (statusType) {
            case MAILBOX_DISABLED:
                return tridionConfig.getEr45();
            case MAILBOX_DNE:
                return tridionConfig.getEr46();
            case MAILBOX_FULL:
                return tridionConfig.getEr47();
            case UNREACHABLE:
            case UNRESOLVABLE:
            case ILLEGITIMATE:
            case ROLE_ACCOUNT:
            case TYPE_DOMAIN:
            case DISPOSABLE:
            case TIMEOUT:
                return tridionConfig.getEr49();
            case UNKNOWN:
            case VERIFIED:
            case ACCEPT_ALL:
                // These are good conditions
                break;
        }

        if (isEmailUndeliverable(result)) {
            return tridionConfig.getEr49();
        }

        return null;
    }

    /**
     * Returns true if the given result signals that the email is undeliverable.
     * @param result The given result
     * @return True if the email is undeliverable
     */
    public static boolean isEmailUndeliverable(EmailValidationResult result) {
        return result != null && TextUtils.equals(result.getCertainty(), EMAIL_UNDELIVERABLE_CERTAINTY);
    }

    /**
     * Appends a clickable link to the given text view.
     * Takes in a listener for handling when the link is clicked.
     *
     * @param mainTextView The TextView to append the link text to
     * @param textViewListener The listener to handle when the link text is clicked and other callbacks
     */
    public static void appendIgnoreLinkToText(TextView mainTextView, OnClickListener textViewListener) {
        SpannableString link = makeLinkSpan(IGNORE_STRING, textViewListener);

        // Append the link we created above using a function defined below.
        mainTextView.append(SPACE);
        mainTextView.append(link);

        // Make the link text clickable
        makeLinksFocusable(mainTextView);
    }

    /**
     * Styles the text to be underlined and blue, resembling a link.  This method was chosen instead of another
     * TextView, because the link text needed to appear at the very end of the main text body.
     *
     * @param text The link text to style
     * @param listener The listener to capture clicking
     * @return The styled SpannableString
     */
    private static SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
        SpannableString link = new SpannableString(text);
        link.setSpan(new ClickableString(listener), 0, text.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        link.setSpan(new ForegroundColorSpan(Color.BLUE), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        link.setSpan(new UnderlineSpan(), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return link;
    }

    /**
     * Allows the link text to be focusable within the main TextView.
     * @param tv The main TextView
     */
    private static void makeLinksFocusable(TextView tv) {
        MovementMethod m = tv.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (tv.getLinksClickable()) {
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    /**
     * Represents a string that can be clicked, firing off onClick()
     * callback when doing so.
     */
    private static class ClickableString extends ClickableSpan {
        private View.OnClickListener mListener;
        public ClickableString(View.OnClickListener listener) {
            mListener = listener;
        }
        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }
}
