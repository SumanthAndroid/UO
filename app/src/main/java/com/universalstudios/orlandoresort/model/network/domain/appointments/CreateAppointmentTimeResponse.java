package com.universalstudios.orlandoresort.model.network.domain.appointments;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.NetworkResponse;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by GOKHAN on 7/26/2016.
 */
@Parcel
public class CreateAppointmentTimeResponse extends NetworkResponse {

    @SerializedName("AppointmentTimeId")
    Long appointmentTimeId;

    @SerializedName("Quantity")
    Integer quantity;

    @SerializedName("Barcode")
    String barcode;

    @SerializedName("Status")
    String status;

    @SerializedName("DistributionPoint")
    String distributionPoint;

    @SerializedName("Url")
    String url;

    @SerializedName("Id")
    Long ticketAppointmentId;

    @SerializedName("ExternalIds")
    List<AppointmentTimeExternalIds> externalIds;

    @SerializedName("startTime")
    String startime;

    @SerializedName("endTime")
    String endTime;

    @SerializedName("QueueId")
    Long queueId;

    @SerializedName("PoiId")
    Long poiId;

    @SerializedName("QueueImage")
    String queueImage;

    // FIELD NOT RETURNED FROM SERVICE
    @SerializedName("TicketDisplayName")
    String ticketDisplayName;

    @SerializedName("Imageurl")
    String imageUrl;

    @SerializedName("HasBeenRead")
    boolean hasBeeenRead;

    @SerializedName("ParkName")
    String parkName;

    @SerializedName("AppointmentTime")
    private AppointmentTimes appointmentTime;

    boolean isTicketAppointmentStored = false;

    public long getAppointmentTimeId() {
        return appointmentTimeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getStatus() {
        return status;
    }

    public String getDistributionPoint() {
        return distributionPoint;
    }

    public String getUrl() {
        return url;
    }

    public Long getTicketAppointmentId() {
        return ticketAppointmentId;
    }

    public String getStartime() {
        return startime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isHasBeeenRead() {
        return hasBeeenRead;
    }

    public void setHasBeeenRead(boolean hasBeeenRead) {
        this.hasBeeenRead = hasBeeenRead;
    }

    public boolean isTicketAppointmentStored() {
        return isTicketAppointmentStored;
    }

    public List<AppointmentTimeExternalIds> getExternalIds() {
        return externalIds;
    }

    public String getTicketDisplayName() {
        return ticketDisplayName;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public void setTicketDisplayName(String ticketDisplayName) {
        this.ticketDisplayName = ticketDisplayName;
    }

    public AppointmentTimes getAppointmentTime() {
        return appointmentTime;
    }


    public Long getQueueId() {
        return queueId;
    }

    public Long getPoiId() {
        return poiId;
    }

    public String getQueueImage() {
        return queueImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateAppointmentTimeResponse that = (CreateAppointmentTimeResponse) o;

        if (appointmentTimeId != null ? !appointmentTimeId.equals(that.appointmentTimeId) : that.appointmentTimeId != null) {
            return false;
        }

        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null) {
            return false;
        }

        if (barcode != null ? !barcode.equals(that.barcode) : that.barcode != null) {
            return false;
        }

        if (status != null ? !status.equals(that.status) : that.status != null) {
            return false;
        }

        if (startime != null ? !startime.equals(that.startime) : that.startime != null) {
            return false;
        }

        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) {
            return false;
        }

        if (queueId != null ? !queueId.equals(that.queueId) : that.queueId != null) {
            return false;
        }

        if (queueImage != null ? !queueImage.equals(that.queueImage) : that.queueImage != null) {
            return false;
        }

        return ticketDisplayName != null ? ticketDisplayName.equals(that.ticketDisplayName) : that.ticketDisplayName == null;

    }

}
