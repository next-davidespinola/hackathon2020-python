package com.bbva.hackathon.bbvakids.item;

import java.io.Serializable;


public class ItemPK implements Serializable {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    protected long id;

    protected long profileId;
}
