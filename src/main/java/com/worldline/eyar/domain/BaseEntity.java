package com.worldline.eyar.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@MappedSuperclass
@ToString
public class BaseEntity implements DBEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "ACTIVE")
    private Boolean active = Boolean.TRUE;

    @CreatedBy
    @Column(name = "GENERATED_BY")
    private String generatedBy;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @CreatedDate
    @Column(name = "SUBMIT_TIME")
    private Instant submittedTime;

    @LastModifiedDate
    @Column(name = "MODIFICATION_TIME")
    private Instant modificationTime;

    @Version
    @Column(name = "VERSION")
    private Integer version;

    @Override
    public Long getId() {
        return this.id;
    }

    public interface BaseEntityFields {
        FieldMetaData<String> ID = new FieldMetaData<>("id");
        FieldMetaData<Boolean> ACTIVE = new FieldMetaData<>("active");
        FieldMetaData<Instant> MODIFICATION_TIME = new FieldMetaData<>("modificationTime");
        FieldMetaData<String> GENERATED_BY = new FieldMetaData<>("generatedBy");
        FieldMetaData<String> LAST_MODIFIED_BY = new FieldMetaData<>("lastModifiedBy");
        FieldMetaData<Instant> SUBMITTED_TIME = new FieldMetaData<>("submittedTime");
    }
}
