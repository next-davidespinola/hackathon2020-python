package com.bbva.hackathon.bbvakids.profile;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

@ApplicationScoped
@Transactional(REQUIRED)
public class ProfileService {
    @Transactional(SUPPORTS)
    public List<Profile> findAllProfiles() {
        return Profile.listAll();
    }

    @Transactional(SUPPORTS)
    public Profile findProfileById(Long id) {
        return Profile.findById(id);
    }

    public Profile persistProfile(@Valid Profile profile) {
        Profile.persist(profile);
        return profile;
    }

    public Profile updateProfile(@Valid Profile profile) {
        Profile entity = Profile.findById(profile.id);
        entity.name = profile.name;
        entity.lastName = profile.lastName;
        entity.nickName = profile.nickName;
        entity.email = profile.email;
        entity.level = profile.level;
        entity.currentExp = profile.currentExp;
        entity.nextLevelExp = profile.nextLevelExp;
        entity.money = profile.money;
        entity.gems = profile.gems;
        return entity;
    }

    public void deleteProfile(Long id) {
        Profile profile = Profile.findById(id);
        profile.delete();
    }
}
