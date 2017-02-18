package com.universalstudios.orlandoresort.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import com.crittercism.app.Crittercism;
import com.universalstudios.orlandoresort.BuildConfig;
import com.universalstudios.orlandoresort.controller.application.UniversalOrlandoApplication;
import com.universalstudios.orlandoresort.model.network.domain.photoframes.PhotoFrameExperience;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoContentUris;
import com.universalstudios.orlandoresort.model.persistence.UniversalOrlandoDatabaseTables;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 6/21/16.
 * Class: PhotoFrameExperienceManager
 * Class Description: Manager for convenience of handling photoframe experiences
 */
public class PhotoFrameExperienceManager {
    public static final String TAG = "PhotoFrameExperienceManager";

    /**
     * Gets a list of {@link PhotoFrameExperience} from the database using a list of experience ids
     *
     * @param experienceIds ID's of the experiences to find
     * @return A list of {@link PhotoFrameExperience} from the database matching the ID's passed in
     */
    public static List<PhotoFrameExperience> getExperiencesFromIds(List<Long> experienceIds) {
        List<PhotoFrameExperience> experiencesFromDatabase = new ArrayList<>();
        ContentResolver contentResolver = UniversalOrlandoApplication.getAppContext().getContentResolver();
        if (null != contentResolver) {
            String[] args = new String[experienceIds.size()];
            List<String> argsList = new ArrayList<>(experienceIds.size());
            for (Long id : experienceIds) {
                argsList.add(String.valueOf(id));
            }
            argsList.toArray(args);

            String selection = UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.COL_EXPERIENCE_ID + " = ?";
            Cursor cursor = contentResolver.query(UniversalOrlandoContentUris.PHOTOFRAME_EXPERIENCE, null, selection, args, null);

            try {
                if (null != cursor && cursor.moveToNext()) {
                    do {
                        String experienceJson = cursor.getString(cursor.getColumnIndexOrThrow(UniversalOrlandoDatabaseTables.PhotoFrameExperienceTable.COL_PHOTOFRAME_EXPERIENCE_JSON));
                        if (!TextUtils.isEmpty(experienceJson)) {
                            PhotoFrameExperience experience = PhotoFrameExperience.fromJson(experienceJson, PhotoFrameExperience.class);
                            if (null != experience) {
                                experiencesFromDatabase.add(experience);
                            }
                        }
                    } while (cursor.moveToNext());
                }
            } catch (SQLiteException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }

                Crittercism.logHandledException(e);
            } finally {
                if (null != cursor) {
                    cursor.close();
                }
            }
        }
        return experiencesFromDatabase;
    }
}
