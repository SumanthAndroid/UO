package com.universalstudios.orlandoresort.controller.userinterface.explore.map;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.crittercism.app.Crittercism;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;
import com.squareup.picasso.Downloader.Response;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.R;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.model.network.cache.CacheUtils;
import com.universalstudios.orlandoresort.model.network.image.ImageUtils;
import com.universalstudios.orlandoresort.model.network.image.UniversalOrlandoImageDownloader;
import com.universalstudios.orlandoresort.model.network.service.ServiceEndpointUtils;
import com.universalstudios.orlandoresort.model.state.UniversalAppState;
import com.universalstudios.orlandoresort.model.state.UniversalAppStateManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * 
 * @author Steven Byle
 */
public class MapTileProvider implements TileProvider {
	private static final String TAG = MapTileProvider.class.getSimpleName();

	private static final int BUFFER_SIZE_BYTES = 16 * 1024; // 16 KB

	private final int mTileSizePx;
	private final UniversalOrlandoImageDownloader mUniversalOrlandoImageDownloader;

    private Long mTileSetId = null;

    public MapTileProvider(@Nullable Long tileSetId) {
        Context context = UniversalOrlandoApplication.getAppContext();
        this.mTileSetId = tileSetId;

		// Create the image downloader to get the images
		mUniversalOrlandoImageDownloader = new UniversalOrlandoImageDownloader(
				CacheUtils.MAP_TILE_DISK_CACHE_NAME,
				CacheUtils.MAP_TILE_DISK_CACHE_MIN_SIZE_BYTES,
				CacheUtils.MAP_TILE_DISK_CACHE_MAX_SIZE_BYTES);
		mTileSizePx = ImageUtils.getMapTileSizeInPx(context.getResources().getInteger(R.integer.map_tile_dpi_shift));
	}

	@Override
	public Tile getTile(int x, int y, int zoom) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getTile: zoom = " + zoom + " x = " + x + " y = " + y);
		}

		// Don't try to load tiles beyond the max zoom
		if (zoom > MapUtils.MAX_ZOOM_LEVEL) {
			return NO_TILE;
		}

        // Get the current services URL and tile set for the request
		UniversalAppState universalAppState = UniversalAppStateManager.getInstance();
		String servicesBaseUrl = universalAppState.getServicesBaseUrl();
        if (null == mTileSetId) {
            mTileSetId = universalAppState.getActiveMapTileSetId();
        }
        if (servicesBaseUrl == null || mTileSetId == null) {
            return NO_TILE;
        }

		// Build the map tile request
		Uri imageUri = Uri.parse(servicesBaseUrl).buildUpon()
				.appendEncodedPath(ServiceEndpointUtils.URL_PATH_MAP_TILES)
				.appendPath("" + mTileSetId)
				.appendPath("" + mTileSizePx)
				.appendPath("" + zoom)
				.appendPath("" + x)
				.appendPath("" + y)
				.build();

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "getTile: imageUri = " + imageUri.toString());
		}

		// Attempt to load the tile from the disk cache, if it's not there, then load from the network
		Response response = null;
		try {
			response = mUniversalOrlandoImageDownloader.load(imageUri, false);
		}
		catch (IOException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "getTile: exception trying to load the tile from the network/cache", e);
			}
			// Log the exception to crittercism
			Crittercism.logHandledException(e);

			return NO_TILE;
		}

		// Attempt to parse the image bytes out of the input stream
		InputStream inputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			inputStream = response.getInputStream();
			byteArrayOutputStream = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[BUFFER_SIZE_BYTES];
			while ((nRead = inputStream.read(data, 0, BUFFER_SIZE_BYTES)) != -1) {
				byteArrayOutputStream.write(data, 0, nRead);
			}

			byte[] tileBitmapByteArray = byteArrayOutputStream.toByteArray();
			return new Tile(mTileSizePx, mTileSizePx, tileBitmapByteArray);
		}
		catch (Exception e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "getTile: exception trying to convert image input stream to a byte array", e);
			}

			// Log the exception to crittercism
			Crittercism.logHandledException(e);
			return NO_TILE;
		}
		finally {
			// Close streams
			if (inputStream != null) {
				try {
					inputStream.close();
				}
				catch (IOException ignored) {
				}
			}
			if (byteArrayOutputStream != null) {
				try {
					byteArrayOutputStream.close();
				}
				catch (IOException ignored) {
				}
			}
		}
	}

	public void destroy() {
		if (mUniversalOrlandoImageDownloader != null) {
			mUniversalOrlandoImageDownloader.destroy();
		}
	}
}
