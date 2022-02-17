package io.muenchendigital.digiwf.s3.integration.domain.service;

import io.muenchendigital.digiwf.s3.integration.domain.exception.BrokenFileException;
import io.muenchendigital.digiwf.s3.integration.domain.exception.FileExistanceException;
import io.muenchendigital.digiwf.s3.integration.domain.model.FileData;
import io.muenchendigital.digiwf.s3.integration.domain.model.FileResource;
import io.muenchendigital.digiwf.s3.integration.infrastructure.entity.Folder;
import io.muenchendigital.digiwf.s3.integration.infrastructure.exception.S3AccessException;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.S3Repository;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FileHandlingServiceTest {

    @Mock
    private S3Repository s3Repository;

    @Mock
    private FolderRepository folderRepository;

    private FileHandlingService fileHandlingService;

    @BeforeEach
    public void beforeEach() {
        this.fileHandlingService = new FileHandlingService(this.s3Repository, this.folderRepository);
    }

    @Test
    void getFileException() throws Exception {
        final UUID uuid = UUID.randomUUID();
        final String filename = "test.txt";
        Mockito.when(this.s3Repository.getFilepathesFromFolder(uuid.toString())).thenReturn(new HashSet<>());
        Assertions.assertThrows(FileExistanceException.class, () -> this.fileHandlingService.getFile(uuid.toString(), filename));
    }

    @Test
    void getFile() throws S3AccessException, IOException, FileExistanceException {
        final UUID uuid = UUID.randomUUID();
        final String filename = "test.txt";
        final String filePath = uuid + "/" + filename;
        final InputStream file = IOUtils.toInputStream("the file content", "UTF-8");

        Mockito.when(this.s3Repository.getFilepathesFromFolder(uuid.toString())).thenReturn(new HashSet<>(List.of(filePath)));
        Mockito.when(this.s3Repository.downloadFile(filePath)).thenReturn(file);

        final FileResource result = this.fileHandlingService.getFile(uuid.toString(), filename);

        final FileResource expected = new FileResource();
        expected.setResource(new InputStreamResource(file));
        expected.setFilename(filename);

        assertThat(result, is(expected));
    }

    @Test
    void saveFile() throws IOException, S3AccessException, BrokenFileException, FileExistanceException {
        final UUID uuid = UUID.randomUUID();
        final String filename = "test.txt";
        final String filePath = uuid + "/" + filename;
        final InputStream file = IOUtils.toInputStream("the file content", "UTF-8");
        final MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockFile.getOriginalFilename()).thenReturn(filename);
        Mockito.when(mockFile.getInputStream()).thenReturn(file);

        final FileData fileData = new FileData();
        fileData.setEndOfLife(LocalDate.of(2022, 1, 1));
        fileData.setRefId(uuid.toString());
        fileData.setMultipartFile(mockFile);

        Mockito.when(this.s3Repository.getFilepathesFromFolder(uuid.toString())).thenReturn(new HashSet<>(List.of(filePath)));
        Assertions.assertThrows(FileExistanceException.class, () -> this.fileHandlingService.saveFile(fileData));
    }

    @Test
    void updateFile() throws IOException, S3AccessException, BrokenFileException {
        final UUID uuid = UUID.randomUUID();
        final String filename = "test.txt";
        final String filePath = uuid + "/" + filename;
        final InputStream file = IOUtils.toInputStream("the file content", "UTF-8");
        final MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockFile.getOriginalFilename()).thenReturn(filename);
        Mockito.when(mockFile.getInputStream()).thenReturn(file);

        final FileData fileData = new FileData();
        fileData.setEndOfLife(LocalDate.of(2022, 1, 1));
        fileData.setRefId(uuid.toString());
        fileData.setMultipartFile(mockFile);

        Mockito.when(this.folderRepository.findByRefId(uuid.toString())).thenReturn(Optional.empty());
        this.fileHandlingService.updateFile(fileData);
        final var folderToSave1 = new Folder();
        folderToSave1.setRefId(fileData.getRefId());
        folderToSave1.setEndOfLife(fileData.getEndOfLife());
        Mockito.verify(this.folderRepository, Mockito.times(1)).save(folderToSave1);
        Mockito.verify(this.s3Repository, Mockito.times(1)).uploadFile(filePath, file);


        Mockito.reset(this.folderRepository);
        Mockito.reset(this.s3Repository);
        final var folderToFind1 = new Folder();
        folderToFind1.setRefId(fileData.getRefId());
        folderToFind1.setEndOfLife(fileData.getEndOfLife().minusYears(1));
        Mockito.when(this.folderRepository.findByRefId(uuid.toString())).thenReturn(Optional.of(folderToFind1));
        this.fileHandlingService.updateFile(fileData);
        final var folderToSave2 = new Folder();
        folderToSave2.setRefId(folderToFind1.getRefId());
        folderToSave2.setEndOfLife(fileData.getEndOfLife());
        Mockito.verify(this.folderRepository, Mockito.times(1)).save(folderToSave2);
        Mockito.verify(this.s3Repository, Mockito.times(1)).uploadFile(filePath, file);


        Mockito.reset(this.folderRepository);
        Mockito.reset(this.s3Repository);
        final var folderToFind2 = new Folder();
        folderToFind2.setRefId(fileData.getRefId());
        folderToFind2.setEndOfLife(fileData.getEndOfLife().plusYears(1));
        Mockito.when(this.folderRepository.findByRefId(uuid.toString())).thenReturn(Optional.of(folderToFind2));
        this.fileHandlingService.updateFile(fileData);
        final var folderToSave3 = new Folder();
        folderToSave2.setRefId(folderToFind2.getRefId());
        folderToSave2.setEndOfLife(fileData.getEndOfLife());
        Mockito.verify(this.folderRepository, Mockito.times(0)).save(folderToSave2);
        Mockito.verify(this.s3Repository, Mockito.times(1)).uploadFile(filePath, file);


        Mockito.reset(this.folderRepository);
        Mockito.reset(this.s3Repository);
        final var folderToFind3 = new Folder();
        folderToFind3.setRefId(fileData.getRefId());
        folderToFind3.setEndOfLife(fileData.getEndOfLife().plusYears(1));
        Mockito.when(this.folderRepository.findByRefId(uuid.toString())).thenReturn(Optional.of(folderToFind3));
        Mockito.doThrow(new IOException()).when(mockFile).getInputStream();
        Assertions.assertThrows(BrokenFileException.class, () -> this.fileHandlingService.updateFile(fileData));
        final var folderToSave4 = new Folder();
        folderToSave4.setRefId(folderToFind3.getRefId());
        folderToSave4.setEndOfLife(fileData.getEndOfLife());
        Mockito.verify(this.folderRepository, Mockito.times(0)).save(folderToSave4);

    }

    @Test
    void deleteFileException() throws S3AccessException {
        final UUID uuid = UUID.randomUUID();
        final String filename = "test.txt";
        Mockito.when(this.s3Repository.getFilepathesFromFolder(uuid.toString())).thenReturn(new HashSet<>());
        Assertions.assertThrows(FileExistanceException.class, () -> this.fileHandlingService.deleteFile(uuid.toString(), filename));
    }

    @Test
    void deleteFile() throws S3AccessException, FileExistanceException {
        final UUID uuid = UUID.randomUUID();
        final String filename = "test.txt";
        final String filePath = uuid + "/" + filename;
        Mockito.when(this.s3Repository.getFilepathesFromFolder(uuid.toString())).thenReturn(new HashSet<>(List.of(filePath)));
        this.fileHandlingService.deleteFile(uuid.toString(), filename);
        Mockito.verify(this.s3Repository, Mockito.times(1)).deleteFile(filePath);
    }

    @Test
    void createFilePath() {
        assertThat(FileHandlingService.createFilePath("folder", "file.txt"), is("folder/file.txt"));
        assertThat(FileHandlingService.createFilePath(null, "file.txt"), is("null/file.txt"));
        assertThat(FileHandlingService.createFilePath("folder", null), is("folder/null"));
        assertThat(FileHandlingService.createFilePath(null, null), is("null/null"));
    }

    @Test
    void shouldNewEndOfLifeBeSet() {
        final var fileData = new FileData();
        final var folder = new Folder();

        fileData.setEndOfLife(null);
        folder.setEndOfLife(null);
        assertThat(FileHandlingService.shouldNewEndOfLifeBeSet(fileData, folder), is(false));

        fileData.setEndOfLife(LocalDate.of(2022, 1, 1));
        folder.setEndOfLife(LocalDate.of(2021, 1, 1));
        assertThat(FileHandlingService.shouldNewEndOfLifeBeSet(fileData, folder), is(true));

        fileData.setEndOfLife(LocalDate.of(2022, 1, 1));
        folder.setEndOfLife(null);
        assertThat(FileHandlingService.shouldNewEndOfLifeBeSet(fileData, folder), is(true));

        fileData.setEndOfLife(null);
        folder.setEndOfLife(LocalDate.of(2021, 1, 1));
        assertThat(FileHandlingService.shouldNewEndOfLifeBeSet(fileData, folder), is(false));

        fileData.setEndOfLife(LocalDate.of(2021, 1, 1));
        folder.setEndOfLife(LocalDate.of(2022, 1, 1));
        assertThat(FileHandlingService.shouldNewEndOfLifeBeSet(fileData, folder), is(false));
    }

}
