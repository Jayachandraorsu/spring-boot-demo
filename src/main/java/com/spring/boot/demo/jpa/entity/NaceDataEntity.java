package com.spring.boot.demo.jpa.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "NACE_DATA")
@Entity
@Data
public class NaceDataEntity {

    @Id
    @Column(name = "ORDER_ID")
    private Long orderId;

    @Column(name = "LEVEL")
    private Long level;

    @Column(name = "CODE")
    private String code;

    @Column(name = "PARENT")
    private String parent;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ITEMS_INCLUDE")
    private String itemsInclude;

    @Column(name = "ITEMS_ALSO_INCLUDE")
    private String itemsAlsoInclude;

    @Column(name = "RULING")
    private String ruling;

    @Column(name = "ITEMS_EXCLUDE")
    private String itemsExclude;

    @Column(name = "REFERENCE_TO_ISIC")
    private String referenceToIsic;
}
