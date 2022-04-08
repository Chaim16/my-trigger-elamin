/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.modules.trigger.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author yuqingming
* @date 2022-04-09
**/
@Entity
@Data
@Table(name="trigger_job")
public class TriggerJob implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "任务ID")
    private Long id;

    @Column(name = "status")
    @ApiModelProperty(value = "状态")
    private String status;

    @Column(name = "trigger_time")
    @ApiModelProperty(value = "调度时间")
    private Timestamp triggerTime;

    @Column(name = "remove")
    @ApiModelProperty(value = "是否删除")
    private Integer remove;

    @Column(name = "call_name",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "回调名称")
    private String callName;

    @Column(name = "call_data")
    @ApiModelProperty(value = "回调数据")
    private String callData;

    @Column(name = "call_type")
    @ApiModelProperty(value = "回调类型")
    private String callType;

    @Column(name = "call_host")
    @ApiModelProperty(value = "回调主机")
    private String callHost;

    @Column(name = "cron",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "CRON")
    private String cron;

    @Column(name = "create_time")
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "modify_time")
    @UpdateTimestamp
    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyTime;

    @Column(name = "app",nullable = false)
    @NotNull
    @ApiModelProperty(value = "应用id")
    private Long app;

    @Column(name = "callerror_retry_count")
    @ApiModelProperty(value = "重试次数")
    private Integer callerrorRetryCount;

    @Column(name = "run_retry")
    @ApiModelProperty(value = "未ack重试次数")
    private Integer runRetry;

    public void copy(TriggerJob source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}