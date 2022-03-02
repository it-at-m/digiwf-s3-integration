# FileApiApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**delete1**](FileApiApi.md#delete1) | **DELETE** /file/{refId} | 
[**get**](FileApiApi.md#get) | **GET** /file/{refId} | 
[**save**](FileApiApi.md#save) | **POST** /file | 
[**update**](FileApiApi.md#update) | **PUT** /file | 

<a name="delete1"></a>
# **delete1**
> PresignedUrlDto delete1(refId, fileName, expiresInMinutes)



Creates a presigned URL to delete the file specified in the parameter from the S3 storage

### Example
```java
// Import classes:
//import io.muenchendigital.digiwf.s3.integration.client.gen.ApiException;
//import io.muenchendigital.digiwf.s3.integration.client.gen.api.FileApiApi;


FileApiApi apiInstance = new FileApiApi();
String refId = "refId_example"; // String | 
String fileName = "fileName_example"; // String | 
Integer expiresInMinutes = 56; // Integer | 
try {
    PresignedUrlDto result = apiInstance.delete1(refId, fileName, expiresInMinutes);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FileApiApi#delete1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **refId** | **String**|  |
 **fileName** | **String**|  |
 **expiresInMinutes** | **Integer**|  | [enum: ]

### Return type

[**PresignedUrlDto**](PresignedUrlDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="get"></a>
# **get**
> PresignedUrlDto get(refId, fileName, expiresInMinutes)



Creates a presigned URL to fetch the file specified in the parameter from the S3 storage

### Example
```java
// Import classes:
//import io.muenchendigital.digiwf.s3.integration.client.gen.ApiException;
//import io.muenchendigital.digiwf.s3.integration.client.gen.api.FileApiApi;


FileApiApi apiInstance = new FileApiApi();
String refId = "refId_example"; // String | 
String fileName = "fileName_example"; // String | 
Integer expiresInMinutes = 56; // Integer | 
try {
    PresignedUrlDto result = apiInstance.get(refId, fileName, expiresInMinutes);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FileApiApi#get");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **refId** | **String**|  |
 **fileName** | **String**|  |
 **expiresInMinutes** | **Integer**|  | [enum: ]

### Return type

[**PresignedUrlDto**](PresignedUrlDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="save"></a>
# **save**
> PresignedUrlDto save(body)



Creates a presigned URL to store the file specified in the parameter within the S3 storage

### Example
```java
// Import classes:
//import io.muenchendigital.digiwf.s3.integration.client.gen.ApiException;
//import io.muenchendigital.digiwf.s3.integration.client.gen.api.FileApiApi;


FileApiApi apiInstance = new FileApiApi();
FileDataDto body = new FileDataDto(); // FileDataDto | 
try {
    PresignedUrlDto result = apiInstance.save(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FileApiApi#save");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**FileDataDto**](FileDataDto.md)|  |

### Return type

[**PresignedUrlDto**](PresignedUrlDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="update"></a>
# **update**
> PresignedUrlDto update(body)



Creates a presigned URL to overwrite the file specified in the parameter within the S3 storage

### Example
```java
// Import classes:
//import io.muenchendigital.digiwf.s3.integration.client.gen.ApiException;
//import io.muenchendigital.digiwf.s3.integration.client.gen.api.FileApiApi;


FileApiApi apiInstance = new FileApiApi();
FileDataDto body = new FileDataDto(); // FileDataDto | 
try {
    PresignedUrlDto result = apiInstance.update(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FileApiApi#update");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**FileDataDto**](FileDataDto.md)|  |

### Return type

[**PresignedUrlDto**](PresignedUrlDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

