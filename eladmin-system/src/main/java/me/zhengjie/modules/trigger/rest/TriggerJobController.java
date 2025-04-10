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
package me.zhengjie.modules.trigger.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.modules.trigger.domain.TriggerJob;
import me.zhengjie.modules.trigger.service.TriggerJobService;
import me.zhengjie.modules.trigger.service.dto.TriggerJobQueryCriteria;
import me.zhengjie.utils.CronUtil;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author yuqingming
* @date 2022-04-09
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "api/triggerJob管理")
@RequestMapping("/api/triggerJob")
public class TriggerJobController {

    private final TriggerJobService triggerJobService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('triggerJob:list')")
    public void exportTriggerJob(HttpServletResponse response, TriggerJobQueryCriteria criteria) throws IOException {
        triggerJobService.download(triggerJobService.queryAll(criteria), response);
    }

    @Log("手动调度")
    @ApiOperation("手动调度")
    @PostMapping(value = "/trigger")
    @PreAuthorize("@el.check('triggerJob:list')")
    public ResponseEntity<Object> triggerJob(@RequestBody Long id) {
        return new ResponseEntity<>(triggerJobService.trigger(id), HttpStatus.OK);
    }

    @GetMapping
    @Log("查询api/triggerJob")
    @ApiOperation("查询api/triggerJob")
    @PreAuthorize("@el.check('triggerJob:list')")
    public ResponseEntity<Object> queryTriggerJob(TriggerJobQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(triggerJobService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api/triggerJob")
    @ApiOperation("新增api/triggerJob")
    @PreAuthorize("@el.check('triggerJob:add')")
    public ResponseEntity<Object> createTriggerJob(@Validated @RequestBody TriggerJob resources) throws Exception {
        resources.setStatus("wait");
        Date time = null;
        if (CronUtil.checkCronOneTime(resources.getCron())) {
            time = CronUtil.getOnceTime(resources.getCron());
        } else {
            time = CronUtil.getLoopTime(resources.getCron(), System.currentTimeMillis());
        }
        resources.setTriggerTime(new Timestamp(time.getTime()));
        resources.setRemove(0);
        resources.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity<>(triggerJobService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改api/triggerJob")
    @ApiOperation("修改api/triggerJob")
    @PreAuthorize("@el.check('triggerJob:edit')")
    public ResponseEntity<Object> updateTriggerJob(@Validated @RequestBody TriggerJob resources) throws Exception {
        Date time = null;
        if (CronUtil.checkCronOneTime(resources.getCron())) {
            time = CronUtil.getOnceTime(resources.getCron());
        } else {
            time = CronUtil.getLoopTime(resources.getCron(), System.currentTimeMillis());
        }
        resources.setTriggerTime(new Timestamp(time.getTime()));
        resources.setCreateTime(new Timestamp(System.currentTimeMillis()));
        triggerJobService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除api/triggerJob")
    @ApiOperation("删除api/triggerJob")
    @PreAuthorize("@el.check('triggerJob:del')")
    public ResponseEntity<Object> deleteTriggerJob(@RequestBody Long[] ids) {
        triggerJobService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}