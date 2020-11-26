package com.bbva.hackathon.bbvakids.item;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.*;



@Schema(description = "The profile user")
@Entity
@IdClass(ItemPK.class)
public class Item extends PanacheEntityBase {
    @Id
    public long id;
    @Id
    public long profileId;

    public String type;
    public String name;
    public int requiredLevel;
    public boolean used;
}
