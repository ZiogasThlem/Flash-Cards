package com.tilem.flashcards.data.entity;

import org.springframework.data.annotation.Transient;

import java.util.UUID;

@SuppressWarnings("all")
public abstract class DbEntity implements Comparable {

    @Transient
    protected String tempUuid;

    /*
     * TempUniqueID is used in circumstances where uniqueID is null (e.g. before
     * an object is persisted) or when we wish to add the same object twice in a
     * map or a Vaadin container.
     */
    @Transient
    protected long tempUniqueID;

    public String getTempUuid() {
        if (tempUuid == null) {
            tempUuid = UUID.randomUUID().toString();
        }
        return tempUuid;
    }

    public void setTempUuid(String tempUuid) {
        this.tempUuid = tempUuid;
    }

    public long getTempUniqueID() {
        return tempUniqueID;
    }

    public void setTempUniqueID(long tempUniqueID) {
        this.tempUniqueID = tempUniqueID;
    }

    public String getSimpleLabel() {
        return String.valueOf( getUniqueID() );
    }

    /*
     * A more detailed version of getSimpleLabel method
     */
    public String getDetailedLabel() {
        return getUniqueID() + " - " + getSimpleLabel();
    }

    public abstract String getEntityTitle();

    public abstract Long getUniqueID();

    @Override
    public String toString() {
        return getSimpleLabel();
    }

    @Override
    public int hashCode() {
        if (getUniqueID() != null)
            return getUniqueID().hashCode();
        else if (getTempUuid() != null)
            return getTempUuid().hashCode();
        else
            return 0;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        DbEntity other = (DbEntity) obj;
        if (getUniqueID() != null) {
            if (other.getUniqueID() == null)
                return false;
            else
                return getUniqueID().equals(other.getUniqueID());
        } else {
            return getTempUuid().equals(other.getTempUuid());
        }
    }

    @Override
    public int compareTo(Object o) {
        if(this.toString() != null) {
            return this.toString().compareToIgnoreCase(o == null ? "" : o.toString()) ;
        }

        return 1;
    }

}
