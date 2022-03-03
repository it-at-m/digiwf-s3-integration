package io.muenchendigital.digiwf.s3.integration.gen.api;

import io.muenchendigital.digiwf.s3.integration.gen.ApiClient;

import java.util.Date;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-03-03T15:53:28.604617800+01:00[Europe/Berlin]")@Component("io.muenchendigital.digiwf.s3.integration.gen.api.FolderApiApi")
public class FolderApiApi {
    private ApiClient apiClient;

    public FolderApiApi() {
        this(new ApiClient());
    }

    @Autowired
    public FolderApiApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * 
     * Deletes the folder specified in the parameter together with the corresponding database entry
     * <p><b>200</b> - OK
     * @param refId  (required)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void delete(String refId) throws RestClientException {
        deleteWithHttpInfo(refId);
    }

    /**
     * 
     * Deletes the folder specified in the parameter together with the corresponding database entry
     * <p><b>200</b> - OK
     * @param refId  (required)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> deleteWithHttpInfo(String refId) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'refId' is set
        if (refId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'refId' when calling delete");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("refId", refId);
        String path = UriComponentsBuilder.fromPath("/folder/{refId}").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = {  };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI(path, HttpMethod.DELETE, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Updates the end of life attribute in the corresponding database entry for the folder specified in the parameter
     * <p><b>200</b> - OK
     * @param refId  (required)
     * @param endOfLife  (required)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void updateEndOfLife(String refId, java.time.LocalDate endOfLife) throws RestClientException {
        updateEndOfLifeWithHttpInfo(refId, endOfLife);
    }

    /**
     * 
     * Updates the end of life attribute in the corresponding database entry for the folder specified in the parameter
     * <p><b>200</b> - OK
     * @param refId  (required)
     * @param endOfLife  (required)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> updateEndOfLifeWithHttpInfo(String refId, java.time.LocalDate endOfLife) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'refId' is set
        if (refId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'refId' when calling updateEndOfLife");
        }
        // verify the required parameter 'endOfLife' is set
        if (endOfLife == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'endOfLife' when calling updateEndOfLife");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("refId", refId);
        String path = UriComponentsBuilder.fromPath("/folder/{refId}").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "endOfLife", endOfLife));

        final String[] accepts = {  };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        return apiClient.invokeAPI(path, HttpMethod.PUT, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
