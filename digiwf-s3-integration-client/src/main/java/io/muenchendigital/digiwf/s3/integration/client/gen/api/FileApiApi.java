package io.muenchendigital.digiwf.s3.integration.client.gen.api;

import io.muenchendigital.digiwf.s3.integration.client.gen.ApiClient;

import io.muenchendigital.digiwf.s3.integration.client.gen.model.FileDataDto;
import io.muenchendigital.digiwf.s3.integration.client.gen.model.PresignedUrlDto;

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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-03-03T08:30:45.193582300+01:00[Europe/Berlin]")@Component("io.muenchendigital.digiwf.s3.integration.client.gen.api.FileApiApi")
public class FileApiApi {
    private ApiClient apiClient;

    public FileApiApi() {
        this(new ApiClient());
    }

    @Autowired
    public FileApiApi(ApiClient apiClient) {
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
     * Creates a presigned URL to delete the file specified in the parameter from the S3 storage
     * <p><b>200</b> - OK
     * @param refId  (required)
     * @param fileName  (required)
     * @param expiresInMinutes  (required)
     * @return PresignedUrlDto
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public PresignedUrlDto delete1(String refId, String fileName, Integer expiresInMinutes) throws RestClientException {
        return delete1WithHttpInfo(refId, fileName, expiresInMinutes).getBody();
    }

    /**
     * 
     * Creates a presigned URL to delete the file specified in the parameter from the S3 storage
     * <p><b>200</b> - OK
     * @param refId  (required)
     * @param fileName  (required)
     * @param expiresInMinutes  (required)
     * @return ResponseEntity&lt;PresignedUrlDto&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<PresignedUrlDto> delete1WithHttpInfo(String refId, String fileName, Integer expiresInMinutes) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'refId' is set
        if (refId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'refId' when calling delete1");
        }
        // verify the required parameter 'fileName' is set
        if (fileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'fileName' when calling delete1");
        }
        // verify the required parameter 'expiresInMinutes' is set
        if (expiresInMinutes == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'expiresInMinutes' when calling delete1");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("refId", refId);
        String path = UriComponentsBuilder.fromPath("/file/{refId}").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "fileName", fileName));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "expiresInMinutes", expiresInMinutes));

        final String[] accepts = { 
            "*/*"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<PresignedUrlDto> returnType = new ParameterizedTypeReference<PresignedUrlDto>() {};
        return apiClient.invokeAPI(path, HttpMethod.DELETE, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Creates a presigned URL to fetch the file specified in the parameter from the S3 storage
     * <p><b>200</b> - OK
     * @param refId  (required)
     * @param fileName  (required)
     * @param expiresInMinutes  (required)
     * @return PresignedUrlDto
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public PresignedUrlDto get(String refId, String fileName, Integer expiresInMinutes) throws RestClientException {
        return getWithHttpInfo(refId, fileName, expiresInMinutes).getBody();
    }

    /**
     * 
     * Creates a presigned URL to fetch the file specified in the parameter from the S3 storage
     * <p><b>200</b> - OK
     * @param refId  (required)
     * @param fileName  (required)
     * @param expiresInMinutes  (required)
     * @return ResponseEntity&lt;PresignedUrlDto&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<PresignedUrlDto> getWithHttpInfo(String refId, String fileName, Integer expiresInMinutes) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'refId' is set
        if (refId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'refId' when calling get");
        }
        // verify the required parameter 'fileName' is set
        if (fileName == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'fileName' when calling get");
        }
        // verify the required parameter 'expiresInMinutes' is set
        if (expiresInMinutes == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'expiresInMinutes' when calling get");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("refId", refId);
        String path = UriComponentsBuilder.fromPath("/file/{refId}").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "fileName", fileName));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "expiresInMinutes", expiresInMinutes));

        final String[] accepts = { 
            "*/*"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {  };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<PresignedUrlDto> returnType = new ParameterizedTypeReference<PresignedUrlDto>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Creates a presigned URL to store the file specified in the parameter within the S3 storage
     * <p><b>200</b> - OK
     * @param body  (required)
     * @return PresignedUrlDto
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public PresignedUrlDto save(FileDataDto body) throws RestClientException {
        return saveWithHttpInfo(body).getBody();
    }

    /**
     * 
     * Creates a presigned URL to store the file specified in the parameter within the S3 storage
     * <p><b>200</b> - OK
     * @param body  (required)
     * @return ResponseEntity&lt;PresignedUrlDto&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<PresignedUrlDto> saveWithHttpInfo(FileDataDto body) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling save");
        }
        String path = UriComponentsBuilder.fromPath("/file").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "*/*"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<PresignedUrlDto> returnType = new ParameterizedTypeReference<PresignedUrlDto>() {};
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * 
     * Creates a presigned URL to overwrite the file specified in the parameter within the S3 storage
     * <p><b>200</b> - OK
     * @param body  (required)
     * @return PresignedUrlDto
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public PresignedUrlDto update(FileDataDto body) throws RestClientException {
        return updateWithHttpInfo(body).getBody();
    }

    /**
     * 
     * Creates a presigned URL to overwrite the file specified in the parameter within the S3 storage
     * <p><b>200</b> - OK
     * @param body  (required)
     * @return ResponseEntity&lt;PresignedUrlDto&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<PresignedUrlDto> updateWithHttpInfo(FileDataDto body) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling update");
        }
        String path = UriComponentsBuilder.fromPath("/file").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "*/*"
         };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] {  };

        ParameterizedTypeReference<PresignedUrlDto> returnType = new ParameterizedTypeReference<PresignedUrlDto>() {};
        return apiClient.invokeAPI(path, HttpMethod.PUT, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
