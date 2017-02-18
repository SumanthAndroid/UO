package com.universalstudios.orlandoresort.model.network.domain.photoframes;

import android.util.Log;

import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.userinterface.network.NetworkRequestSender;
import com.universalstudios.orlandoresort.model.network.domain.global.UniversalOrlandoServicesRequest;
import com.universalstudios.orlandoresort.model.network.domain.preloadingdb.SyncPhotoFrameWithDatabase;
import com.universalstudios.orlandoresort.model.network.request.BaseNetworkRequestBuilder;
import com.universalstudios.orlandoresort.model.network.service.ServiceEndpointUtils;
import com.universalstudios.orlandoresort.model.network.service.UniversalOrlandoServices;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 6/19/16.
 * Class: GetPhotoFrameExperienceRequest
 * Class Description: Request to get PhotoFrameExperiences
 */
public class GetPhotoFrameExperienceRequest extends UniversalOrlandoServicesRequest implements Callback<GetPhotoFramesExperienceResponse> {
    public static final String TAG = "GtPhtFrmeExprnceRqst";

    public static final String PAGE_SIZE_ALL = "All";


    private GetPhotoFrameExperienceRequest(String senderTag, Priority priority, ConcurrencyType concurrencyType) {
        super(senderTag, priority, concurrencyType, null);
    }

    public static class Builder extends BaseNetworkRequestBuilder<Builder> {

        public Builder(NetworkRequestSender sender) {
            super(sender);
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public GetPhotoFrameExperienceRequest build() {
            return new GetPhotoFrameExperienceRequest(senderTag, priority, concurrencyType);
        }
    }

    @Override
    public void makeNetworkRequest(RestAdapter restAdapter) {
        super.makeNetworkRequest(restAdapter);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "makeNetworkRequest");
        }

        UniversalOrlandoServices services = restAdapter.create(UniversalOrlandoServices.class);

        switch (getConcurrencyType()) {
            case SYNCHRONOUS:
                try {
                    GetPhotoFramesExperienceResponse response = services.getPhotoFramesExperiences(
                            ServiceEndpointUtils.getCity(), 1, PAGE_SIZE_ALL);
                    success(response, null);
                }
                catch (RetrofitError retrofitError) {
                    failure(retrofitError);
                }
                break;
            case ASYNCHRONOUS:
                services.getPhotoFramesExperiences(
                        ServiceEndpointUtils.getCity(), 1, PAGE_SIZE_ALL, this);
                break;
            default:
                throw new IllegalArgumentException("Invalid concurrencyType set on NetworkRequest");
        }
    }

    @Override
    public void success(GetPhotoFramesExperienceResponse getPhotoFramesExperienceResponse, Response response) {

        if (null != getPhotoFramesExperienceResponse) {
           // syncPhotoFrameExperiencesWithDatabase(getPhotoFramesExperienceResponse.getPhotoFrameExperiences());
            SyncPhotoFrameWithDatabase syncPhotoFrameWithDatabase = new SyncPhotoFrameWithDatabase(getPhotoFramesExperienceResponse);
            syncPhotoFrameWithDatabase.successCase(getPhotoFramesExperienceResponse.getPhotoFrameExperiences());
        }
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, retrofitError.getMessage());
        }
    }
//
//    private void syncPhotoFrameExperiencesWithDatabase(List<PhotoFrameExperience> photoFrameExperiences) {
//        if (null == photoFrameExperiences || photoFrameExperiences.isEmpty()) {
//            return;
//        }
//
//        ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
//
//        if (null != contentResolver) {
//            Cursor cursor = contentResolver.query(UniversalOrlandoContentUris.PHOTOFRAME_EXPERIENCE, null, null, null, null);
//
//            for (PhotoFrameExperience experience : photoFrameExperiences) {
//                boolean found = false;
//                if (null != cursor && cursor.moveToFirst()) {
//                    do {
//                        long experienceId = cursor.getLong(cursor.getColumnIndexOrThrow(UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.COL_EXPERIENCE_ID));
//
//                        if (experience.getId() == experienceId) {
//                            found = true;
//                            break;
//                        }
//
//                    } while (cursor.moveToNext());
//
//                }
//                ContentValues experienceContentValues = photoFrameExperienceToContentValues(experience);
//
//                if (found) {
//                    //update
//                    String selection = UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.COL_EXPERIENCE_ID + " = ?";
//                    contentResolver.update(UniversalOrlandoContentUris.PHOTOFRAME_EXPERIENCE, experienceContentValues, selection, new String[]{String.valueOf(experience.getId())});
//                } else {
//                    //insert
//                    contentResolver.insert(UniversalOrlandoContentUris.PHOTOFRAME_EXPERIENCE, experienceContentValues);
//                }
//            }
//        }
//    }
//
//    private ContentValues photoFrameExperienceToContentValues(PhotoFrameExperience experience) {
//        if (null == experience) {
//            return null;
//        }
//        ContentValues values = new ContentValues();
//        values.put(UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.COL_EXPERIENCE_ID, experience.getId());
//        values.put(UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.COL_PHOTOFRAME_EXPERIENCE_JSON, experience.toJson());
//        return values;
//    }
}
