/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.codes.yaax.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultWebRequestor;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.WebRequestor;
import com.restfb.exception.FacebookException;
import com.restfb.types.Account;
import com.restfb.types.ads.AdAccount;
import com.restfb.types.ads.Lead;
import java.io.IOException;

/**
 *
 * @author s6608360
 */
public class FacebookApi {

    private static JsonObject validationTokenConection(String token) {
        JsonObject result;
        JsonParser parser = new JsonParser();
        try {
            WebRequestor wr = new DefaultWebRequestor();
            WebRequestor.Response accessTokenResponse = wr.executeGet(
                    "https://graph.facebook.com/me?access_token=" + token);
            result = parser.parse(accessTokenResponse.getBody()).getAsJsonObject();
        } catch (IOException ex) {
            System.out.println("error al tratar de conectarse con facebook: " + ex);
            result = parser.parse("{error :" + ex + "}").getAsJsonObject();
        }
        return result;
    }

    public static String checkingToken(String token) {
        try {
            JsonObject answer = validationTokenConection(token);
            if (answer.has("error")) {
                return "No";
            }
            return "Si";
        } catch (FacebookException e) {
            System.out.println("Excepción de Facebook: " + e);
            return "No";
        }
    }

    public static JsonObject account(String token, String name) {
        JsonObject json = new JsonObject();
        try {
            JsonObject answer = validationTokenConection(token);
            if (!answer.has("error")) {
                FacebookClient fbClient = new DefaultFacebookClient(token, Version.LATEST);
                Connection<Account> accounts = fbClient.fetchConnection("/me/accounts", Account.class);

                for (int i = 0; i < accounts.getData().size(); i++) {
                    Account currentAc = accounts.getData().get(i);
                    if (currentAc.getName().equals(name)) {
                        json.addProperty("nombre", currentAc.getName());
                        json.addProperty("token", currentAc.getAccessToken());
                        json.addProperty("id", currentAc.getId());
                        return json;
                    }
                }
                json.add("error", new JsonPrimitive("La cuenta con el nombre " + name + ", no se encontró, favor de verificar"));
            } else {
                json.add("error", answer.get("error"));
            }

        } catch (FacebookException ex) {
            System.out.println("error al obtener la cuenta: " + ex);
            json.add("error", new JsonPrimitive(ex.toString()));
        }
        return json;
    }

    public static JsonObject accounts(String token) {
        JsonObject json = new JsonObject();
        JsonArray jsonElements = new JsonArray();
        try {
            JsonObject answer = validationTokenConection(token);
            if (!answer.has("error")) {
                FacebookClient fbClient = new DefaultFacebookClient(token, Version.LATEST);
                Connection<Account> accounts = fbClient.fetchConnection("/me/accounts", Account.class);
                for (int i = 0; i < accounts.getData().size(); i++) {
                    Account currentAc = accounts.getData().get(i);
                    JsonObject elementJson = new JsonObject();
                    elementJson.addProperty("nombre", currentAc.getName());
                    elementJson.addProperty("token", currentAc.getAccessToken());
                    elementJson.addProperty("id", currentAc.getId());
                    jsonElements.add(elementJson);
                }
                json.add("cuentas", jsonElements);
                return json;
            } else {
                json.add("error", answer.get("error"));
            }
        } catch (FacebookException ex) {
            System.out.println("error al obtener la cuenta: " + ex + ex.getLocalizedMessage() + ex.getMessage());
            json.add("error", new JsonPrimitive(ex.toString()));
        }
        return json;
    }

    public static JsonObject campains(String accountToken, String pageId) {
        JsonObject json = new JsonObject();
        JsonArray jsonElements = new JsonArray();
        try {
            JsonObject answer = validationTokenConection(accountToken);
            if (!answer.has("error")) {
                FacebookClient fbClient = new DefaultFacebookClient(accountToken, Version.LATEST);
                System.out.println(pageId + "/leadgen_forms/");

                Connection<AdAccount> campains = fbClient.fetchConnection(pageId + "/leadgen_forms/", AdAccount.class, Parameter.with("filtering", "[{'field':'status','operator':'EQUAL','value':'ACTIVE'}]"));

                for (int i = 0; i < campains.getData().size(); i++) {
                    AdAccount camp = campains.getData().get(i);
                    JsonObject elementJson = new JsonObject();
                    elementJson.addProperty("nombre", camp.getName());
                    elementJson.addProperty("id", camp.getId());
                    jsonElements.add(elementJson);
                }
                json.add("formularios", jsonElements);
                return json;
            } else {
                json.add("error", answer.get("error"));
            }
        } catch (FacebookException ex) {
            System.out.println("error al obtener los formularios: " + ex);
            json.addProperty("error", ex.toString());
        }
        return json;
    }

    public static JsonObject userDataCamp(String tokenForm, String formId) {
        
        JsonObject json = new JsonObject();
        JsonArray jsonElements = new JsonArray();
        try {
            JsonObject answer = validationTokenConection(tokenForm);
            if (!answer.has("error")) {
                System.out.println(formId + "/leads/");
                FacebookClient fbClient = new DefaultFacebookClient(tokenForm, Version.LATEST);
                Connection<Lead> leads = fbClient.fetchConnection(formId + "/leads/", Lead.class);
                for (int i = 0; i < leads.getData().size(); i++) {
                    Lead cntLead = leads.getData().get(i);
                    JsonObject elementJson = new JsonObject();
                    for(int j = 0; j < cntLead.getFieldData().size(); j++){
                        elementJson.addProperty("tiempo_creacion", cntLead.getCreatedTime().toString());
                        elementJson.addProperty(cntLead.getFieldData().get(j).getName(),cntLead.getFieldData().get(j).getValues().get(0));
                        System.out.println(elementJson);
                    }
                    jsonElements.add(elementJson);
                }
                json.add("datos", jsonElements);
                return json;
            }else{
                json.add("error", answer.get("error"));
            }
        } catch (FacebookException ex) {
            System.out.println("error al obtener los datos del usuario: " + ex);
            json.addProperty("error", ex.toString());
            
        }
        return json;
    }

}
