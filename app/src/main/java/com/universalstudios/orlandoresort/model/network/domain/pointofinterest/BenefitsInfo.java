/**
 * 
 */
package com.universalstudios.orlandoresort.model.network.domain.pointofinterest;

import com.google.gson.annotations.SerializedName;
import com.universalstudios.orlandoresort.model.network.response.GsonObject;

import org.parceler.Parcel;

/**
 * @author acampbell
 *
 */
@Parcel
public class BenefitsInfo extends GsonObject {

    @SerializedName("Title")
    String title;
    
    @SerializedName("Text")
    String text;
    
    @SerializedName("BenefitsCategory")
    String benefitsCategory;

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * @return the text
     */
    public String getText() {
        return text;
    }
    
    /**
     * @return the benefitsCategory
     */
    public String getBenefitsCategory() {
        return benefitsCategory;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((benefitsCategory == null) ? 0 : benefitsCategory.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BenefitsInfo other = (BenefitsInfo) obj;
        if (benefitsCategory == null) {
            if (other.benefitsCategory != null)
                return false;
        }
        else if (!benefitsCategory.equals(other.benefitsCategory))
            return false;
        if (text == null) {
            if (other.text != null)
                return false;
        }
        else if (!text.equals(other.text))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        }
        else if (!title.equals(other.title))
            return false;
        return true;
    }
    
}
