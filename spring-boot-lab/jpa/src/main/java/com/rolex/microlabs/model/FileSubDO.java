/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author rolex
 * @since 2019
 */
@Entity
@Data
@Table(name = "t_file_sub")

public class FileSubDO {

    @Id
    String id;
    @Column(name = "user_id")
    String userId;
    String uid;
    @Column(name = "customer_id")
    String customerId;
    @Column(name = "local_name_file")
    String localNameFile;
    @Column(name = "remote_name_file_url")
    String remoteNameFileUrl;
    @Column(name = "local_result_file")
    String localResultFile;
    @Column(name = "remote_result_file_url")
    String remoteResultFileUrl;
    String state;
    @Column(name = "succeeded_count")
    Integer succeededCount;
    @Column(name = "existed_count")
    Integer existedCount;
    @Column(name = "not_found_count")
    Integer notFoundCount;
    Date idt;
    Date udt;

}
