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
package me.zhengjie.modules.history.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.modules.history.domain.HistoryJob;
import me.zhengjie.modules.history.service.HistoryJobService;
import me.zhengjie.modules.history.service.dto.HistoryJobQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author yuqingming
* @date 2022-04-09
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "api/history管理")
@RequestMapping("/api/historyJob")
public class HistoryJobController {

    private final HistoryJobService historyJobService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('historyJob:list')")
    public void exportHistoryJob(HttpServletResponse response, HistoryJobQueryCriteria criteria) throws IOException {
        historyJobService.download(historyJobService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询api/history")
    @ApiOperation("查询api/history")
    @PreAuthorize("@el.check('historyJob:list')")
    public ResponseEntity<Object> queryHistoryJob(HistoryJobQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(historyJobService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api/history")
    @ApiOperation("新增api/history")
    @PreAuthorize("@el.check('historyJob:add')")
    public ResponseEntity<Object> createHistoryJob(@Validated @RequestBody HistoryJob resources){
        return new ResponseEntity<>(historyJobService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改api/history")
    @ApiOperation("修改api/history")
    @PreAuthorize("@el.check('historyJob:edit')")
    public ResponseEntity<Object> updateHistoryJob(@Validated @RequestBody HistoryJob resources){
        historyJobService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除api/history")
    @ApiOperation("删除api/history")
    @PreAuthorize("@el.check('historyJob:del')")
    public ResponseEntity<Object> deleteHistoryJob(@RequestBody Long[] ids) {
        historyJobService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}