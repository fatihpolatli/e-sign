package com.esign.signer.controller;

import java.util.List;

/**
 * GenericResponse
 */
public class GenericResponse {

    Integer Code;

    String Description;

    Object Response;

    Object Terminals;

    public GenericResponse(Integer code, String description, Object response, Object terminals) {
        Code = code;
        Description = description;
        Response = response;
        Terminals = terminals;
    }

    
    public static GenericResponse createSuccessResponse(Object data){

        return new GenericResponse(200, "description", null, data);
    }

    public Integer getCode() {
        return Code;
    }

    public void setCode(Integer code) {
        Code = code;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Object getResponse() {
        return Response;
    }

    public void setResponse(Object response) {
        Response = response;
    }

    public Object getTerminals() {
        return Terminals;
    }

    public void setTerminals(Object terminals) {
        Terminals = terminals;
    }

   

 

   
}