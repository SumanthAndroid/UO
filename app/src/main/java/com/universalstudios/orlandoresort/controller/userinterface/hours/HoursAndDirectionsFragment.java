
package com.universalstudios.orlandoresort.controller.userinterface.hours;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarTitleProvider;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.wallet.UniversalFloatingActionButton;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.datetime.DateTimeUtils;
import com.universalstudios.orlandoresort.model.network.domain.venue.Venue;
import com.universalstudios.orlandoresort.model.network.domain.venue.VenueHours;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.utils.WnWKillSwitchDateUtil;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * 
 * 
 * @author Steven Byle
 */
public class HoursAndDirectionsFragment extends DatabaseQueryFragment implements OnClickListener, OnLongClickListener, ActionBarTitleProvider, ViewTreeObserver.OnGlobalLayoutListener {
	private static final String TAG = HoursAndDirectionsFragment.class.getSimpleName();

	private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";
	private static HashMap<Long, Integer> sVenueImageResIds;

	private int mCalculatedImageHeightDp;
	private int mActionBarTitleResId;
	private DrawerStateProvider mParentDrawerStateProvider;
	private View mOurParksHeader;
	private ViewGroup mParkListLayout;
	private View mCalendarItem;
	private ViewGroup mOrlandoButtonLayout;
	private ViewGroup mHollywoodButtonLayout;
	private TextView mUsoAddressText;
	private TextView mUshAddressText;
	private View mHeroImage;
	private ViewGroup mWnwDirectionsButtonLayout;
	private LinearLayout mOrlandoLocationsContainer;
	private LinearLayout mHollywoodLocationsContainer;
    private TextView mWnwAddressText;
	private LinearLayout mWnwDirectionsContainer;
	private ScrollView mScrollView;
	private UniversalFloatingActionButton mUfabWallet;

	public static HoursAndDirectionsFragment newInstance(int actionBarTitleResId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: actionBarTitleResId = " + actionBarTitleResId);
		}

		// Create a new fragment instance
		HoursAndDirectionsFragment fragment = new HoursAndDirectionsFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		args.putInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID, actionBarTitleResId);

		DatabaseQuery databaseQuery;
		if (BuildConfigUtils.isLocationFlavorHollywood()) {
			databaseQuery = DatabaseQueryUtils.getVenueDatabaseQuery(
					VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD,
					VenuesTable.VAL_VENUE_ID_CITY_WALK_HOLLYWOOD);
		} else {
			if(WnWKillSwitchDateUtil.shouldDisplayContent()) {
				databaseQuery = DatabaseQueryUtils.getVenueDatabaseQuery(
						VenuesTable.VAL_VENUE_ID_ISLANDS_OF_ADVENTURE,
						VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_FLORIDA,
						VenuesTable.VAL_VENUE_ID_CITY_WALK_ORLANDO,
						VenuesTable.VAL_VENUE_ID_WET_N_WILD);
			} else {
				databaseQuery = DatabaseQueryUtils.getVenueDatabaseQuery(
						VenuesTable.VAL_VENUE_ID_ISLANDS_OF_ADVENTURE,
						VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_FLORIDA,
						VenuesTable.VAL_VENUE_ID_CITY_WALK_ORLANDO);
			}
		}

		if (databaseQuery != null) {
			args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
		}
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onAttach");
		}

		// Check if parent fragment (if there is one) implements the interface
		Fragment parentFragment = getParentFragment();
		if (parentFragment != null && parentFragment instanceof DrawerStateProvider) {
			mParentDrawerStateProvider = (DrawerStateProvider) parentFragment;
		}
		// Otherwise, check if parent activity implements the interface
		else if (activity != null && activity instanceof DrawerStateProvider) {
			mParentDrawerStateProvider = (DrawerStateProvider) activity;
		}
		// If neither implements the interface, log a warning
		else if (mParentDrawerStateProvider == null) {
			if (BuildConfig.DEBUG) {
				Log.w(TAG, "onAttach: neither the parent fragment or parent activity implement DrawerStateProvider");
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Default parameters
		Bundle args = getArguments();
		if (args == null) {
			mActionBarTitleResId = -1;
		}
		// Otherwise, set incoming parameters
		else {
			mActionBarTitleResId = args.getInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID);
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			// Track the page view
			AnalyticsUtils.trackPageView(
					AnalyticsUtils.CONTENT_GROUP_PLANNING,
					null, null,
					AnalyticsUtils.CONTENT_SUB_2_HOURS_AND_DIRECTIONS,
					AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
					null, null);
		}

		// Get the smallest (portrait) width in dp
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		float widthDp = displayMetrics.widthPixels / displayMetrics.density;
		float heightDp = displayMetrics.heightPixels / displayMetrics.density;
		float smallestWidthDp = Math.min(widthDp, heightDp);

		// Compute the height based on image aspect ratio 1080x760 @ 480dpi
		mCalculatedImageHeightDp = (int) Math.round(smallestWidthDp * (760.0 / 1080.0));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Set the action bar title, if the drawer isn't open
		if (mParentDrawerStateProvider != null && !mParentDrawerStateProvider.isDrawerOpenAtAll()) {
			Activity parentActivity = getActivity();
			if (parentActivity != null) {
				parentActivity.getActionBar().setTitle(provideTitle());
			}
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_hours_and_directions, container, false);
		// Setup Views
		mUfabWallet = (UniversalFloatingActionButton) fragmentView.findViewById(R.id.view_ufab_hours_directions);
		mOurParksHeader = fragmentView.findViewById(R.id.fragment_hours_and_directions_our_parks_header);
		mParkListLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_hours_and_directions_park_list_layout);
		mCalendarItem = fragmentView.findViewById(R.id.fragment_hours_and_directions_calendar_item);
		mUsoAddressText = (TextView) fragmentView.findViewById(R.id.fragment_hours_and_directions_uso_address_text);
		mUshAddressText = (TextView) fragmentView.findViewById(R.id.fragment_hours_and_directions_ush_address_text);
		mOrlandoButtonLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_hours_and_directions_uso_address_directions_button_layout);
		mHollywoodButtonLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_hours_and_directions_ush_address_directions_button_layout);
		mHollywoodLocationsContainer= (LinearLayout) fragmentView.findViewById(R.id.fragment_hours_and_directions_directions_hollywood_containter);
		mOrlandoLocationsContainer= (LinearLayout) fragmentView.findViewById(R.id.fragment_hours_and_directions_directions_orlando_containter);
		mScrollView = (ScrollView) fragmentView.findViewById(R.id.scrollContainer);

		if (BuildConfigUtils.isLocationFlavorHollywood()) {
			mOrlandoLocationsContainer.setVisibility(View.GONE);
			mHollywoodLocationsContainer.setVisibility(View.VISIBLE);
		} else {
			mOrlandoLocationsContainer.setVisibility(View.VISIBLE);
			mHollywoodLocationsContainer.setVisibility(View.GONE);
		}

		mHeroImage = fragmentView.findViewById(R.id.fragment_hours_and_directions_hero_image);

		mCalendarItem.setOnClickListener(this);
		mOrlandoButtonLayout.setOnClickListener(this);
		mHollywoodButtonLayout.setOnClickListener(this);
		mUsoAddressText.setOnLongClickListener(this);
		mUshAddressText.setOnLongClickListener(this);

		// Wet n Wild
		mWnwAddressText = (TextView) fragmentView.findViewById(R.id.fragment_hours_and_directions_wnw_address_text);
		mWnwDirectionsButtonLayout = (ViewGroup) fragmentView.findViewById(R.id.fragment_hours_and_directions_wnw_address_directions_button_layout);
		mWnwDirectionsContainer = (LinearLayout) fragmentView.findViewById(R.id.fragment_hours_and_directions_wnw_directions_container);

		boolean showWnw = WnWKillSwitchDateUtil.shouldDisplayContent();
		mWnwAddressText.setVisibility(showWnw? View.VISIBLE : View.GONE);
		mWnwDirectionsButtonLayout.setVisibility(showWnw? View.VISIBLE : View.GONE);
		mWnwDirectionsContainer.setVisibility(showWnw? View.VISIBLE : View.GONE);

		mWnwAddressText.setOnLongClickListener(this);
		mWnwDirectionsButtonLayout.setOnClickListener(this);

		// Set image height based on detail image aspect ratio
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int calculatedImageHeightPx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				mCalculatedImageHeightDp, displayMetrics));
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, calculatedImageHeightPx);
		mHeroImage.setLayoutParams(layoutParams);

		return fragmentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mUfabWallet.getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	@Override
	public void onClick(View v) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onClick");
		}

		switch (v.getId()) {
			case R.id.fragment_hours_and_directions_calendar_item:
				// Open the hours calendar page
				startActivity(new Intent(v.getContext(), HoursCalendarActivity.class));
				break;
			case R.id.fragment_hours_and_directions_ush_address_directions_button_layout:
			case R.id.fragment_hours_and_directions_uso_address_directions_button_layout: {
				String latLon;
				if (BuildConfigUtils.isLocationFlavorHollywood()) {
					latLon = getString(R.string.hours_and_direction_ush_lat_lon);
					String latLonLabel = getString(R.string.hours_and_directions_ush_address_name);
				} else {
					latLon = getString(R.string.hours_and_direction_uso_lat_lon);
					String latLonLabel = getString(R.string.hours_and_directions_uso_address_name);
				}

				// View the map, starting from the lat/lon
				//String uriString = "geo:" + latLon;
				String uriString = "http://maps.google.com/maps?daddr=" + latLon;
				try {
					//uriString += "?q=" + URLEncoder.encode(latLon + "(" + latLonLabel + ")", "UTF-8");
					if (BuildConfig.DEBUG) {
						Log.d(TAG, uriString);
					}

					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(uriString));
					if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
						startActivity(intent);

						// Track the event
						AnalyticsUtils.trackEvent(
								null,
								AnalyticsUtils.EVENT_NAME_GET_RESORT_DIRECTIONS,
								null,
								null);
					}
				}
				catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(TAG, "onClick: exception trying to get directions");
					}

					// Log the exception to crittercism
					Crittercism.logHandledException(e);
				}
				break;
			}
			case R.id.fragment_hours_and_directions_wnw_address_directions_button_layout: {
                String latLon = getString(R.string.hours_and_direction_lat_lon_wnw);
				String latLonLabel = getString(R.string.hours_and_directions_address_name_wnw);

                // View the map, starting from the lat/lon
                String uriString = "geo:" + latLon;
                try {
                    uriString += "?q=" + URLEncoder.encode(latLon + "(" + latLonLabel + ")", "UTF-8");
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, uriString);
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(uriString));
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);

                        // Track the event
                        AnalyticsUtils.trackEvent(
								null,
                                AnalyticsUtils.EVENT_NAME_GET_WET_N_WILD_DIRECTIONS,
                                null,
                                null);
                    }

                }
                catch (Exception e) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "onClick: exception trying to get directions");
                    }

                    // Log the exception to crittercism
                    Crittercism.logHandledException(e);
                }
                break;
            }
			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onClick: unknown view clicked");
				}
				break;
		}
	}

	@Override
	public boolean onLongClick(View v) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLongClick");
		}
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

		switch (v.getId()) {
			case R.id.fragment_hours_and_directions_uso_address_text: {
				String address = mUsoAddressText.getText().toString();

				// Copy the address to the clipboard
				ClipData clip = ClipData.newPlainText(getString(R.string.detail_address_clipboard_label), address);
				clipboard.setPrimaryClip(clip);

				UserInterfaceUtils.showToastFromForeground(
						getString( R.string.detail_address_copied_to_clipboard_toast_message), Toast.LENGTH_SHORT, v.getContext());
				return true;
			}
			case R.id.fragment_hours_and_directions_wnw_address_text: {
                String address = mWnwAddressText.getText().toString();

                // Copy the address to the clipboard
                ClipData clip = ClipData.newPlainText(getString(R.string.detail_address_clipboard_label), address);
                clipboard.setPrimaryClip(clip);

                UserInterfaceUtils.showToastFromForeground(
                        getString( R.string.detail_address_copied_to_clipboard_toast_message), Toast.LENGTH_SHORT, v.getContext());
                return true;
            }
			case R.id.fragment_hours_and_directions_ush_address_text: {
				String address = mUshAddressText.getText().toString();

				// Copy the address to the clipboard
				ClipData clip = ClipData.newPlainText(getString(R.string.detail_address_clipboard_label), address);
				clipboard.setPrimaryClip(clip);

				UserInterfaceUtils.showToastFromForeground(
						getString( R.string.detail_address_copied_to_clipboard_toast_message), Toast.LENGTH_SHORT, v.getContext());
				return true;
			}
			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onClick: unknown view long clicked");
				}
				return false;
		}
	}

	@Override
	public String provideTitle() {
		return getString(mActionBarTitleResId);
	}


	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoadFinished");
		}

		switch (loader.getId()) {
			case LOADER_ID_DATABASE_QUERY:
				// Let the parent class check for double loads
				if (checkIfDoubleOnLoadFinished()) {
					if (BuildConfig.DEBUG) {
						Log.i(TAG, "onLoadFinished: ignoring update due to double load");
					}
					return;
				}

				// Get the current date
				Date now = new Date();
				long currentTimeInMs = now.getTime();

				// Get the beginning and end of today
				Calendar calendar = Calendar.getInstance(DateTimeUtils.getParkTimeZone(), Locale.US);
				calendar.setTime(new Date(currentTimeInMs));
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				long begTodayInMs = calendar.getTimeInMillis();

				calendar.add(Calendar.DAY_OF_YEAR, 1);
				long endTodayInMs = calendar.getTimeInMillis();

				// Clear the park list and add the park hours
				mParkListLayout.removeAllViews();

				// Pull out data from the venues
				if (data != null && data.moveToFirst()) {
					do {
						String venueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));
						Long venueId = data.getLong(data.getColumnIndex(VenuesTable.COL_VENUE_ID));

						Venue venue = GsonObject.fromJson(venueObjectJson, Venue.class);
						List<VenueHours> venueHoursList = venue.getHours();

						// Go through the hours to see if it is currently in between any
						boolean foundHoursForToday = false;
						if (venueHoursList != null) {
							for (VenueHours venueHours : venueHoursList) {
								if (venueHours == null) {
									continue;
								}

								Long openTimeUnix = venueHours.getOpenTimeUnix();
								Long closeTimeUnix = venueHours.getCloseTimeUnix();
								if (openTimeUnix == null || openTimeUnix == 0
										|| closeTimeUnix == null || closeTimeUnix == 0) {
									continue;
								}

								// If current time is in between the open hours, use those hours
								if (openTimeUnix * 1000 <= currentTimeInMs
										&& currentTimeInMs < closeTimeUnix * 1000) {

									addParkHoursView(venue, openTimeUnix * 1000, closeTimeUnix * 1000);
									foundHoursForToday = true;
									break;
								}
							}
						}

						// If today's hours were found, continue to the next venue
						if (foundHoursForToday) {
							continue;
						}

						// Otherwise, cycle through the hours again and find hours that open today
						if (venueHoursList != null) {
							for (VenueHours venueHours : venueHoursList) {
								if (venueHours == null) {
									continue;
								}

								Long openTimeUnix = venueHours.getOpenTimeUnix();
								Long closeTimeUnix = venueHours.getCloseTimeUnix();
								String openTimeString = venueHours.getOpenTimeString();
								if (openTimeUnix == null || openTimeUnix == 0
										|| closeTimeUnix == null || closeTimeUnix == 0
										|| openTimeString == null || openTimeString.isEmpty()) {
									continue;
								}

								// If the open time is between the beginning and end of today, use those hours
								if (begTodayInMs <= openTimeUnix * 1000 && openTimeUnix * 1000 < endTodayInMs) {
									addParkHoursView(venue, openTimeUnix * 1000, closeTimeUnix * 1000);
									foundHoursForToday = true;
									break;
								}
							}
						}

						// If no suitable hours we found for this venue, hide the hours text
						if (!foundHoursForToday) {
							addParkHoursView(venue, null, null);
						}
					}
					while (data.moveToNext());
				}

				mOurParksHeader.setVisibility(mParkListLayout.getChildCount() > 0 ? View.VISIBLE : View.GONE);
				break;
			default:
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoaderReset");
		}

		switch (loader.getId()) {
			case LOADER_ID_DATABASE_QUERY:
				// Data is not available anymore, delete reference
				break;
			default:
				break;
		}
	}

	private void addParkHoursView(Venue venue, Long openTimeMs, Long closeTimeMs) {
		if (venue == null) {
			return;
		}

		// Format the park hours to 12-hour ("6PM" or "6:30PM"), park time
		String openTodayText = null;
		if (openTimeMs != null && closeTimeMs != null) {
			SimpleDateFormat sdfOutTimeNoMinutes;
			SimpleDateFormat sdfOutTimeWithMinutes;
			String closeTimeFormatted;
			String openTimeFormatted;

			if (DateTimeUtils.is24HourFormat()) {
				// 24 hour format always shows minutes
				sdfOutTimeNoMinutes = new SimpleDateFormat("HH:mm", Locale.US);
				sdfOutTimeWithMinutes = new SimpleDateFormat("HH:mm", Locale.US);
			} else {
				// Format the park hours to 12-hour ("6PM" or "6:30PM"), park time
				sdfOutTimeNoMinutes = new SimpleDateFormat("ha", Locale.US);
				sdfOutTimeWithMinutes = new SimpleDateFormat("h:mma", Locale.US);
			}

			sdfOutTimeNoMinutes.setTimeZone(DateTimeUtils.getParkTimeZone());
			sdfOutTimeWithMinutes.setTimeZone(DateTimeUtils.getParkTimeZone());

			if (!DateTimeUtils.is24HourFormat() && (openTimeMs / (60 * 1000)) % 60 == 0) {
				openTimeFormatted = sdfOutTimeNoMinutes.format(new Date(openTimeMs));
			} else {
				openTimeFormatted = sdfOutTimeWithMinutes.format(new Date(openTimeMs));
			}

			if (!DateTimeUtils.is24HourFormat() && (closeTimeMs / (60 * 1000)) % 60 == 0) {
				closeTimeFormatted = sdfOutTimeNoMinutes.format(new Date(closeTimeMs));
			} else {
				closeTimeFormatted = sdfOutTimeWithMinutes.format(new Date(closeTimeMs));
			}
			String hoursFormatted = (openTimeFormatted + " - " + closeTimeFormatted).toLowerCase(Locale.US);

			String openTodayFormat = getString(R.string.hours_and_directions_header_open_today);
			openTodayText = String.format(openTodayFormat, hoursFormatted);
		}

		String parkName = venue.getDisplayName();
		View featureView = createParkHoursItemView(
				mParkListLayout, getVenueImageResId(venue.getId()), parkName, openTodayText);
		mParkListLayout.addView(featureView);
	}

	private Integer getVenueImageResId(Long venueId) {
		if (venueId == null) {
			return null;
		}

		if (sVenueImageResIds == null) {
			sVenueImageResIds = new HashMap<Long, Integer>();

			if (BuildConfigUtils.isLocationFlavorHollywood()) {
				sVenueImageResIds.put(VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_HOLLYWOOD, R.drawable.ic_home_map_logo_universal_studios_hollywood);
				sVenueImageResIds.put(VenuesTable.VAL_VENUE_ID_CITY_WALK_HOLLYWOOD, R.drawable.ic_home_map_logo_citywalk_hollywood);
			} else {
				sVenueImageResIds.put(VenuesTable.VAL_VENUE_ID_ISLANDS_OF_ADVENTURE, R.drawable.ic_home_map_logo_islands_of_adventure);
				sVenueImageResIds.put(VenuesTable.VAL_VENUE_ID_UNIVERSAL_STUDIOS_FLORIDA, R.drawable.ic_home_map_logo_universal_studios_florida);
				sVenueImageResIds.put(VenuesTable.VAL_VENUE_ID_CITY_WALK_ORLANDO, R.drawable.ic_home_map_logo_citywalk);
				sVenueImageResIds.put(VenuesTable.VAL_VENUE_ID_WET_N_WILD, R.drawable.ic_home_map_logo_wet_n_wild);
			}
		}

		return sVenueImageResIds.get(venueId);
	}

	private View createParkHoursItemView(ViewGroup parentViewGroup, Integer imageResId,
			String primaryTextString, String secondaryTextString) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());

		ViewGroup featureView = (ViewGroup) inflater.inflate(R.layout.list_park_hours_item, parentViewGroup, false);
		TextView primaryText = (TextView) featureView.findViewById(R.id.list_park_hours_item_primary_text);
		TextView secondaryText = (TextView) featureView.findViewById(R.id.list_park_hours_item_secondary_text);
		View divider = featureView.findViewById(R.id.list_park_hours_item_divider);

		if (primaryTextString != null) {
			primaryText.setText(primaryTextString);
		}
		primaryText.setVisibility(primaryTextString != null ? View.VISIBLE : View.INVISIBLE);

		if (secondaryTextString != null) {
			secondaryText.setText(secondaryTextString);
		}
		secondaryText.setVisibility(secondaryTextString != null ? View.VISIBLE : View.INVISIBLE);

		divider.setVisibility(parentViewGroup.getChildCount() > 0 ? View.VISIBLE : View.GONE);
		return featureView;
	}

	@Override
	public void onGlobalLayout() {
		mUfabWallet.getViewTreeObserver().removeOnGlobalLayoutListener(this);
		int height = mUfabWallet.getHeight();
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mUfabWallet.getLayoutParams();
		height += params.bottomMargin;
		mScrollView.setPadding(mScrollView.getPaddingLeft(), mScrollView.getPaddingTop(), mScrollView.getPaddingRight(), height);
	}
}