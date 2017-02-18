package com.universalstudios.orlandoresort.controller.userinterface.home;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crittercism.app.Crittercism;
import com.squareup.picasso.Picasso;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.domain.offers.Offer;
import com.universalstudios.orlandoresort.model.network.image.ImageUtils;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.request.RequestQueryParams;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables.OffersTable;

/**
 * {@link RecyclerView.Adapter} that wraps a {@link Cursor} to show  items with an option
 */
public class HomeOffersRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final String TAG = HomeOffersRecyclerAdapter.class.getSimpleName();

	private Cursor mCursor;
	private OnItemClickListener mOnItemClickListener;

	private Picasso mPicasso;
	private UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;
	private final String mImageSizeParam;

	public HomeOffersRecyclerAdapter(Context context, Cursor cursor, HomeOffersRecyclerAdapter.OnItemClickListener onItemClickListener) {
		mCursor = cursor;
		mOnItemClickListener = onItemClickListener;

		// Create the image downloader to get the images
		mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
				CacheUtils.POI_IMAGE_DISK_CACHE_NAME, CacheUtils.POI_IMAGE_DISK_CACHE_MIN_SIZE_BYTES,
				CacheUtils.POI_IMAGE_DISK_CACHE_MAX_SIZE_BYTES);

		mPicasso = new Picasso.Builder(context).loggingEnabled(UniversalOrlandoImageDownloader.SHOW_DEBUG)
				.downloader(mUniversalOrlandoImageDownloader).build();
		mImageSizeParam = ImageUtils.getPoiImageSizeString(context.getResources().getInteger(R.integer.poi_search_image_dpi_shift));
	}

	@Override
	public int getItemCount() {
		if (mCursor != null) {
			return mCursor.getCount();
		}
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		return OffersItemViewHolder.LAYOUT_RES_ID;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
		return new OffersItemViewHolder(layoutInflater.inflate(OffersItemViewHolder.LAYOUT_RES_ID, parent, false));
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		// Let the cursor adapter bind the view for the offer items
		if (mCursor != null) {
			mCursor.moveToPosition(position);

			OffersItemViewHolder holder = (OffersItemViewHolder) viewHolder;

			// Get data from database row
			String title = mCursor.getString(mCursor.getColumnIndex(OffersTable.COL_ALIAS_DISPLAY_NAME));
			String description = mCursor.getString(mCursor.getColumnIndex(OffersTable.COL_ALIAS_SHORT_DESCRIPTION));
			String imageUrl = mCursor.getString(mCursor.getColumnIndex(OffersTable.COL_ALIAS_THUMBNAIL_IMAGE_URL));
			final String offerObjectJson = mCursor.getString(mCursor.getColumnIndex(OffersTable.COL_OFFER_OBJECT_JSON));

			holder.titleText.setText(title);
			holder.descriptionText.setText(description);

			// Assume there is no image to start
			holder.thumbnailImage.setVisibility(View.GONE);

			// Load the list image
			if (imageUrl != null && !imageUrl.isEmpty()) {
				Uri listImageUri = null;
				try {
					listImageUri = Uri.parse(imageUrl);
				} catch (Exception e) {
					if (BuildConfig.DEBUG) {
						Log.e(TAG, "onBindViewHolder: invalid image URL: " + imageUrl, e);
					}

					// Log the exception to crittercism
					Crittercism.logHandledException(e);
				}

				if (listImageUri != null) {
					Uri imageUriToLoad = listImageUri.buildUpon()
							.appendQueryParameter(RequestQueryParams.Keys.IMAGE_SIZE, mImageSizeParam)
							.build();

					if (BuildConfig.DEBUG) {
						Log.d(TAG, "onBindViewHolder: imageUriToLoad = " + imageUriToLoad);
					}

					mPicasso.load(imageUriToLoad).into(holder.thumbnailImage, new HomeOfferImageCallback(holder));
				}
			} else {
				holder.thumbnailImage.setVisibility(View.GONE);
			}


			holder.clickableLayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mOnItemClickListener != null) {
						Offer offer = GsonObject.fromJson(offerObjectJson, Offer.class);
						mOnItemClickListener.onOfferItemClicked(offer);
					}
				}
			});
		}
	}

	public void destroy() {
		if (mPicasso != null) {
			mPicasso.shutdown();
		}
		if (mUniversalOrlandoImageDownloader != null) {
			mUniversalOrlandoImageDownloader.destroy();
		}
	}

	public Cursor swapCursor(Cursor newCursor) {
		Cursor oldCursor = mCursor;
		mCursor = newCursor;
		notifyDataSetChanged();
		return oldCursor;
	}

	public interface OnItemClickListener {
		void onOfferItemClicked(Offer offer);
	}

	public static class OffersItemViewHolder extends RecyclerView.ViewHolder {
		public static final int LAYOUT_RES_ID = R.layout.list_home_offer_item;
		View clickableLayout;
		TextView titleText;
		TextView descriptionText;
		ImageView thumbnailImage;

		public OffersItemViewHolder(View itemView) {
			super(itemView);
			clickableLayout = itemView.findViewById(R.id.list_home_offer_item_title_clickable_layout);
			titleText = (TextView) itemView.findViewById(R.id.list_home_offer_item_title);
			descriptionText = (TextView) itemView.findViewById(R.id.list_home_offer_item_description);
			thumbnailImage = (ImageView) itemView.findViewById(R.id.list_home_offer_item_image);
		}
	}
}
