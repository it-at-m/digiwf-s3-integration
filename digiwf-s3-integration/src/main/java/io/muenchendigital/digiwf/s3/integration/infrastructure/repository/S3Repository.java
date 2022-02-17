package io.muenchendigital.digiwf.s3.integration.infrastructure.repository;

import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AccessException;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class S3Repository {

    private final String bucketName;
    private final MinioClient client;

    public S3Repository(String bucketName, MinioClient client) throws S3AccessException {
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
     * Hochladen einer Datei in einen gegebenen Ordner.
     *
     * @param file       als Inputstream.
     * @param pathToFile Der Pfad zur Datei.
     *                   Der Pfad ist absolut und ohne Angabe des Buckets anzugeben.
     *                   Beispiel:
     *                   Datei in Bucket: "BUCKET/outerFolder/innerFolder/thefile.csv"
     *                   Angabe in Parameter: "outerFolder/innerFolder/thefile.csv"
     * @throws S3AccessException falls die Datei nicht hochgeladen werden kann.
     */
    public void uploadFile(final String pathToFile, final InputStream file) throws S3AccessException {
        try {
            final PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(this.bucketName)
                    .object(pathToFile)
                    .stream(file, file.available(), -1)
                    .build();
            this.client.putObject(putObjectArgs);
        } catch (final MinioException | InvalidKeyException | NoSuchAlgorithmException | IllegalArgumentException | IOException exception) {
            throw new S3AccessException(String.format("Failed to upload file %s.", pathToFile), exception);
        }
    }


    /**
     * Herunterladen einer Datei von einem gegebenen Ordner.
     *
     * @param pathToFile Der Pfad zur Datei.
     *                   Der Pfad ist absolut und ohne Angabe des Buckets anzugeben.
     *                   Beispiel:
     *                   Datei in Bucket: "BUCKET/outerFolder/innerFolder/thefile.csv"
     *                   Angabe in Parameter: "outerFolder/innerFolder/thefile.csv"
     * @return die heruntergeladene Datei als InputStream.
     * @throws S3AccessException falls die Datei nicht heruntergeladen werden kann.
     */
    public InputStream downloadFile(final String pathToFile) throws S3AccessException {
        final GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(this.bucketName)
                .object(pathToFile)
                .build();
        try {
            return this.client.getObject(getObjectArgs);
        } catch (final InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                | IllegalArgumentException | IOException exception) {
            throw new S3AccessException(String.format("Failed to download file %s.", pathToFile), exception);
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

}
