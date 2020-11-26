package com.bbva.hackathon.bbvakids.profile;


import com.bbva.hackathon.bbvakids.item.Item;
import com.bbva.hackathon.bbvakids.item.ItemService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/api/profiles")
@Produces(APPLICATION_JSON)
public class ProfileResource {

    private static final Logger LOGGER = Logger.getLogger(ProfileResource.class);

    @Inject
    ProfileService profileService;

    @Inject
    ItemService itemService;

    @Operation(summary = "Returns all the profiles from the database")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Profile.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No profiles")
    @GET
    public Response getAllProfiles() {
        List<Profile> profiles = profileService.findAllProfiles();
        return Response.ok(profiles).build();
    }

    @Operation(summary = "Returns a Profile for a given identifier")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Profile.class)))
    @APIResponse(responseCode = "204", description = "The Profile is not found for a given identifier")
    @GET
    @Path("/{id}")
    public Response getProfile(
            @Parameter(description = "Profile identifier", required = true)
            @PathParam("id") Long id) {
        Profile profile = profileService.findProfileById(id);
        if (profile != null) {
            LOGGER.debug("Found Profile " + profile);
            return Response.ok(profile).build();
        } else {
            LOGGER.debug("No Profile found with id " + id);
            return Response.noContent().build();
        }
    }

    @Operation(summary = "Returns a Profile for a given identifier")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Item.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "The Profile is not found for a given identifier")
    @GET
    @Path("/{id}/inventory")
    public Response getProfileInventory(
            @Parameter(description = "Profile identifier", required = true)
            @PathParam("id") Long id) {
        Profile profile = profileService.findProfileById(id);
        if (profile != null) {
            LOGGER.debug("Found Profile " + profile);
            List<Item> items = itemService.findAllItemsByProfileId(id);
            return Response.ok(items).build();
        } else {
            LOGGER.debug("No Profile found with id " + id);
            return Response.noContent().build();
        }
    }

    @PATCH
    @Path("/{id}/inventory/{itemId}")
    public Response updateProfileInventory(
            @Parameter(description = "Profile identifier", required = true)
            @PathParam("id") Long id, @PathParam("itemId") Long itemId,
            @RequestBody(required = true, content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Item.class))) Item item) {
        item.id = itemId;
        item.profileId = id;
        item = itemService.updateItem(item);

        return Response.noContent().build();
    }

    @Operation(summary = "Creates a valid Profile")
    @APIResponse(responseCode = "201", description = "The URI of the created Profile", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
    @POST
    public Response createProfile(
            @RequestBody(required = true, content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Profile.class)))
            @Valid Profile profile, @Context UriInfo uriInfo) {
        profile = profileService.persistProfile(profile);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(profile.id));
        LOGGER.debug("New Profile created with URI " + builder.build().toString());
        return Response.created(builder.build()).build();
    }

    @Operation(summary = "Updates an exiting  Profile")
    @APIResponse(responseCode = "200", description = "The updated Profile", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Profile.class)))
    @PUT
    public Response updateProfile(
            @RequestBody(required = true, content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Profile.class)))
            @Valid Profile profile) {
        profile = profileService.updateProfile(profile);
        LOGGER.debug("Profile updated with new valued " + profile);
        return Response.ok(profile).build();
    }

    @Operation(summary = "Deletes an exiting Profile")
    @APIResponse(responseCode = "204")
    @DELETE
    @Path("/{id}")
    public Response deleteProfile(
            @Parameter(description = "Profile identifier", required = true)
            @PathParam("id") Long id) {
        profileService.deleteProfile(id);
        LOGGER.debug("Profile deleted with " + id);
        return Response.noContent().build();
    }
}