/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.codes.yaax.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author s6608360
 */
@Path("method")
public class FacebookMethod {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of FacebookMethod
     */
    public FacebookMethod() {
    }

    /**
     * Retrieves representation of an instance of
     * mx.com.codes.yaax.rest.FacebookMethod
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        return "";
    }

    @GET
    @Path("validacion/{token}")
    public String validacion(@PathParam("token") String token) {
        return "{Valid : " + FacebookApi.checkingToken(token) + "}";
    }

    @GET
    @Path("cuentas/{token}")
    public String cuentas(@PathParam("token") String token) {
        return FacebookApi.accounts(token).toString();
    }

    @GET
    @Path("cuenta/{token}/{nombre}")
    public String cuenta(@PathParam("token") String token, @PathParam("nombre") String nombre) {
        return FacebookApi.account(token, nombre).toString();
    }
    
    @GET
    @Path("formularios/{token}/{id}")
    public String campains(@PathParam("token") String token, @PathParam("id") String id) {
        return FacebookApi.campains(token, id).toString();
    }
    @GET
    @Path("formData/{token}/{id}")
    public String formData(@PathParam("token") String token, @PathParam("id") String id) {
        return FacebookApi.userDataCamp(token, id).toString();
    }
    
    /**
     * PUT method for updating or creating an instance of FacebookMethod
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
