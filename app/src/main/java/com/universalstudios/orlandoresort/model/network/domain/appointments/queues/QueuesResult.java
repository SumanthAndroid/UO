package com.universalstudios.orlandoresort.model.network.domain.appointments.queues;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * Created by GOKHAN on 7/12/2016.
 */
@Parcel
public class QueuesResult extends GsonObject {
    
    @SerializedName("Id")
    int id;

    @SerializedName("Name")
    String name;

    @SerializedName("MaxAppointmentSize")
    int maxAppointmentSize;

    @SerializedName("AppointmentDuration")
    String appointmentDuration;

    @SerializedName("SessionTimeoutInSec")
    int sessionTimeoutInSec;

    @SerializedName("QueueEntityId")
    long queueEntityId;

    @SerializedName("QueueEntityType")
    String queueEntityType;

    @SerializedName("HasBeenRead")
    Boolean hasBeenRead;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxAppointmentSize() {
        return maxAppointmentSize;
    }

    public void setMaxAppointmentSize(int maxAppointmentSize) {
        this.maxAppointmentSize = maxAppointmentSize;
    }

    public String getAppointmentDuration() {
        return appointmentDuration;
    }

    public void setAppointmentDuration(String appointmentDuration) {
        this.appointmentDuration = appointmentDuration;
    }

    public int getSessionTimeoutInSec() {
        return sessionTimeoutInSec;
    }

    public void setSessionTimeoutInSec(int sessionTimeoutInSec) {
        this.sessionTimeoutInSec = sessionTimeoutInSec;
    }

    public long getQueueEntityId() {
        return queueEntityId;
    }

    public void setQueueEntityId(long queueEntityId) {
        this.queueEntityId = queueEntityId;
    }

    public String getQueueEntityType() {
        return queueEntityType;
    }

    public void setQueueEntityType(String queueEntityType) {
        this.queueEntityType = queueEntityType;
    }

    /**
     * @param hasBeenRead the hasBeenRead to set
     */
    public void setHasBeenRead(Boolean hasBeenRead) {
        this.hasBeenRead = hasBeenRead;
    }

    /**
     * @return the hasBeenRead
     */
    public Boolean getHasBeenRead() {
        return hasBeenRead;
    }


    public boolean equals(Object obj, boolean ignoreTransientFields) {
        if (this == obj) return true;
        if (!(obj instanceof QueuesResult)) return false;

        QueuesResult that = (QueuesResult) obj;

        if (getId() != that.getId()) return false;
        if (getMaxAppointmentSize() != that.getMaxAppointmentSize()) return false;
        if (getSessionTimeoutInSec() != that.getSessionTimeoutInSec()) return false;
        if (getQueueEntityId() != that.getQueueEntityId()) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null)
            return false;
        if (getAppointmentDuration() != null ? !getAppointmentDuration().equals(that.getAppointmentDuration()) : that.getAppointmentDuration() != null)
            return false;

        // Ignore has been read in some comparisons
        if (!ignoreTransientFields) {
            if (hasBeenRead == null) {
                if (that.hasBeenRead != null) {
                    return false;
                }
            } else if (!hasBeenRead.equals(that.hasBeenRead)) {
                return false;
            }
        }

        return getQueueEntityType() != null ? getQueueEntityType().equals(that.getQueueEntityType()) : that.getQueueEntityType() == null;

    }

}
