/**
 *
 */
package com.universalstudios.orlandoresort.controller.userinterface.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.model.network.analytics.CrashAnalyticsUtils;
import com.universalstudios.orlandoresort.view.TintUtils;
import com.universalstudios.orlandoresort.controller.userinterface.account.AccountUtils;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.MessageFormat;

/**
 *
 *
 * @author Steven Byle
 */
public class FeatureListUtils {

	public enum TintType {
		DEFAULT,
		COLORED,
		NO_TINT
	}

	private static final String TAG = FeatureListUtils.class.getSimpleName();

	public static void addPhoneFeature(Activity activity, ViewGroup featureListLayout,
									   String phoneNumberString, String primarySubText, String viewTagPhone,
									   OnLongClickListener onLongClickListener, OnClickListener onClickListener) {

		addPhoneFeature(activity, featureListLayout, phoneNumberString, primarySubText, viewTagPhone,
				onLongClickListener, onClickListener, TintType.DEFAULT);
	}

	public static void addPhoneFeature(Activity activity, ViewGroup featureListLayout,
									   String phoneNumberString, String primarySubText, String viewTagPhone,
									   OnLongClickListener onLongClickListener, OnClickListener onClickListener, TintType tintType) {

		if (phoneNumberString == null || phoneNumberString.isEmpty()) {
			return;
		}

		// Remove all non digit characters
		phoneNumberString = phoneNumberString.replaceAll("[^0-9]", "");

		Long phoneNumberLong = null;
		try {
			phoneNumberLong = Long.parseLong(phoneNumberString);
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "addPhoneFeature: exception parsing phone number to long", e);
			}

			// Log the exception to crittercism
			CrashAnalyticsUtils.logHandledException(e);
		}

		if (phoneNumberLong != null) {
			// Only try to format 10 or 11 digit numbers
			if (phoneNumberLong.toString().length() == 10 || phoneNumberLong.toString().length() == 11) {

				try {
					String formattedPhoneNumber = "";
					if (phoneNumberLong.toString().length() == 10) {
						DecimalFormat phoneDecimalFormat = new DecimalFormat("0000000000");
						String phoneNumberRawString = phoneDecimalFormat.format(phoneNumberLong);

						String[] phoneNumberArray = {
								phoneNumberRawString.substring(0, 3),
								phoneNumberRawString.substring(3, 6),
								phoneNumberRawString.substring(6, 10)
						};

						MessageFormat phoneNumberMessageFormat = new java.text.MessageFormat("({0}) {1}-{2}");
						formattedPhoneNumber = phoneNumberMessageFormat.format(phoneNumberArray);
					}
					// Phone number with 1 extension
					else if (phoneNumberLong.toString().length() == 11 && phoneNumberLong.toString().substring(0, 1).equals("1")) {
						DecimalFormat phoneDecimalFormat = new DecimalFormat("00000000000");
						String phoneNumberRawString = phoneDecimalFormat.format(phoneNumberLong);

						String[] phoneNumberArray = {
								phoneNumberRawString.substring(0, 1),
								phoneNumberRawString.substring(1, 4),
								phoneNumberRawString.substring(4, 7),
								phoneNumberRawString.substring(7, 11)
						};

						MessageFormat phoneNumberMessageFormat = new java.text.MessageFormat("{0} ({1}) {2}-{3}");
						formattedPhoneNumber = phoneNumberMessageFormat.format(phoneNumberArray);
					}

					if (!formattedPhoneNumber.isEmpty()) {

						View featureView = createFeatureItemView(featureListLayout,
								R.drawable.ic_detail_feature_phone, true,
								formattedPhoneNumber, primarySubText, null,
								(featureListLayout.getChildCount() > 0),
								viewTagPhone, onClickListener, tintType);

						Intent intent = new Intent(Intent.ACTION_DIAL);
						intent.setData(Uri.parse("tel:" + formattedPhoneNumber));

						// Only enable clicking if the device can dial phone numbers
						if (intent.resolveActivity(activity.getPackageManager()) != null) {
							featureView.setTag(R.id.key_view_tag_detail_feature_phone_number, intent);
							featureView.setClickable(true);
						}
						else {
							featureView.setClickable(false);
						}

						// Make the phone number long clickable, to copy the text
						featureView.setOnLongClickListener(onLongClickListener);
						featureView.setLongClickable(true);

						featureListLayout.addView(featureView);
					}
				}
				catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(TAG, "addPhoneFeature: exception formatting phone number", e);
					}

					// Log the exception to crittercism
					CrashAnalyticsUtils.logHandledException(e);
				}
			}
		}
	}

	public static void addTwitterWithHashtagFeature(Activity activity, ViewGroup featureListLayout,
													String twitterUrl, String hastTagToDisplay, String primarySubText, String viewTag,
													OnLongClickListener onLongClickListener, OnClickListener onClickListener,
													TintType tintType) {
        if (TextUtils.isEmpty(twitterUrl) || TextUtils.isEmpty(hastTagToDisplay)) {
            return;
        }

        if (!URLUtil.isNetworkUrl(twitterUrl)) {
            return;
        }

        View featureView = createFeatureItemView(featureListLayout,
                R.drawable.ic_twitter, true, hastTagToDisplay, primarySubText,
                null, (featureListLayout.getChildCount() > 0), viewTag, onClickListener, tintType);

        Intent hashTagIntent = new Intent(Intent.ACTION_VIEW);
        hashTagIntent.setData(Uri.parse(twitterUrl));

        if (hashTagIntent.resolveActivity(activity.getPackageManager()) != null) {
            featureView.setTag(R.id.key_view_tag_detail_feature_twitter_hashtag, hashTagIntent);
            featureView.setClickable(true);
        } else {
            featureView.setClickable(false);
        }

        // Make the phone number long clickable, to copy the text
        featureView.setOnLongClickListener(onLongClickListener);
        featureView.setLongClickable(true);

        featureListLayout.addView(featureView);
	}

	public static void addFeedbackFeature(Activity activity, ViewGroup featureListLayout,
										  String textToDisplay, String primarySubText, String viewTag,
										  OnClickListener onClickListener, TintType tintType) {
		if (TextUtils.isEmpty(textToDisplay)) {
			return;
		}

		View featureView = createFeatureItemView(featureListLayout,
				R.drawable.ic_detail_feature_feedback, true, textToDisplay, primarySubText,
				null, (featureListLayout.getChildCount() > 0), viewTag, onClickListener, tintType);

		featureListLayout.addView(featureView);
	}



	/**
	 * Creates and adds the email feature view
	 *
	 * @param activity Calling Activity
	 * @param featureListLayout ViewGroup to add this to
	 * @param emailString The email address
	 * @param primaryDisplayText What to display instead of the email address
	 *                           null or empty String here will show the email address
	 * @param primarySubText Subtext to show
	 * @param viewTagPhone String tag for view
	 * @param onLongClickListener This View's longClickListener
     * @param onClickListener This View's clickListener
     */
	public static void addEmailFeature(Activity activity, ViewGroup featureListLayout,
									   String emailString, String primaryDisplayText, String primarySubText, String emailSubject, String viewTagPhone,
									   OnLongClickListener onLongClickListener, OnClickListener onClickListener, TintType tintType) {
		if (TextUtils.isEmpty(emailString)) {
			return;
		}

		boolean isValidEmail = AccountUtils.isValidEmail(emailString);
		if (!isValidEmail) {
			return;
		}

		View featureView = createFeatureItemView(featureListLayout,
				R.drawable.ic_detail_feature_email, true,
				TextUtils.isEmpty(primaryDisplayText) ? emailString : primaryDisplayText, primarySubText, null,
				(featureListLayout.getChildCount() > 0),
				viewTagPhone, onClickListener, tintType);

		Uri emailExtras = Uri.fromParts("mailto", emailString, null);
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, emailExtras);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);

		// Only enable clicking if the device can send emails
		if (emailIntent.resolveActivity(activity.getPackageManager()) != null) {
			featureView.setTag(R.id.key_view_tag_detail_feature_email_address, emailIntent);
			featureView.setClickable(true);
		} else {
			featureView.setClickable(false);
		}

		// Make the phone number long clickable, to copy the text
		featureView.setOnLongClickListener(onLongClickListener);
		featureView.setLongClickable(true);

		featureListLayout.addView(featureView);
	}

	public static View createFeatureItemView(ViewGroup parentViewGroup, Integer iconResId, boolean useSmallerIcon,
			Integer primaryTextResId, Integer primarySubTextResId, Integer secondaryTextResId, boolean showTopDivider, String tag,
			OnClickListener onClickListener) {

		return createFeatureItemView(parentViewGroup, iconResId, useSmallerIcon,
				primaryTextResId, primarySubTextResId, secondaryTextResId, showTopDivider, tag, onClickListener, TintType.DEFAULT);
	}

	public static View createFeatureItemView(ViewGroup parentViewGroup, Integer iconResId, boolean useSmallerIcon,
			Integer primaryTextResId, Integer primarySubTextResId, Integer secondaryTextResId, boolean showTopDivider, String tag,
			OnClickListener onClickListener, TintType tintType) {
		Context context = parentViewGroup.getContext();

		String primaryText = primaryTextResId != null ? context.getString(primaryTextResId) : null;
		String primarySubText = primarySubTextResId != null ? context.getString(primarySubTextResId) : null;
		String secondaryText = secondaryTextResId != null ? context.getString(secondaryTextResId) : null;

		return createFeatureItemView(parentViewGroup, iconResId, useSmallerIcon,
				primaryText, primarySubText, secondaryText, showTopDivider, tag, onClickListener, tintType);
	}

	public static View createFeatureItemListView(ViewGroup parentViewGroup, Integer iconResId, boolean useSmallerIcon,
			String primaryTextRes, Integer primarySubTextResId, Integer secondaryTextResId, boolean showTopDivider, String tag,
			OnClickListener onClickListener) {
		Context context = parentViewGroup.getContext();

		String primaryText = primaryTextRes != null ? primaryTextRes : null;
		String primarySubText = primarySubTextResId != null ? context.getString(primarySubTextResId) : null;
		String secondaryText = secondaryTextResId != null ? context.getString(secondaryTextResId) : null;

		return createFeatureItemView(parentViewGroup, iconResId, useSmallerIcon,
				primaryText, primarySubText, secondaryText, showTopDivider, tag, onClickListener);
	}

	public static View createFeatureItemView(ViewGroup parentViewGroup, Integer iconResId, boolean useSmallerIcon,
			String primaryTextString, String primarySubTextString,
			String secondaryTextString, boolean showTopDivider, Object tag,
			OnClickListener onClickListener) {
		return createFeatureItemView(parentViewGroup, iconResId, useSmallerIcon, primaryTextString,
				primarySubTextString, secondaryTextString, showTopDivider, tag, onClickListener, TintType.DEFAULT);
	}

	public static View createFeatureItemView(ViewGroup parentViewGroup, Integer iconResId, boolean useSmallerIcon,
			String primaryTextString, String primarySubTextString,
			String secondaryTextString, boolean showTopDivider, Object tag,
			OnClickListener onClickListener, TintType tintType) {
		Context context = parentViewGroup.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);

		ViewGroup featureView = (ViewGroup) inflater.inflate(R.layout.list_feature_item, parentViewGroup, false);
		ImageView iconImage = (ImageView) featureView.findViewById(R.id.list_feature_item_icon_image);
		TextView primaryText = (TextView) featureView.findViewById(R.id.list_feature_item_primary_text);
		TextView primarySubText = (TextView) featureView.findViewById(R.id.list_feature_item_primary_sub_text);
		TextView secondaryText = (TextView) featureView.findViewById(R.id.list_feature_item_secondary_text);
		View topDivider = featureView.findViewById(R.id.list_feature_item_top_divider);

		if (iconResId != null) {
			switch (tintType) {
				case DEFAULT:
					int defaultColor = ContextCompat.getColor(context, R.color.feature_icon);
					Drawable defaultDrawable = TintUtils.tintDrawable(ContextCompat.getDrawable(context,
							iconResId), defaultColor);
					iconImage.setImageDrawable(defaultDrawable);
					break;
				case COLORED:
					int coloredColor = ContextCompat.getColor(context, R.color.feature_icon_colored);
					Drawable coloredDrawable = TintUtils.tintDrawable(ContextCompat.getDrawable(context,
							iconResId), coloredColor);
					iconImage.setImageDrawable(coloredDrawable);
					break;
				case NO_TINT:
					iconImage.setImageResource(iconResId);
					break;
			}

		}
		int iconDimenInPx = context.getResources().getDimensionPixelSize(useSmallerIcon ?
				R.dimen.detail_feature_small_icon_size : R.dimen.detail_feature_normal_icon_size);
		int iconMarginRightInPx = context.getResources().getDimensionPixelSize(R.dimen.detail_feature_icon_margin_right);
		LinearLayout.LayoutParams iconLayoutParams = new LinearLayout.LayoutParams(iconDimenInPx, iconDimenInPx);
		iconLayoutParams.setMargins(0, 0, iconMarginRightInPx, 0);
		iconImage.setLayoutParams(iconLayoutParams);
		iconImage.setVisibility(iconResId != null ? View.VISIBLE : View.GONE);

		if (primaryTextString != null) {
			primaryText.setText(primaryTextString);
		}
		primaryText.setVisibility(primaryTextString != null ? View.VISIBLE : View.GONE);

		if (primarySubTextString != null) {
			primarySubText.setText(primarySubTextString);
		}
		primarySubText.setVisibility(primarySubTextString != null ? View.VISIBLE : View.GONE);

		if (secondaryTextString != null) {
			secondaryText.setText(secondaryTextString);
		}
		secondaryText.setVisibility(secondaryTextString != null ? View.VISIBLE : View.GONE);

		topDivider.setVisibility(showTopDivider ? View.VISIBLE : View.GONE);

		// Set the tag to reference back to clicks
		featureView.setOnClickListener(onClickListener);
		featureView.setTag(tag);
		return featureView;
	}

	public static View createOfferFeatureItemView(ViewGroup parentViewGroup, Picasso picasso, String thumbnailUrl,
            String primaryTextString, String primarySubTextString, boolean showTopDivider, String tag,
 OnClickListener onClickListener) {
        Context context = parentViewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        ViewGroup featureView = (ViewGroup) inflater
                .inflate(R.layout.list_offer_item, parentViewGroup, false);
        TextView primaryText = (TextView) featureView.findViewById(R.id.list_offer_item_primary_text);
        TextView primarySubText = (TextView) featureView.findViewById(R.id.list_offer_item_primary_sub_text);
        View topDivider = featureView.findViewById(R.id.list_offer_item_top_divider);

        ViewHolder holder = new ViewHolder();
        holder.listImage = (ImageView) featureView.findViewById(R.id.list_offer_item_image);
        holder.listImageNoImage = (ImageView) featureView
                .findViewById(R.id.list_offer_item_item_no_image_logo);

        if (primaryTextString != null) {
            primaryText.setText(primaryTextString);
        }
        primaryText.setVisibility(primaryTextString != null ? View.VISIBLE : View.GONE);

        if (primarySubTextString != null) {
            primarySubText.setText(primarySubTextString);
        }
        primarySubText.setVisibility(primarySubTextString != null ? View.VISIBLE : View.GONE);

        topDivider.setVisibility(showTopDivider ? View.VISIBLE : View.GONE);

        // Set the tag to reference back to clicks
        featureView.setOnClickListener(onClickListener);
        featureView.setTag(tag);

        if (!TextUtils.isEmpty(thumbnailUrl)) {
            picasso.load(thumbnailUrl).into(holder.listImage, new ListImageCallback(holder));
        } else {
            holder.listImage.setVisibility(View.INVISIBLE);
            holder.listImageNoImage.setVisibility(View.VISIBLE);
        }
        return featureView;
    }

	public static class ViewHolder {

        public ImageView listImage;
        public ImageView listImageNoImage;
    }

	// Private static class using weak references to prevent leaking a context
    private static final class ListImageCallback implements Callback {

        private final WeakReference<ViewHolder> mViewHolder;

        public ListImageCallback(ViewHolder viewHolder) {
            mViewHolder = new WeakReference<ViewHolder>(viewHolder);
        }

        @Override
        public void onSuccess() {
            ViewHolder holder = mViewHolder.get();
            if (holder != null) {
                if (holder.listImage != null) {
                    holder.listImage.setVisibility(View.VISIBLE);
                }

                if (holder.listImageNoImage != null) {
                    holder.listImageNoImage.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onError() {
            ViewHolder holder = mViewHolder.get();
            if (holder != null) {
                if (holder.listImage != null) {
                    holder.listImage.setVisibility(View.GONE);
                }
                if (holder.listImageNoImage != null) {
                    holder.listImageNoImage.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
