/**
 * 
 */
package com.universalstudios.orlandoresort.controller.userinterface.news;

import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQuery;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryFragment;
import com.universalstudios.orlandoresort.controller.userinterface.data.DatabaseQueryUtils;
import com.universalstudios.orlandoresort.controller.userinterface.data.LoaderUtils;
import com.universalstudios.orlandoresort.controller.userinterface.detail.ImageDetailFragmentPagerAdapter;
import com.universalstudios.orlandoresort.controller.userinterface.image.ImagePagerUtils;
import com.universalstudios.orlandoresort.controller.userinterface.image.ImagePagerUtils.PagerDotColor;
import com.universalstudios.orlandoresort.controller.userinterface.image.PicassoProvider;
import com.universalstudios.orlandoresort.controller.userinterface.link.DeepLinkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.notification.NotificationUtils;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.domain.news.News;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.NewsTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.PointsOfInterestTable;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.VenuesTable;
import com.universalstudios.orlandoresort.view.fonts.TextView;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager;
import com.universalstudios.orlandoresort.view.viewpager.JazzyViewPager.TransitionEffect;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 
 * @author Steven Byle
 */
public class NewsDetailFragment extends DatabaseQueryFragment implements OnPageChangeListener,
        PicassoProvider, OnClickListener {
	private static final String TAG = NewsDetailFragment.class.getSimpleName();

	private static final String KEY_STATE_CUR_VIEWPAGER_TAB = "KEY_STATE_CUR_VIEWPAGER_TAB";
	private static final String KEY_LOADER_ARG_DATABASE_QUERY_JSON = "KEY_LOADER_ARG_DATABASE_QUERY_JSON";
	private static final int LOADER_ID_DEEP_LINK_QUERY = LoaderUtils.LOADER_ID_NEWS_DETAIL_FRAGMENT;
    private static final String EVENT_SERIES_DETAIL = "EventSeriesDetail";

	private int mCurrentViewPagerTab;
	private int mCalculatedImageHeightDp;
	private TextView mDateText;
	private TextView mMessageHeadingText;
	private TextView mMessageBodyText;
	private RelativeLayout mViewPagerContainer;
	private JazzyViewPager mViewPager;
	private LinearLayout mPagerDotContainer;
	private View mBottomGradient;
	private ImageDetailFragmentPagerAdapter mImageDetailFragmentPagerAdapter;
	private UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
	private Picasso mPicasso;
	private boolean mHasLoadedImages;
	private View mDeepLinkButton;
	private View mDeepLinkButtonBar;
	private TextView mDeepLinkTextView;
	private News mNews;

	public static NewsDetailFragment newInstance(long newsId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: newsId = " + newsId);
		}

		// Create a new fragment instance
		NewsDetailFragment fragment = new NewsDetailFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}

		// Add parameters to the argument bundle
		DatabaseQuery databaseQuery = DatabaseQueryUtils.getNewsDatabaseQuery(newsId);
		args.putString(KEY_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());

		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onAttach");
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
		}
		// Otherwise, set incoming parameters
		else {
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
		}
		// Otherwise restore state, overwriting any passed in parameters
		else {
		}

		// Get the smallest (portrait) width in dp
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		float widthDp = displayMetrics.widthPixels / displayMetrics.density;
		float heightDp = displayMetrics.heightPixels / displayMetrics.density;
		float smallestWidthDp = Math.min(widthDp, heightDp);

		// Compute the height based on image aspect ratio 1080x760 @ 480dpi
		mCalculatedImageHeightDp = (int) Math.round(smallestWidthDp * (760.0 / 1080.0));

		// Set the action bar title
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			ActionBar actionBar = parentActivity.getActionBar();
			actionBar.setTitle(R.string.action_title_park_notification);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Inflate the fragment layout into the container
		View fragmentView = inflater.inflate(R.layout.fragment_news_detail, container, false);

		// Setup Views
		mDateText = (TextView) fragmentView.findViewById(R.id.fragment_news_detail_date_text);
		mMessageHeadingText = (TextView) fragmentView.findViewById(R.id.fragment_news_detail_message_heading_text);
		mMessageBodyText = (TextView) fragmentView.findViewById(R.id.fragment_news_detail_message_body_text);
		mViewPagerContainer = (RelativeLayout) fragmentView.findViewById(R.id.fragment_news_detail_viewpager_container);
		mViewPager = (JazzyViewPager) fragmentView.findViewById(R.id.fragment_news_detail_viewpager);
		mPagerDotContainer = (LinearLayout) fragmentView.findViewById(R.id.fragment_news_detail_dot_layout);
		mBottomGradient = fragmentView.findViewById(R.id.fragment_news_detail_bottom_gradient);
		mDeepLinkButtonBar = fragmentView.findViewById(R.id.fragment_news_detail_bottom_action_bar);
		mDeepLinkButton = fragmentView.findViewById(R.id.fragment_news_detail_deep_link_button);
		mDeepLinkButton.setOnClickListener(this);
		mDeepLinkTextView = (TextView) fragmentView.findViewById(R.id.fragment_news_detail_deep_link_textview);

		// Hide the views until they are binded to
		mDateText.setVisibility(View.INVISIBLE);
		mMessageHeadingText.setVisibility(View.INVISIBLE);
		mMessageBodyText.setVisibility(View.INVISIBLE);
		mPagerDotContainer.setVisibility(View.GONE);
		mBottomGradient.setVisibility(View.GONE);
		mDeepLinkButtonBar.setVisibility(View.GONE);

		// Set pager height based on detail image aspect ratio
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int calculatedImageHeightPx = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				mCalculatedImageHeightDp, displayMetrics));
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mViewPagerContainer.getLayoutParams();
		layoutParams.width = LayoutParams.MATCH_PARENT;
		layoutParams.height = calculatedImageHeightPx;
		mViewPagerContainer.setLayoutParams(layoutParams);

		mViewPager.setOnPageChangeListener(this);
		mViewPager.setTransitionEffect(TransitionEffect.Standard);
		mViewPager.setFadeEnabled(false);

		// Create the image downloader to get the images
		mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
				CacheUtils.POI_IMAGE_DISK_CACHE_NAME,
				CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
				CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);

		mPicasso = new Picasso.Builder(getActivity())
		.debugging(UniversalOrlandoImageDownloader.SHOW_DEBUG)
		.downloader(mUniversalOrlandoImageDownloader)
		.build();

		mHasLoadedImages = false;

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			mCurrentViewPagerTab = mViewPager.getCurrentItem();
		}
		// Otherwise, restore state
		else {
			mCurrentViewPagerTab = savedInstanceState.getInt(KEY_STATE_CUR_VIEWPAGER_TAB);
		}

		return fragmentView;
	}
	
    @Override
    public void onClick(View v) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onClick");
        }

        switch (v.getId()) {
            case R.id.fragment_news_detail_deep_link_button:
                if(mNews != null && DeepLinkUtils.isDatabaseRequired(mNews)) {
                    Bundle loaderArgs = new Bundle();
                    DatabaseQuery databaseQuery;

                    if(mNews.getLinkDestination().equals(EVENT_SERIES_DETAIL)) {
                        databaseQuery = DatabaseQueryUtils.getEventSeriesDetailDatabaseQuery(mNews.getLinkId());
                    } else{
                        databaseQuery = DatabaseQueryUtils.getDetailDatabaseQuery(mNews.getLinkId());
                    }

                    loaderArgs.putString(KEY_LOADER_ARG_DATABASE_QUERY_JSON, databaseQuery.toJson());
                    LoaderUtils.initFragmentLoaderWithHandler(this, new Bundle(), LOADER_ID_DEEP_LINK_QUERY, loaderArgs);

                } else if (mNews != null) {
                    boolean loadedPage = DeepLinkUtils.loadPage(mNews, getActivity(), getFragmentManager(),
                            ((ViewGroup) this.getView().getParent()).getId());
                    if (!loadedPage) {
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG, "onClick: Unable to load news link: news = " + mNews.toString());
                        }
                        Toast.makeText(getActivity(), getString(R.string.news_view_failed), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "onClick: Unable to load news link");
                    }
                    Toast.makeText(getActivity(), getString(R.string.news_view_failed), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onActivityCreated: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewStateRestored: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStart");
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onResume");
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onPause");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onSaveInstanceState");
		}
		outState.putInt(KEY_STATE_CUR_VIEWPAGER_TAB, mCurrentViewPagerTab);
	}

	@Override
	public void onStop() {
		super.onStop();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStop");
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroyView");
		}

		if (mImageDetailFragmentPagerAdapter != null) {
			mImageDetailFragmentPagerAdapter.destroy();
		}
		if (mPicasso != null) {
			mPicasso.shutdown();
		}
		if (mUniversalOrlandoImageDownloader != null) {
			mUniversalOrlandoImageDownloader.destroy();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onDestroy");
		}
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
	    if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreateLoader");
        }
	    
	    switch(id) {
	        case LOADER_ID_DEEP_LINK_QUERY:
	            String databaseQueryJson = args.getString(KEY_LOADER_ARG_DATABASE_QUERY_JSON);
                DatabaseQuery databaseQuery = GsonObject.fromJson(databaseQueryJson, DatabaseQuery.class);
                return LoaderUtils.createCursorLoader(databaseQuery);
	    }
	    
	    return super.onCreateLoader(id, args);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onLoadFinished");
		}

		switch (loader.getId()) {
			case LOADER_ID_DATABASE_QUERY: {
				// Let the parent class check for double loads
				if (checkIfDoubleOnLoadFinished()) {
					if (BuildConfig.DEBUG) {
						Log.i(TAG, "onLoadFinished: ignoring update due to double load");
					}
					return;
				}

				// If the news item is found, display it
				if (data != null && data.moveToFirst()) {
					String newObjectJson = data.getString(data.getColumnIndex(NewsTable.COL_NEWS_OBJECT_JSON));
					News news = GsonObject.fromJson(newObjectJson, News.class);
					mNews = news;

					if(mNews != null && mNews.getLinkId() == null) {
						mDeepLinkButton.setVisibility(View.GONE);
					}

					String messageHeading = news.getMessageHeading();
					String messageBody = news.getMessageBody();
					String formattedStartDate = NewsUtils.getFormattedDate(news.getStartDateInMillis(), getResources());
					Long newsId = news.getId();

					mMessageHeadingText.setText(messageHeading != null ? messageHeading : "");
					mMessageHeadingText.setVisibility(messageHeading != null ? View.VISIBLE : View.GONE);
					mMessageBodyText.setText(messageBody != null ? messageBody : "");
					mMessageBodyText.setVisibility(messageBody != null ? View.VISIBLE : View.GONE);
					mDateText.setText(formattedStartDate != null ? formattedStartDate : "");
					mDateText.setVisibility(formattedStartDate != null ? View.VISIBLE : View.GONE);

					// If the news item has not been read before, update it in the database
					Boolean hasBeenRead = news.getHasBeenRead();
					if (hasBeenRead == null || !hasBeenRead.booleanValue()) {
						NewsUtils.updateNewsHasBeenReadInDatabase(news, true,
								getActivity().getContentResolver(), true);
					}
					
                    // If news has deep link, show button bar and set text
                    if (DeepLinkUtils.isLinkAvailable(mNews)) {
                        mDeepLinkButtonBar.setVisibility(View.VISIBLE);
                        mDeepLinkTextView.setText(DeepLinkUtils.getLinkStringResId(mNews));
                    }

					// If there is a notification active for this park news
					// item, dismiss it since the user is viewing the detail
					Activity parentActivity = getActivity();
					if (newsId != null && parentActivity != null) {
						NotificationManager notificationManager =
								(NotificationManager) parentActivity.getSystemService(Context.NOTIFICATION_SERVICE);
						notificationManager.cancel(newsId.toString(), NotificationUtils.NOTIFICATION_ID_NEWS);
					}

					// Only load new images if the data hasn't been loaded before
					if (!mHasLoadedImages) {

						// Try to load valid detail image URIs
						List<Uri> validImageUris = new ArrayList<Uri>();
						List<String> imageUrls = new ArrayList<String>();
						imageUrls.add(news.getDetailImageUrl());

						if (imageUrls != null) {
							for (String imageUrl : imageUrls) {
								if (imageUrl != null) {
									// Only add URIs that can be parsed
									try {
										Uri imageUri = Uri.parse(imageUrl);
										validImageUris.add(imageUri);
									}
									catch (Exception e) {
										if (BuildConfig.DEBUG) {
											Log.e(TAG, "onLoadFinished: invalid image URL: " + imageUrl, e);
										}

										// Log the exception to crittercism
										Crittercism.logHandledException(e);
									}
								}
							}
						}

						// Create the pager adapter to bind images, if any are there
						if (validImageUris.size() > 0) {
							mImageDetailFragmentPagerAdapter = new ImageDetailFragmentPagerAdapter(mViewPager,
									getChildFragmentManager(), validImageUris);
							mViewPager.setAdapter(mImageDetailFragmentPagerAdapter);

							// Clear out the dot pager indicator and add new ones
							mPagerDotContainer.removeAllViews();
							int pageCount = mImageDetailFragmentPagerAdapter.getCount();
							if (pageCount > 1) {
								for (int i = 0; i < pageCount; i++) {
									View pagerDot = ImagePagerUtils.createPagerDotView(
											mPagerDotContainer, i == mCurrentViewPagerTab, PagerDotColor.WHITE);
									mPagerDotContainer.addView(pagerDot);
								}
								mPagerDotContainer.setVisibility(View.VISIBLE);
								mBottomGradient.setVisibility(View.VISIBLE);
							}
							else {
								mPagerDotContainer.setVisibility(View.GONE);
								mBottomGradient.setVisibility(View.GONE);
							}
							mViewPagerContainer.setVisibility(View.VISIBLE);
						}
						// Otherwise, hide the image pager
						else {
							mViewPagerContainer.setVisibility(View.GONE);
						}
					}

					// Track that the data has been loaded once
					mHasLoadedImages = true;

					// Track the page view
					AnalyticsUtils.trackPageView(
							AnalyticsUtils.CONTENT_GROUP_PLANNING,
							AnalyticsUtils.CONTENT_FOCUS_GUEST_SERVICES,
							null,
							AnalyticsUtils.CONTENT_SUB_2_PARK_NOTIFICATIONS + " - " + messageHeading,
							AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
							null, null);
				}
				// If no news item is found, close the activity
				else {
					finishActivity();
				}
				break;
			}
			case LOADER_ID_DEEP_LINK_QUERY: {
			    // Navigate to detail page if POI found
			    if (data != null && data.moveToFirst()) {
			        String objectJson;
		            int poiTypeId = -1;
		            String venueObjectJson;

                    if(mNews.getLinkDestination().equals(EVENT_SERIES_DETAIL)){
                        //if is not a POI and is an event series object instead
                        objectJson = data.getString(data.getColumnIndex(UniversalOrlandoDatabaseTables.EventSeriesTable.COL_EVENT_SERIES_OBJECT_JSON));
                        venueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));

                    } else {
                        objectJson = data.getString(data.getColumnIndex(PointsOfInterestTable.COL_POI_OBJECT_JSON));
                        poiTypeId = data.getInt(data.getColumnIndex(PointsOfInterestTable.COL_POI_TYPE_ID));
                        venueObjectJson = data.getString(data.getColumnIndex(VenuesTable.COL_VENUE_OBJECT_JSON));
                    }

                    boolean pageLoaded = DeepLinkUtils.loadDetailPage(mNews, getActivity(), venueObjectJson, objectJson, poiTypeId);
		            getLoaderManager().destroyLoader(loader.getId());
                    if (!pageLoaded) {
                        if (BuildConfig.DEBUG) {
                            Log.e(TAG, "onLoadFinished: Unable to load page for object: " + objectJson);
                        }
                        Toast.makeText(getActivity(), getString(R.string.news_view_failed), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "onLoadFinished: Unable to load page poi detail");
                    }
                    Toast.makeText(getActivity(), getString(R.string.news_view_failed), Toast.LENGTH_LONG).show();
                }
			    break;
			}
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
				mHasLoadedImages = false;
				break;
			default:
				break;
		}
	}


	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onPageSelected: " + position);
		}

		mCurrentViewPagerTab = position;

		// Set the proper dot on, and the others off
		int pageCount = mPagerDotContainer.getChildCount();
		if (pageCount > 1) {
			for (int i = 0; i < pageCount; i++) {
				View pagerDot = mPagerDotContainer.getChildAt(i);
				if (pagerDot != null) {
					pagerDot.setBackgroundResource(ImagePagerUtils.getPagerDotResId(i == mCurrentViewPagerTab, PagerDotColor.WHITE));
				}
			}
		}
	}

	@Override
	public Picasso providePicasso() {
		return mPicasso;
	}


	private void finishActivity() {
		Activity parentActivity = getActivity();
		if (parentActivity != null) {
			parentActivity.finish();
		}
	}

}
