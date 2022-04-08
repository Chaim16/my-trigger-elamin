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
package me.zhengjie.modules.history.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author yuqingming
* @date 2022-04-09
**/
@Data
public class HistoryJobDto implements Serializable {

    /** ID */
    private Long id;

    /** 调度时间 */
    private Timestamp triggerTime;

    /** 回调名称 */
    private String callName;

    /** 回调数据 */
    private String callData;

    /** 回调方式 */
    private String callType;

    /** 回调主机 */
    private String callHost;

    /** CRON */
    private String cron;

    /** 创建时间 */
    private Timestamp createTime;

    /** 修改时间 */
    private Timestamp modifyTime;

    /** 应用名 */
    private Long app;

    /** 重试次数 */
    private Integer callerrorRetryCount;

    /** run重试 */
    private Integer runRetry;
}