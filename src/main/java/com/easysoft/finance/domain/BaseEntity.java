package com.easysoft.finance.domain;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(/*strategy = GenerationType.IDENTITY*/)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @LastModifiedDate
    private Date updatedDate;

    public BaseEntity(Date createdDate) {
        this.createdDate = createdDate;
    }

    public BaseEntity() {
        this.createdDate = new Date();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Transient
    public String convertActiveFlag(String active) {
        if ("on".equalsIgnoreCase(active) || "Y".equalsIgnoreCase(active) || "true".equals(active)) {
            return "Y";
        } else {
            return "N";
        }
    }

    @Transient
    public boolean isActive(String active) {
        if ("on".equalsIgnoreCase(active) || "Y".equalsIgnoreCase(active) || "true".equals(active)) {
            return true;
        } else {
            return false;
        }
    }
}