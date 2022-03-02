# FolderApiApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**delete**](FolderApiApi.md#delete) | **DELETE** /folder/{refId} | 
[**updateEndOfLife**](FolderApiApi.md#updateEndOfLife) | **PUT** /folder/{refId} | 

<a name="delete"></a>
# **delete**
> delete(refId)



Deletes the folder specified in the parameter together with the corresponding database entry

### Example
```java
// Import classes:
//import io.muenchendigital.digiwf.s3.integration.client.gen.ApiException;
//import io.muenchendigital.digiwf.s3.integration.client.gen.api.FolderApiApi;


FolderApiApi apiInstance = new FolderApiApi();
String refId = "refId_example"; // String | 
try {
    apiInstance.delete(refId);
} catch (ApiException e) {
    System.err.println("Exception when calling FolderApiApi#delete");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **refId** | **String**|  |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="updateEndOfLife"></a>
# **updateEndOfLife**
> updateEndOfLife(refId, endOfLife)



Updates the end of life attribute in the corresponding database entry for the folder specified in the parameter

### Example
```java
// Import classes:
//import io.muenchendigital.digiwf.s3.integration.client.gen.ApiException;
//import io.muenchendigital.digiwf.s3.integration.client.gen.api.FolderApiApi;


FolderApiApi apiInstance = new FolderApiApi();
String refId = "refId_example"; // String | 
java.time.LocalDate endOfLife = new java.time.LocalDate(); // java.time.LocalDate | 
try {
    apiInstance.updateEndOfLife(refId, endOfLife);
} catch (ApiException e) {
    System.err.println("Exception when calling FolderApiApi#updateEndOfLife");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **refId** | **String**|  |
 **endOfLife** | **java.time.LocalDate**|  |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

