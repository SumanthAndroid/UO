package com.universalstudios.orlandoresort.controller.userinterface.checkout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.userinterface.actionbar.ActionBarActivity;
import com.universalstudios.orlandoresort.controller.userinterface.image.PicassoProvider;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;

import org.parceler.Parcels;

/**
 * AddressInformationActivity which will initiate the AddressInformationFragment.
 *
 * Created by Tyler Ritchie on 10/3/16.
 */

public class OrderConfirmationActivity extends ActionBarActivity implements PicassoProvider {
    private static final String ORDER_CONFIRMATION_FRAGMENT_TAG = "order_confirmation_fragment";

    private UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
    private Picasso mPicasso;

    public static Bundle newInstanceBundle(OrderConfirmationInfo orderConfirmationInfo) {
        Bundle args = new Bundle();
        args.putParcelable(OrderConfirmationFragment.KEY_ARG_ORDER_SUMMARY, Parcels.wrap(orderConfirmationInfo));
        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        Bundle args = getIntent().getExtras();
        OrderConfirmationInfo orderConfirmationInfo;
        if (args != null) {
            orderConfirmationInfo = Parcels.unwrap(args.getParcelable(OrderConfirmationFragment.KEY_ARG_ORDER_SUMMARY));
        } else {
            orderConfirmationInfo = new OrderConfirmationInfo();
        }

        // Create the image downloader to get the images
        mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
                CacheUtils.POI_IMAGE_DISK_CACHE_NAME,
                CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
                CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);

        mPicasso = new Picasso.Builder(this)
                .debugging(UniversalOrlandoImageDownloader.SHOW_DEBUG)
                .downloader(mUniversalOrlandoImageDownloader).build();

        if (null == savedInstanceState) {
            addFragment(OrderConfirmationFragment.newInstance(orderConfirmationInfo), ORDER_CONFIRMATION_FRAGMENT_TAG);
        }

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPicasso != null) {
            mPicasso.shutdown();
        }
        if (mUniversalOrlandoImageDownloader != null) {
            mUniversalOrlandoImageDownloader.destroy();
        }
    }

    public void addFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.order_confirmation_container, fragment, tag)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Picasso providePicasso() {
        return mPicasso;
    }
}
