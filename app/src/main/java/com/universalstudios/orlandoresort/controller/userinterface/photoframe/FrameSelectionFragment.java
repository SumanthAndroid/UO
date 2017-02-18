package com.universalstudios.orlandoresort.controller.userinterface.photoframe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.model.network.domain.photoframes.PhotoFrame;
import com.universalstudios.orlandoresort.frommergeneedsrefactor.framedcamera.FramedCameraActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 6/13/16.
 * Class: FrameSelectionFragment
 * Class Description: Fragment to show the list of possible photo frames
 */
public class FrameSelectionFragment extends Fragment implements FrameSelectionAdapter.PhotoFrameSelectionAdapterListener {
    public static final String TAG = "FrameSelectionFragment";

    private static final String ARG_FRAMES = "ARG_FRAMES";

    private ArrayList<PhotoFrame> mFrames;
    private RecyclerView mRecyclerView;
    private FrameSelectionAdapter mAdapter;

    public static FrameSelectionFragment newInstance(List<PhotoFrame> frames) {
        FrameSelectionFragment fragment = new FrameSelectionFragment();
        Bundle b = new Bundle();
        b.putSerializable(ARG_FRAMES, (ArrayList) frames);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == savedInstanceState) {
            if (null != getArguments()) {
                mFrames = (ArrayList) getArguments().getSerializable(ARG_FRAMES);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_frame_selection, container, false);

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (null == mAdapter && null != mFrames) {
            mAdapter = new FrameSelectionAdapter(mFrames, getActivity(), this);
        }
        mRecyclerView.setAdapter(mAdapter);
        return layout;
    }

    @Override
    public void onFrameSelected(PhotoFrame frame) {
        startCamera(frame);
    }

    private void startCamera(PhotoFrame frame) {
        if (null == frame) {
            return;
        }
        startActivity(FramedCameraActivity.createIntent(getActivity(), frame.getDetailImageUrl()));
    }
}
