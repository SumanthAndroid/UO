package com.universalstudios.orlandoresort.controller.userinterface.home;

/**
 * 
 * 
 * @author Jack Hughes
 */
public class FeaturedItem {
  
  private long mId;
  
  private Type mType;
  
  public FeaturedItem(long  id, Type type) {
    this.mId = id;
    this.mType = type;
  }

  public enum Type {
    POI(1),
    NEWS(2),
    EVENT_SERIES(3),
    OFFER(4);

    private int value;

    private Type( int value ) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  public long getId() {
    return mId;
  }

  public void setId( long mId ) {
    this.mId = mId;
  }

  public Type getType() {
    return mType;
  }

  public void setType( Type mType ) {
    this.mType = mType;
  }

}
