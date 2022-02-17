package io.muenchendigital.digiwf.s3.integration.infrastructure.repository;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import io.minio.messages.Item;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AccessException;
import org.apache.commons.collections4.IteratorUtils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class S3Repository {

    private final String bucketName;
    private final MinioClient client;

    public S3Repository(final String bucketName, final MinioClient client) throws S3AccessException {
        this.bucketName = bucketName;
        this.client = client;
        try {
            this.client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (final MinioException | InvalidKeyException | NoSuchAlgorithmException | IllegalArgumentException | IOException e) {
            throw new S3AccessException("S3 initialization failed.", e);
        }
    }

    /**
     * Gibt die Pfade zu den Dateien in einen gegebenen Ordner zurück.
     *
     * @param folder Der Ordner.
     *               Der Pfad ist absolut und ohne Angabe des Buckets anzugeben.
     *               Beispiel 1:
     *               Datei in Bucket: "BUCKET/folder"
     *               Angabe in Parameter: "folder"
     *               Beispiel 2:
     *               Datei in Bucket: "BUCKET/folder/subfolder"
     *               Angabe in Parameter: "folder/subfolder"
     * @return die Pfade zu den Dateien in einen gegebenen Ordner. Gibt auch die Pfade der Dateien in Unterordner zurück.
     * @throws S3AccessException falls die Datei nicht hochgeladen werden kann.
     */
    public Set<String> getFilepathesFromFolder(final String folder) throws S3AccessException {
        try {
            final ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
                    .bucket(this.bucketName)
                    .prefix(folder)
                    .recursive(true)
                    .build();
            final List<Result<Item>> resultItemList = IteratorUtils.toList(this.client.listObjects(listObjectsArgs).iterator());
            final Set<String> filepathesFromFolder = new HashSet<>();
            for (final Result<Item> resultItem : resultItemList) {
                filepathesFromFolder.add(resultItem.get().objectName());
            }
            return filepathesFromFolder;
        } catch (final MinioException | InvalidKeyException | NoSuchAlgorithmException | IllegalArgumentException | IOException exception) {
            throw new S3AccessException(String.format("Failed to extract file pathes from folder %s.", folder), exception);
        }
    }

    /**
     * Löscht die Datei in einen gegebenen Ordner.
     *
     * @param pathToFile Der Pfad zur Datei.
     *                   Der Pfad ist absolut und ohne Angabe des Buckets anzugeben.
     *                   Beispiel:
     *                   Datei in Bucket: "BUCKET/outerFolder/innerFolder/thefile.csv"
     *                   Angabe in Parameter: "outerFolder/innerFolder/thefile.csv"
     * @throws S3AccessException falls die Datei nicht gelöscht werden kann.
     */
    public void deleteFile(final String pathToFile) throws S3AccessException {
        try {
            final RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(this.bucketName)
                    .object(pathToFile)
                    .build();
            this.client.removeObject(removeObjectArgs);
        } catch (final InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                | IllegalArgumentException | IOException exception) {
            throw new S3AccessException(String.format("Failed to delete file %s.", pathToFile), exception);
        }
    }

    /**
     * Erstellt die Presigned-URL zum Herunterladen einer Datei von einem gegebenen Dateipfad.
     *
     * @param pathToFile       Der Pfad zur Datei.
     *                         Der Pfad ist absolut und ohne Angabe des Buckets anzugeben.
     *                         Beispiel:
     *                         Datei in Bucket: "BUCKET/outerFolder/innerFolder/thefile.csv"
     *                         Angabe in Parameter: "outerFolder/innerFolder/thefile.csv"
     * @param expiresInMinutes zur Definition des Gültigkeitszeitraums der Presigned-URL.
     * @return die Presigned URL zum holen einer Datei.
     * @throws S3AccessException falls die Datei nicht heruntergeladen werden kann.
     */
    public String getPresignedUrlForFileDownload(final String pathToFile, final int expiresInMinutes) throws S3AccessException {
        try {
            final GetPresignedObjectUrlArgs downloadArgs = GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(this.bucketName)
                    .object(pathToFile)
                    .expiry(expiresInMinutes, TimeUnit.MINUTES)
                    .build();
            return this.client.getPresignedObjectUrl(downloadArgs);
        } catch (final InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                | IllegalArgumentException | IOException exception) {
            throw new S3AccessException(String.format("Failed to create a download presigned url for file %s.", pathToFile), exception);
        }
    }

    /**
     * Erstellt die Presigned-URL zum Löschen einer Datei gegeben durch Dateipfad.
     *
     * @param pathToFile       Der Pfad zur Datei.
     *                         Der Pfad ist absolut und ohne Angabe des Buckets anzugeben.
     *                         Beispiel:
     *                         Datei in Bucket: "BUCKET/outerFolder/innerFolder/thefile.csv"
     *                         Angabe in Parameter: "outerFolder/innerFolder/thefile.csv"
     * @param expiresInMinutes zur Definition des Gültigkeitszeitraums der Presigned-URL.
     * @return die Presigned URL zum holen einer Datei.
     * @throws S3AccessException falls die Datei nicht heruntergeladen werden kann.
     */
    public String getPresignedUrlForFileDeletion(final String pathToFile, final int expiresInMinutes) throws S3AccessException {
        try {
            final GetPresignedObjectUrlArgs deletionArgs = GetPresignedObjectUrlArgs.builder()
                    .method(Method.DELETE)
                    .bucket(this.bucketName)
                    .object(pathToFile)
                    .expiry(expiresInMinutes, TimeUnit.MINUTES)
                    .build();
            return this.client.getPresignedObjectUrl(deletionArgs);
        } catch (final InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                | IllegalArgumentException | IOException exception) {
            throw new S3AccessException(String.format("Failed to create a deletion presigned url for file %s.", pathToFile), exception);
        }
    }

    /**
     * Erstellt die Presigned-URL zum Hochladen einer Datei in den gegebenen Dateipfad.
     *
     * @param pathToFile       Der Pfad zur Datei.
     *                         Der Pfad ist absolut und ohne Angabe des Buckets anzugeben.
     *                         Beispiel:
     *                         Datei in Bucket: "BUCKET/outerFolder/innerFolder/thefile.csv"
     *                         Angabe in Parameter: "outerFolder/innerFolder/thefile.csv"
     * @param expiresInMinutes zur Definition des Gültigkeitszeitraums der Presigned-URL.
     * @return die Presigned URL zum holen einer Datei.
     * @throws S3AccessException falls die Datei nicht heruntergeladen werden kann.
     */
    public String getPresignedUrlForFileUpload(final String pathToFile, final int expiresInMinutes) throws S3AccessException {
        try {
            final GetPresignedObjectUrlArgs uploadArgs = GetPresignedObjectUrlArgs.builder()
                    .method(Method.PUT)
                    .bucket(this.bucketName)
                    .object(pathToFile)
                    .expiry(expiresInMinutes, TimeUnit.MINUTES)
                    .build();
            return this.client.getPresignedObjectUrl(uploadArgs);
        } catch (final InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                | IllegalArgumentException | IOException exception) {
            throw new S3AccessException(String.format("Failed to create a upload presigned url for file %s.", pathToFile), exception);
        }
    }

}
