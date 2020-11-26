package com.bbva.hackathon.bbvakids.item;


import com.bbva.hackathon.bbvakids.profile.Profile;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

@ApplicationScoped
@Transactional(REQUIRED)
public class ItemService {
    @Transactional(SUPPORTS)
    public List<Item> findAllItemsByProfileId(Long profileId) {
        return Item.find("profileId", profileId).list();
    }

    public Item updateItem(@Valid Item item) {
        Item entity = Item.find("profileId = :profileId and id = :id", Parameters.with("profileId", item.profileId).and("id", item.id)).singleResult();
        entity.used = item.used;
        return entity;
    }
}
