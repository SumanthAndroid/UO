package com.universalstudios.orlandoresort.model.network.domain.photoframes;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * Project Name: Universal Orlando
 * Created by kevinhaines on 6/19/16.
 * Class: GetPhotoFramesExperienceResponse
 * Class Description: TODO: ALWAYS FILL OUT
 */
@Parcel
public class GetPhotoFramesExperienceResponse extends NetworkResponse {

    @SerializedName("Results")
    List<PhotoFrameExperience> photoFrameExperiences;

    public List<PhotoFrameExperience> getPhotoFrameExperiences() {
        return photoFrameExperiences;
    }
}
