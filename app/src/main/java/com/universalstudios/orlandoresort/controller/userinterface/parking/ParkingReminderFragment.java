
package com.universalstudios.orlandoresort.controller.userinterface.parking;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.config.BuildConfigUtils;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarTitleProvider;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.DismissableDialogFragment;
import com.universalstudios.orlandoresort.controller.userinterface.drawer.DrawerStateProvider;
import com.universalstudios.orlandoresort.controller.userinterface.global.UserInterfaceUtils;
import com.universalstudios.orlandoresort.controller.userinterface.managers.AppPreferenceManager;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.state.parking.ParkingState;
import com.universalstudios.orlandoresort.model.state.parking.ParkingStateManager;
import com.universalstudios.orlandoresort.view.TintUtils;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.camera.ImageCapture;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.camera.ImageCaptureListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Steven Byle
 */
public class ParkingReminderFragment extends Fragment implements ActionBarTitleProvider, OnItemSelectedListener, View.OnClickListener,
        ImageCaptureListener {
	public static final String TAG = ParkingReminderFragment.class.getSimpleName();
	private static final String KEY_ARG_ACTION_BAR_TITLE_RES_ID = "KEY_ARG_ACTION_BAR_TITLE_RES_ID";
	public static final String ARG_SECTION_TITLE = "ARG_SECTION_TITLE";
	private static final int REQUEST_CODE_SELECT_SECTION = 101;

	private String mBasementPrefix;

	private int mActionBarTitleResId;
	private DrawerStateProvider mParentDrawerStateProvider;
	private Spinner mLevelSpinner;
	private Spinner mRowSpinner;
	private TextView mSectionText;
	private ViewGroup mSectionTextContainer;
	private ArrayAdapter<CharSequence> mLevelAdapter;
	private ArrayAdapter<CharSequence> mRowAdapter;
	private ArrayAdapter<CharSequence> mEmptyAdapter;
	private EditText mNotesEditText;
	private String mSavedSection;
	private String mSavedLevel;
	private String mSavedRow;
	private String mSavedNotes;
	private boolean mRestoredSection;
	private boolean mRestoredLevel;
	private boolean mRestoredRow;
	private ViewGroup mParkingPictureContainer;
	private ImageView mParkingPicturePlaceholder;
	private ImageView mParkingPicture;
	private Map<CharSequence, String> mParkingLevelSuffix;

	private String mParkingImageLoc;

	public static ParkingReminderFragment newInstance(int actionBarTitleResId, String sectionTitle) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "newInstance: actionBarTitleResId = " + actionBarTitleResId);
		}

		// Create a new fragment instance
		ParkingReminderFragment fragment = new ParkingReminderFragment();

		// Get arguments passed in, if any
		Bundle args = fragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		args.putInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID, actionBarTitleResId);
		args.putString(ARG_SECTION_TITLE, sectionTitle);
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

		mBasementPrefix = getString(R.string.parking_reminder_basement_prefix);

		// Default parameters
		Bundle args = getArguments();
		if (args == null) {
			mActionBarTitleResId = -1;
		}
		// Otherwise, set incoming parameters
		else {
			mActionBarTitleResId = args.getInt(KEY_ARG_ACTION_BAR_TITLE_RES_ID);
			mSavedSection = args.getString(ARG_SECTION_TITLE);
		}

		// If this is the first creation, default state variables
		if (savedInstanceState == null) {
			// Track the page view
			AnalyticsUtils.trackPageView(
					AnalyticsUtils.CONTENT_GROUP_PLANNING,
					null, null,
					AnalyticsUtils.CONTENT_SUB_2_PARKING_REMINDER,
					AnalyticsUtils.PROPERTY_NAME_RESORT_WIDE,
					null, null);
		}
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
		View fragmentView = inflater.inflate(R.layout.fragment_parking_reminder, container, false);

		// Setup Views
		mLevelSpinner = (Spinner) fragmentView.findViewById(R.id.fragment_parking_reminder_level_spinner);
		mRowSpinner = (Spinner) fragmentView.findViewById(R.id.fragment_parking_reminder_row_spinner);
		mNotesEditText = (EditText) fragmentView.findViewById(R.id.fragment_parking_reminder_notes_edit_text);
		mSectionText = (TextView) fragmentView.findViewById(R.id.fragment_parking_reminder_section_text);
		mSectionTextContainer = (ViewGroup) fragmentView.findViewById(R.id.fragment_parking_reminder_section_text_container);
		mParkingPictureContainer = (ViewGroup) fragmentView.findViewById(R.id.fragment_parking_reminder_image_container);
		mParkingPicturePlaceholder = (ImageView) fragmentView.findViewById(R.id.fragment_parking_reminder_image_placeholder);
		mParkingPicture = (ImageView) fragmentView.findViewById(R.id.fragment_parking_reminder_image);
		mParkingPictureContainer.setOnClickListener(this);

		mSectionText.setText(mSavedSection);
		mSectionTextContainer.setOnClickListener(this);

		mEmptyAdapter = new ArrayAdapter<CharSequence>(getActivity(),
				R.layout.spinner_selected_item,
				new CharSequence[]{""});
		mEmptyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

		mLevelSpinner.setAdapter(mEmptyAdapter);
		mLevelSpinner.setEnabled(false);
		mRowSpinner.setAdapter(mEmptyAdapter);
		mRowSpinner.setEnabled(false);

		setSpinnerOnItemSelectedListener(mLevelSpinner, this);
		setSpinnerOnItemSelectedListener(mRowSpinner, this);

		// Restore the saved state
		ParkingState parkingState = ParkingStateManager.getInstance();

		if (!TextUtils.isEmpty(parkingState.getSection())) {
			mSavedSection = parkingState.getSection();
			mSavedLevel = parkingState.getLevel();
			mSavedRow = parkingState.getRow();
			restoreParkingState();
		}


		mSavedLevel = parkingState.getLevel();
		if (mSavedLevel == null) {
			mSavedLevel = "";
		}
		mSavedRow = parkingState.getRow();
		if (mSavedRow == null) {
			mSavedRow = "";
		}
		mSavedNotes = parkingState.getNotes();
		if (mSavedNotes == null) {
			mSavedNotes = "";
		}

		mParkingImageLoc = AppPreferenceManager.getParkingReminderImageLocation();
		if (!TextUtils.isEmpty(mParkingImageLoc)) {
			mParkingPicturePlaceholder.setVisibility(View.GONE);
			mParkingPicture.setVisibility(View.VISIBLE);
			Picasso.with(getActivity())
					.load(new File(mParkingImageLoc))
					.into(mParkingPicture);
		}

		mNotesEditText.setText(mSavedNotes);
		mNotesEditText.setSelection(mNotesEditText.length());

		return fragmentView;
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onViewStateRestored: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");
		}

		// Enable action bar items
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_CODE_SELECT_SECTION:
				if (resultCode == Activity.RESULT_OK) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						mSavedSection = bundle.getString(ARG_SECTION_TITLE);
						ParkingState parkingState = ParkingStateManager.getInstance();
						parkingState.setSection(mSavedSection);
						onSectionSelected(mSavedSection);
					}
				}
				break;
			default:
				super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onStop() {
		super.onStop();

		if (BuildConfig.DEBUG) {
			Log.v(TAG, "onStop");
		}

		// Save parking state
		saveParkingState();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreateOptionsMenu");
		}

		inflater.inflate(R.menu.action_discard, menu);
		TintUtils.tintAllMenuItems(menu, getContext());
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onPrepareOptionsMenu");
		}

		// If the menu drawer is open, hide action items related to the main
		// content view, and only show action items specific to the menu
		if (mParentDrawerStateProvider != null) {
			boolean isDrawerOpenAtAll = mParentDrawerStateProvider.isDrawerOpenAtAll();

			MenuItem menuItem;
			menuItem = menu.findItem(R.id.action_discard);
			if (menuItem != null) {
				menuItem.setVisible(!isDrawerOpenAtAll).setEnabled(!isDrawerOpenAtAll);
			}
		}

		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onOptionsItemSelected");
		}

		switch (item.getItemId()) {
			case R.id.action_discard:
				// Confirm before clearing parking state
				FragmentManager fragmentManager = getChildFragmentManager();
				boolean isDialogFragmentShowing =
						fragmentManager.findFragmentByTag(ConfirmClearDialogFragment.class.getSimpleName()) != null;

				if (!isDialogFragmentShowing) {
					closeKeyboard();
					ConfirmClearDialogFragment dialogFragment = new ConfirmClearDialogFragment();
					dialogFragment.show(fragmentManager, dialogFragment.getClass().getSimpleName());
				}
				return true;
			default:
				if (BuildConfig.DEBUG) {
					Log.w(TAG, "onOptionsItemSelected: unknown menu item selected");
				}
				return super.onOptionsItemSelected(item);
		}
	}

	private void restoreParkingState() {
		if (!mRestoredSection) {
			onSectionSelected(mSavedSection);
			mRestoredSection = true;
		}

		ParkingState parkingState = ParkingStateManager.getInstance();

		//set the level spinner value
		if (!TextUtils.isEmpty(parkingState.getLevel())) {
			if (!mRestoredLevel) {
				setSpinnerToValue(mLevelSpinner, parkingState.getLevel());
				mRestoredLevel = true;
			}
		} else if (!TextUtils.isEmpty(parkingState.getRow())) {
			//only set the row spinner here if there is no level value
			//because it will be set in onItemSelected
			if (!mRestoredRow) {
				setSpinnerToValue(mRowSpinner, parkingState.getRow());
				mRestoredRow = true;
			}
		}
	}

	private void onSectionSelected(String section) {
		if (!TextUtils.isEmpty(section)) {
			mSectionText.setText(section);
			updateLevelSpinner(section);
			updateRowSpinner(section, null);
		} else {
			mSectionText.setText("");
			mLevelSpinner.setAdapter(mEmptyAdapter);
			mLevelSpinner.setEnabled(false);
			mRowSpinner.setAdapter(mEmptyAdapter);
			mRowSpinner.setEnabled(false);
		}
	}

	private void updateLevelSpinner(String section) {
		int minLevel = -1;
		int maxLevel = -1;
		int basementLevels = -1;

		if (BuildConfigUtils.isLocationFlavorHollywood()) {
			if (section.equals(getString(R.string.parking_reminder_section_item_frankenstein))) {
				minLevel = 1;
				maxLevel = 7;
			} else if (section.equals(getString(R.string.parking_reminder_section_item_jurassic_park))) {
				minLevel = 1;
				maxLevel = 6;
				basementLevels = 2;
			} else if (section.equals(getString(R.string.parking_reminder_section_item_curious_george))) {
				minLevel = 1;
				maxLevel = 5;
				basementLevels = 2;
			} else if (section.equals(getString(R.string.parking_reminder_section_item_et))) {
				minLevel = 1;
				maxLevel = 7;
				basementLevels = 1;
			}
		} else {
			if (section.equals(getString(R.string.parking_reminder_section_item_jurassic_park))) {
				minLevel = 1;
				maxLevel = 5;
			} else if (section.equals(getString(R.string.parking_reminder_section_item_king_kong))) {
				minLevel = 1;
				maxLevel = 5;
			} else if (section.equals(getString(R.string.parking_reminder_section_item_jaws))) {
				minLevel = 1;
				maxLevel = 5;
			} else if (section.equals(getString(R.string.parking_reminder_section_item_spider_man))) {
				minLevel = 1;
				maxLevel = 6;
			} else if (section.equals(getString(R.string.parking_reminder_section_item_cat_in_the_hat))) {
				minLevel = 1;
				maxLevel = 6;
			} else if (section.equals(getString(R.string.parking_reminder_section_item_et))) {
				minLevel = 1;
				maxLevel = 6;
			}

		}

		if (minLevel != -1) {
			CharSequence[] array;
			if (basementLevels == -1) {
				array = createCharSequenceArray(minLevel, maxLevel);
			} else {
				array = createCharSequenceArray(basementLevels, minLevel, maxLevel);
			}

			if (BuildConfigUtils.isLocationFlavorHollywood()) {
				for (int i = 0; i < array.length; i++) {
					if (!TextUtils.isEmpty(array[i])) {
						String suffix = getParkingLevelSuffix(array[i]);
						if (!TextUtils.isEmpty(suffix)) {
							array[i] = array[i] + " - " + suffix;
						}
					}
				}
			}

			mLevelAdapter = new ArrayAdapter<>(getActivity(),
					R.layout.spinner_selected_item,
					array);
			mLevelAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

			mLevelSpinner.setAdapter(mLevelAdapter);
			mLevelSpinner.setEnabled(true);
		} else {
			setSpinnerSelection(mLevelSpinner, 0);
			mLevelSpinner.setEnabled(false);
		}

	}

	private void updateRowSpinner(String section, String selectedLevel) {
		CharSequence[] rows = null;
		if (BuildConfigUtils.isLocationFlavorHollywood()) {
			char minRow = '0';
			char maxRow = '0';
			if (section.equals(getString(R.string.parking_reminder_section_item_frankenstein))) {
				//A,B,C
				if (!TextUtils.isEmpty(selectedLevel)) {
					minRow = 'A';
					maxRow = 'C';
				}
			} else if (section.equals(getString(R.string.parking_reminder_section_item_jurassic_park))) {
				//J,K,L,M
				if (!TextUtils.isEmpty(selectedLevel)) {
					minRow = 'J';
					maxRow = 'M';
				}
			} else if (section.equals(getString(R.string.parking_reminder_section_item_curious_george))) {
				//P,Q,R
				if (!TextUtils.isEmpty(selectedLevel)) {
					minRow = 'P';
					maxRow = 'R';
				}
			} else if (section.equals(getString(R.string.parking_reminder_section_item_et))) {
				//S,T,U,V,W
				if (!TextUtils.isEmpty(selectedLevel)) {
					minRow = 'S';
					maxRow = 'W';
				}
			} else if (section.equals(getString(R.string.parking_reminder_section_item_king_kong))) {
				//D
				minRow = 'D';
				maxRow = 'D';
			} else if (section.equals(getString(R.string.parking_reminder_section_item_woody_woodpecker))) {
				//E,F,G,H
				minRow = 'E';
				maxRow = 'H';
			}
			if (minRow != '0') {
				rows = createCharSequenceArray(minRow, maxRow);
			}
		} else {
			int minRow = -1;
			int maxRow = -1;
			int level;

			//only set the row adapter if we have a level because all orlando sections have multiple levels
			if (!TextUtils.isEmpty(selectedLevel) && selectedLevel.matches("[0-9]+")) {
				level = Integer.parseInt(selectedLevel);

				if (section.equals(getString(R.string.parking_reminder_section_item_jurassic_park))) {
					if (level == 2) {
						minRow = level * 100;
						maxRow = level * 100 + 5;
					} else {
						minRow = level * 100;
						maxRow = level * 100 + 6;
					}
				} else if (section.equals(getString(R.string.parking_reminder_section_item_king_kong))) {
					if (level == 2) {
						minRow = level * 100 + 6;
						maxRow = level * 100 + 11;
					} else {
						minRow = level * 100 + 7;
						maxRow = level * 100 + 12;
					}
				} else if (section.equals(getString(R.string.parking_reminder_section_item_jaws))) {
					if (level == 1) {
						minRow = level * 100 + 13;
						maxRow = level * 100 + 19;
					} else if (level == 2) {
						minRow = level * 100 + 12;
						maxRow = level * 100 + 17;
					} else {
						minRow = level * 100 + 13;
						maxRow = level * 100 + 18;
					}
				} else if (section.equals(getString(R.string.parking_reminder_section_item_spider_man))) {
					if (level == 2) {
						minRow = level * 100 + 50;
						maxRow = level * 100 + 55;
					} else {
						minRow = level * 100 + 50;
						maxRow = level * 100 + 56;
					}
				} else if (section.equals(getString(R.string.parking_reminder_section_item_cat_in_the_hat))) {
					if (level == 2) {
						minRow = level * 100 + 56;
						maxRow = level * 100 + 60;
					} else {
						minRow = level * 100 + 57;
						maxRow = level * 100 + 61;
					}
				} else if (section.equals(getString(R.string.parking_reminder_section_item_et))) {
					if (level == 2) {
						minRow = level * 100 + 61;
						maxRow = level * 100 + 66;
					} else {
						minRow = level * 100 + 62;
						maxRow = level * 100 + 67;
					}
				}
				rows = createCharSequenceArray(minRow, maxRow);
			}
		}


		if (rows != null) {
			mRowAdapter = new ArrayAdapter<>(getActivity(),
					R.layout.spinner_selected_item,
					rows);
			mRowAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

			mRowSpinner.setAdapter(mRowAdapter);
			mRowSpinner.setEnabled(true);
		} else {
			mRowSpinner.setAdapter(mEmptyAdapter);
			mRowSpinner.setEnabled(false);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "onItemSelected: parent.getId() = " + parent.getId()
					+ " parent.getItemAtPosition(pos) = " + parent.getItemAtPosition(pos));
		}

		switch (parent.getId()) {
			case R.id.fragment_parking_reminder_level_spinner:
				if (mLevelAdapter != null) {
					mSavedLevel = mLevelAdapter.getItem(pos).toString();
					updateRowSpinner(mSavedSection, mSavedLevel);
					if (!mRestoredRow) {
						setSpinnerToValue(mRowSpinner, mSavedRow);
						mRestoredRow = true;
					}
				}
				break;
			case R.id.fragment_parking_reminder_row_spinner:
				if (mRowAdapter != null) {
					mSavedRow = mRowAdapter.getItem(pos).toString();
				}
				break;
		}

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "onNothingSelected: parent.getId() = " + parent.getId());
        }
    }

    @Override
    public String provideTitle() {
        return getString(mActionBarTitleResId);
    }

    private void saveParkingState() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "saveParkingState");
        }

        String selectedSection = mSavedSection;
        if (selectedSection == null) {
            selectedSection = "";
        }

        String selectedLevel = (String) mLevelSpinner.getSelectedItem();
        if (selectedLevel == null) {
            selectedLevel = "";
        }

		String selectedRow = (String) mRowSpinner.getSelectedItem();
		if (selectedRow == null) {
			selectedRow = "";
		}

		String notes = mNotesEditText.getText().toString();
		if (notes == null) {
			notes = "";
		}

		ParkingState parkingState = ParkingStateManager.getInstance();
		parkingState.setGarage(null);
		parkingState.setSection(selectedSection);
		parkingState.setLevel(selectedLevel);
		parkingState.setRow(selectedRow);
		parkingState.setNotes(notes);
		ParkingStateManager.saveInstance();
	}

    private void clearParkingReminder() {
        // Reset spinners to be blank and clear the notes
        mSavedSection = "";
        mLevelSpinner.setSelection(0);
        mRowSpinner.setSelection(0);
        if (mNotesEditText.length() > 0) {
            mNotesEditText.getText().clear();
        }
		mParkingPicturePlaceholder.setVisibility(View.VISIBLE);
		mParkingPicture.setVisibility(View.GONE);
        AppPreferenceManager.setParkingReminderImageLocation("");
    }

    /**
     * Shows the ParkingSectionSelectionFragment to reselect
     * the user's parking section
     */
    private void showSectionsSelectionFragment() {
        Intent intent = new Intent(getContext(), ParkingSectionSelectionActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SELECT_SECTION);
    }

	private void setSpinnerToValue(Spinner spinner, String value) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "setSpinnerToValue: value = " + value);
		}

		SpinnerAdapter spinnerAdapter = spinner.getAdapter();

		// Find the value in the spinner and set it
		for (int i = 0; i < spinnerAdapter.getCount(); i++) {
			String spinnerValue = (String) spinnerAdapter.getItem(i);
			if (value.equalsIgnoreCase(spinnerValue)) {
				setSpinnerSelection(spinner, i);
				return;
			}
		}
	}

	private static void setSpinnerSelection(final Spinner spinner, final int pos) {
		spinner.post(new Runnable() {
			@Override
			public void run() {
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "setSpinnerSelection: spinner.getId() = " + spinner.getId()
							+ " pos = " + pos);
				}
				spinner.setSelection(pos, false);
			}
		});
	}

	private static void setSpinnerOnItemSelectedListener(final Spinner spinner, final OnItemSelectedListener onItemSelectedListener) {
		spinner.post(new Runnable() {
			@Override
			public void run() {
				spinner.setOnItemSelectedListener(onItemSelectedListener);
			}
		});
	}

	private CharSequence[] createCharSequenceArray(char minNum, char maxNum) {
		CharSequence[] array = new CharSequence[(maxNum - minNum) + 2];
		array[0] = "";
		for (int i = 1; i < array.length; i++) {
            array[i] = Character.toString((char) (minNum + (i-1)));
		}
		return array;
	}

    private CharSequence[] createCharSequenceArray(int minNum, int maxNum) {
		CharSequence[] array = new CharSequence[(maxNum - minNum) + 2];
		array[0] = "";
		for (int i = 1; i < array.length; i++) {
			array[i] = "" + (minNum + (i-1));
		}
		return array;
	}

    private CharSequence[] createCharSequenceArray(int numBasements, int minNum, int maxNum) {
        //size = difference between min and max + basement size + 2 (1 for 0 offset, 1 for empty first item)
        CharSequence[] array = new CharSequence[numBasements + (maxNum - minNum) + 2];

        //add empty first item
        array[0] = "";

        //add basement levels in descending order (B3, B2...)
        for (int i = 0; i < numBasements; i++) {
            array[i + 1] = mBasementPrefix + (numBasements - i);
        }

        //add levels starting after basement items
        for (int i = numBasements + 1; i < array.length; i++) {
            array[i] = "" + (minNum + i - numBasements - 1);
        }

        return array;
    }

	public static class ConfirmClearDialogFragment extends DismissableDialogFragment implements DialogInterface.OnClickListener {
		private static final String TAG = ConfirmClearDialogFragment.class.getSimpleName();

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			alertDialogBuilder.setTitle(R.string.parking_reminder_confirm_clear_title);
			alertDialogBuilder.setMessage(R.string.parking_reminder_confirm_clear_message);
			alertDialogBuilder.setPositiveButton(R.string.parking_reminder_confirm_clear_positive_button, this);
			alertDialogBuilder.setNegativeButton(R.string.parking_reminder_confirm_clear_negative_button, this);

			Dialog dialog = alertDialogBuilder.create();
			dialog.setCanceledOnTouchOutside(true);
			return dialog;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					Fragment parentFragment = getParentFragment();
					if (parentFragment != null && parentFragment instanceof ParkingReminderFragment) {
						ParkingReminderFragment parkingReminderFragment = (ParkingReminderFragment) parentFragment;
						parkingReminderFragment.clearParkingReminder();
						parkingReminderFragment.onSectionSelected(null);
					}
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					// Do nothing, just dismiss the dialog
					break;
			}
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			super.onCancel(dialog);

			// Simulate a cancel click
			onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
		}
	}

    private void closeKeyboard() {
        UserInterfaceUtils.closeKeyboard(getActivity().getCurrentFocus());
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case (R.id.fragment_parking_reminder_section_text_container):
				showSectionsSelectionFragment();
				break;
			case (R.id.fragment_parking_reminder_image_container):
				ImageCapture.takePicture(getActivity(), null, this);
		}
	}

    @Override
    public void onImageCaptured(Uri uri) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, uri.toString());
        }

        mParkingImageLoc = uri.toString();
        Picasso.with(getActivity())
                .load(new File(mParkingImageLoc))
                .into(mParkingPicture);

		mParkingPicture.setVisibility(View.VISIBLE);
		mParkingPicturePlaceholder.setVisibility(View.GONE);

		AppPreferenceManager.setParkingReminderImageLocation(mParkingImageLoc);
	}

    @Override
    public void onCaptureFailed() {
        Toast.makeText(getActivity(), "failed", Toast.LENGTH_LONG).show();
    }


	private String getParkingLevelSuffix(CharSequence str){
		if (mParkingLevelSuffix == null){
			mParkingLevelSuffix = new HashMap<>();
			mParkingLevelSuffix.put("B2","Gray");
			mParkingLevelSuffix.put("B1","Pink");
			mParkingLevelSuffix.put("1","Blue");
			mParkingLevelSuffix.put("2","Yellow");
			mParkingLevelSuffix.put("3","Red");
			mParkingLevelSuffix.put("4","Green");
			mParkingLevelSuffix.put("5","Brown");
			mParkingLevelSuffix.put("6","Purple");
			mParkingLevelSuffix.put("7","Orange");
		}

		return mParkingLevelSuffix.get(str);
	}
}
