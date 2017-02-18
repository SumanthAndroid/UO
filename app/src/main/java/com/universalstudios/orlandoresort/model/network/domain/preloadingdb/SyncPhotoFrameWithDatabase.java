package com.universalstudios.orlandoresort.model.network.domain.preloadingdb;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.model.network.domain.photoframes.GetPhotoFramesExperienceResponse;
import com.universalstudios.orlandoresort.model.network.domain.photoframes.PhotoFrameExperience;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;

import java.util.List;

/**
 * Sync PhotoFrame data with database for pre loading
 * Created by GOKHAN on 6/27/2016.
 */
public class SyncPhotoFrameWithDatabase {

    GetPhotoFramesExperienceResponse getPhotoFramesExperienceResponse;

    public SyncPhotoFrameWithDatabase(GetPhotoFramesExperienceResponse getPhotoFramesExperienceResponse){
        this.getPhotoFramesExperienceResponse = getPhotoFramesExperienceResponse;
    }

    public void successCase(List<PhotoFrameExperience> photoFrameExperiences){
        syncPhotoFrameExperiencesWithDatabase(photoFrameExperiences);
    }

    @SuppressLint("Recycle")
    private void syncPhotoFrameExperiencesWithDatabase(List<PhotoFrameExperience> photoFrameExperiences) {
        if (null == photoFrameExperiences || photoFrameExperiences.isEmpty()) {
            return;
        }
        ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();

        if (null != contentResolver) {
             Cursor cursor = contentResolver.query(UniversalOrlandoContentUris.PHOTOFRAME_EXPERIENCE, null, null, null, null);

            for (PhotoFrameExperience experience : photoFrameExperiences) {
                boolean found = false;
                if (null != cursor && cursor.moveToFirst()) {
                    do {
                        long experienceId = cursor.getLong(cursor.getColumnIndexOrThrow(UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.COL_EXPERIENCE_ID));

                        if (experience.getId() == experienceId) {
                            found = true;
                            break;
                        }

                    } while (cursor.moveToNext());

                }
                ContentValues experienceContentValues = photoFrameExperienceToContentValues(experience);

                if (found) {
                    //update
                    String selection = UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.COL_EXPERIENCE_ID + " = ?";
                    contentResolver.update(UniversalOrlandoContentUris.PHOTOFRAME_EXPERIENCE, experienceContentValues, selection, new String[]{String.valueOf(experience.getId())});
                } else {
                    //insert
                    contentResolver.insert(UniversalOrlandoContentUris.PHOTOFRAME_EXPERIENCE, experienceContentValues);
                }
            }
        }
    }

    private ContentValues photoFrameExperienceToContentValues(PhotoFrameExperience experience) {
        if (null == experience) {
            return null;
        }
        ContentValues values = new ContentValues();
        values.put(UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.COL_EXPERIENCE_ID, experience.getId());
        values.put(UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.COL_PHOTOFRAME_EXPERIENCE_JSON, experience.toJson());
        return values;
    }
}
