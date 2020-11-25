package com.bbva.hackathon.bbvakids.profile;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

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
public class ProfileResource {

//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    public String hello() {
//        return "hello";
//    }

    private static final Logger LOGGER = Logger.getLogger(ProfileResource.class);

    @Inject
    ProfileService service;

    // tag::adocOpenAPI[]
    @Operation(summary = "Returns all the profiles from the database")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Profile.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No profiles")
    // end::adocOpenAPI[]
    // tag::adocMetrics[]
    @Counted(name = "countGetAllProfiles", description = "Counts how many times the getAllProfiles method has been invoked")
    @Timed(name = "timeGetAllProfiles", description = "Times how long it takes to invoke the getAllProfiles method", unit = MetricUnits.MILLISECONDS)
    // end::adocMetrics[]
    @GET
    public Response getAllProfiles() {
        List<Profile> profiles = service.findAllProfiles();
//        LOGGER.debug("Total number of profiles " + profiles.size());
        return Response.ok(profiles).build();
    }

    // tag::adocOpenAPI[]
    @Operation(summary = "Returns a Profile for a given identifier")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Profile.class)))
    @APIResponse(responseCode = "204", description = "The Profile is not found for a given identifier")
    // end::adocOpenAPI[]
    // tag::adocMetrics[]
    @Counted(name = "countGetProfile", description = "Counts how many times the getProfile method has been invoked")
    @Timed(name = "timeGetProfile", description = "Times how long it takes to invoke the getProfile method", unit = MetricUnits.MILLISECONDS)
    // end::adocMetrics[]
    @GET
    @Path("/{id}")
    public Response getProfile(
            // tag::adocOpenAPI[]
            @Parameter(description = "Profile identifier", required = true)
            // end::adocOpenAPI[]
            @PathParam("id") Long id) {
        Profile profile = service.findProfileById(id);
        if (profile != null) {
            LOGGER.debug("Found Profile " + profile);
            return Response.ok(profile).build();
        } else {
            LOGGER.debug("No Profile found with id " + id);
            return Response.noContent().build();
        }
    }

    // tag::adocOpenAPI[]
    @Operation(summary = "Creates a valid Profile")
    @APIResponse(responseCode = "201", description = "The URI of the created Profile", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
    // end::adocOpenAPI[]
    // tag::adocMetrics[]
    @Counted(name = "countCreateProfile", description = "Counts how many times the createProfile method has been invoked")
    @Timed(name = "timeCreateProfile", description = "Times how long it takes to invoke the createProfile method", unit = MetricUnits.MILLISECONDS)
    // end::adocMetrics[]
    @POST
    public Response createProfile(
            // tag::adocOpenAPI[]
            @RequestBody(required = true, content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Profile.class)))
            // end::adocOpenAPI[]
            @Valid Profile profile, @Context UriInfo uriInfo) {
        profile = service.persistProfile(profile);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(profile.id));
        LOGGER.debug("New Profile created with URI " + builder.build().toString());
        return Response.created(builder.build()).build();
    }

    // tag::adocOpenAPI[]
    @Operation(summary = "Updates an exiting  Profile")
    @APIResponse(responseCode = "200", description = "The updated Profile", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Profile.class)))
    // end::adocOpenAPI[]
    // tag::adocMetrics[]
    @Counted(name = "countUpdateProfile", description = "Counts how many times the updateProfile method has been invoked")
    @Timed(name = "timeUpdateProfile", description = "Times how long it takes to invoke the updateProfile method", unit = MetricUnits.MILLISECONDS)
    // end::adocMetrics[]
    @PUT
    public Response updateProfile(
            // tag::adocOpenAPI[]
            @RequestBody(required = true, content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Profile.class)))
            // end::adocOpenAPI[]
            @Valid Profile profile) {
        profile = service.updateProfile(profile);
        LOGGER.debug("Profile updated with new valued " + profile);
        return Response.ok(profile).build();
    }

    // tag::adocOpenAPI[]
    @Operation(summary = "Deletes an exiting Profile")
    @APIResponse(responseCode = "204")
    // end::adocOpenAPI[]
    // tag::adocMetrics[]
    @Counted(name = "countDeleteProfile", description = "Counts how many times the deleteProfile method has been invoked")
    @Timed(name = "timeDeleteProfile", description = "Times how long it takes to invoke the deleteProfile method", unit = MetricUnits.MILLISECONDS)
    // end::adocMetrics[]
    @DELETE
    @Path("/{id}")
    public Response deleteProfile(
            // tag::adocOpenAPI[]
            @Parameter(description = "Profile identifier", required = true)
            // end::adocOpenAPI[]
            @PathParam("id") Long id) {
        service.deleteProfile(id);
        LOGGER.debug("Profile deleted with " + id);
        return Response.noContent().build();
    }
}