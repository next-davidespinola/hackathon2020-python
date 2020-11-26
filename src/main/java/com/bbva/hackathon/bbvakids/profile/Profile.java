package com.bbva.hackathon.bbvakids.profile;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Random;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;



@Schema(description = "The profile user")
@Entity
public class Profile extends PanacheEntity {

    @NotNull
    @Size(min = 2, max = 50)
    public String name;
    @Size(min = 4, max = 50)
    public String lastName;
    @Size(min = 4, max = 50)
    public String nickName;
    @Size(min = 4, max = 50)
    public String email;

    public int level;
    public int currentExp;
    public int nextLevelExp;
    public double money;


    public long gems;


    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", level=" + level +
                ", gems='" + gems + '\'' +
                '}';
    }
}
