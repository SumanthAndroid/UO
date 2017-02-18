package com.universalstudios.orlandoresort.controller.userinterface.account;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.dialog.FullScreenLoadingView;
import com.universalstudios.orlandoresort.controller.userinterface.ice_tickets.IceTicketUtils;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkFragment;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkUtils;
import com.universalstudios.orlandoresort.controller.userinterface.utils.Toast;
import com.universalstudios.orlandoresort.databinding.FragmentForgotPasswordBinding;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.request.ForgotPasswordRequest;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Account.responses.ForgotPasswordResponse;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.upr_android_406.IBMData.Tridion.ice_tridion.TridionConfig;
import com.universalstudios.orlandoresort.model.network.analytics.AnalyticsUtils;
import com.universalstudios.orlandoresort.model.network.request.NetworkRequest;
import com.universalstudios.orlandoresort.model.network.response.HttpResponseStatus;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 8/9/16.
 * Class: ForgotPasswordFragment
 * Class Description: Fragment for handling forgotten or resetting password
 */
public class ForgotPasswordFragment extends NetworkFragment implements View.OnClickListener {
    public static final String TAG = ForgotPasswordFragment.class.getSimpleName();

    private EditText mEtEmail;
    private Button mBtnSubmit;
    private TridionConfig mTridionConfig;
    private FullScreenLoadingView mLoadingView;

    /**
     * Factory method for creating a new instance of the ForgotPasswordFragment
     *
     * @return new instance of ForgotPasswordFragment
     */
    public static ForgotPasswordFragment newInstance() {

        // Create a new fragment instance
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();

        // Get arguments passed in, if any
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        // Add parameters to the argument
        // bundle here, if any

        // Set the fragment's arguments
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTridionConfig = IceTicketUtils.getTridionConfig();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onAttach");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentForgotPasswordBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_forgot_password, container, false);
        binding.setTridion(mTridionConfig);
        View layout = binding.getRoot();
        mEtEmail = binding.etForgotEmail;
        mBtnSubmit = binding.btnForgotSubmit;

        mBtnSubmit.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (BuildConfig.DEBUG) {
            Log.v(TAG, "onStart");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mBtnSubmit.getId()) {
            if (AccountUtils.isValidEmail(mEtEmail.getText().toString())) {
                showLoadingView();
                ForgotPasswordRequest request = new ForgotPasswordRequest.Builder(this)
                        .setConcurrencyType(NetworkRequest.ConcurrencyType.ASYNCHRONOUS)
                        .setUserId(mEtEmail.getText().toString())
                        .build();
                NetworkUtils.queueNetworkRequest(request);
                NetworkUtils.startNetworkService();
                sendSubmitClickedAnalytics();
            } else {
                Toast.makeText(getContext(), mTridionConfig.getEr6(), Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
        }
    }

    @Override
    public void handleNetworkResponse(NetworkResponse networkResponse) {
        hideLoadingView();
        if (networkResponse instanceof ForgotPasswordResponse) {
            if (null != networkResponse.getHttpStatusCode() &&
                    networkResponse.getHttpStatusCode() == HttpResponseStatus.SUCCESS_OK) {
                Toast.makeText(getContext(), mTridionConfig.getSu3(), Toast.LENGTH_SHORT)
                        .setIconColor(Color.RED)
                        .show();
            } else {
                Toast.makeText(getContext(), mTridionConfig.getEr71(), Toast.LENGTH_LONG)
                        .setIconColor(Color.RED)
                        .show();
            }
        }
    }

    private void showLoadingView() {
        if (null == mLoadingView) {
            mLoadingView = FullScreenLoadingView.show(getActivity().getSupportFragmentManager());
        }
    }

    private void hideLoadingView() {
        if (null != mLoadingView) {
            mLoadingView.dismiss();
            mLoadingView = null;
        }
    }

    private void sendSubmitClickedAnalytics() {
            Map<String, Object> extraData = new HashMap<String, Object>();
            extraData.put(AnalyticsUtils.KEY_EVENTS, "event147,");
            extraData.put(AnalyticsUtils.KEY_EVENT_NAME, "Form Submission");

            AnalyticsUtils.trackPageView(AnalyticsUtils.CONTENT_GROUP_CRM,
                            AnalyticsUtils.CONTENT_FOCUS_ACCOUNT,
                            null,
                            AnalyticsUtils.UO_PAGE_IDENTIFIER_FORGOT_PASSWORD,
                            null,
                            null,
                            extraData);
    }

}
