package io.muenchendigital.digiwf.s3.integration.infrastructure.entity;

import io.muenchendigital.digiwf.s3.integration.infrastructure.repository.FolderRepository;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(indexes = {
        @Index(
                name = "index_ref_id",
                columnList = "ref_id"
        ),
        @Index(
                name = "index_end_of_life",
                columnList = "end_of_life"
        )
})
public class Folder extends BaseEntity {

    @Column(name = "ref_id", nullable = false, unique = true, length = FolderRepository.LENGTH_REF_ID)
    @Type(type = "uuid-char")
    private String refId;

    @Column(name = "end_of_life")
    private LocalDate endOfLife;

}
