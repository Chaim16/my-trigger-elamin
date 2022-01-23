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
package me.zhengjie.modules.trigger.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author yuqingming
* @date 2022-01-23
**/
@Data
public class TriggerJobDto implements Serializable {

    private Long id;

    /** 状态 */
    private String status;

    /** 调度时间 */
    private Timestamp triggerTime;

    /** 是否删除 */
    private Integer remove;

    /** 回调名称 */
    private String callName;

    /** 回调数据 */
    private String callData;

    /** 回调类型 */
    private String callType;

    /** 回调主机 */
    private String callHost;

    /** 表达式 */
    private String cron;

    /** 创建时间 */
    private Timestamp createTime;

    /** 修改时间 */
    private Timestamp modifyTime;

    /** 应用id */
    private Long app;

    /** 重试次数 */
    private Integer callerrorRetryCount;

    /** 未ack重试次数 */
    private Integer runRetry;
}