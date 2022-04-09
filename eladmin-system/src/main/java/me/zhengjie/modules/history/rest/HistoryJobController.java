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

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.onedawn.mytrigger.exception.MyTriggerException;
import cn.onedawn.mytrigger.pojo.Application;
import cn.onedawn.mytrigger.pojo.Job;
import cn.onedawn.mytrigger.type.CallType;
import cn.onedawn.mytrigger.type.JobStatusType;
import cn.onedawn.mytrigger.utils.ConstValue;
import cn.onedawn.mytrigger.utils.SpringBeanFactory;
import cn.onedawn.mytrigger.utils.StatusCode;
import com.alibaba.fastjson.JSON;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.history.domain.HistoryJob;
import me.zhengjie.modules.history.service.dto.HistoryJobDto;
import me.zhengjie.modules.history.service.HistoryJobService;
import me.zhengjie.modules.history.service.dto.HistoryJobQueryCriteria;
import org.apache.http.HttpHost;
import org.apache.poi.ss.formula.functions.T;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yuqingming
 * @website https://el-admin.vip
 * @date 2022-04-09
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "api/history管理")
@RequestMapping("/api/historyJob")
public class HistoryJobController {

    private final HistoryJobService historyJobService;
    private static String elasticsearchHost;
    private static Integer elasticsearchPort;
    private static String elasticsearchIndexName;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");


    @Value("${elasticsearch.host}")
    public void setElasticsearchHost(String elasticsearchHost) {
        HistoryJobController.elasticsearchHost = elasticsearchHost;
    }

    @Value("${elasticsearch.port}")
    public void setElasticsearchPort(Integer elasticsearchPort) {
        HistoryJobController.elasticsearchPort = elasticsearchPort;
    }

    @Value("${elasticsearch.index.name}")
    public void setElasticsearchIndexName(String elasticsearchIndexName) {
        HistoryJobController.elasticsearchIndexName = elasticsearchIndexName;
    }

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
    public ResponseEntity<Object> queryHistoryJob(HistoryJobQueryCriteria criteria, Pageable pageable) throws MyTriggerException {
        HistoryJobDto historyJobDto = new HistoryJobDto();
        Map<String, Object> resiultMap = new HashMap<>();
        if (criteria.getId() != null) {
            historyJobDto.setId(criteria.getId());
        }
        if (criteria.getCallName() != null) {
            historyJobDto.setCallName(criteria.getCallName());
        }
        if (criteria.getCallHost() != null) {
            historyJobDto.setCallHost(criteria.getCallHost());
        }
        if (criteria.getCallData() != null) {
            historyJobDto.setCallData(criteria.getCallData());
        }
        if (criteria.getCron() != null) {
            historyJobDto.setCron(criteria.getCron());
        }
        if (criteria.getCallType() != null) {
            historyJobDto.setCallType(criteria.getCallType());
        }
        if (criteria.getApp() != null) {
            historyJobDto.setApp(criteria.getApp());
        }

        List<HistoryJobDto> historyJobDtos = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(elasticsearchIndexName);
//        searchRequest.types("_doc");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        if (historyJobDto.getCallName() != null) {
            sourceBuilder.query(QueryBuilders.termQuery("callName", historyJobDto.getCallName()));
        }
        if (historyJobDto.getId() != null) {
            sourceBuilder.query(QueryBuilders.termQuery("id", historyJobDto.getId()));
        }
        if (historyJobDto.getCallData() != null) {
            sourceBuilder.query(QueryBuilders.termQuery("callData", historyJobDto.getCallData()));
        }
        if (historyJobDto.getCallType() != null) {
            sourceBuilder.query(QueryBuilders.termQuery("callType", historyJobDto.getCallType()));
        }
        if (historyJobDto.getCallHost() != null) {
            sourceBuilder.query(QueryBuilders.termQuery("callHost", historyJobDto.getCallHost()));
        }
        if (historyJobDto.getCron() != null) {
            sourceBuilder.query(QueryBuilders.termQuery("cron", historyJobDto.getCron()));
        }
        String appName = null;
        if (historyJobDto.getApp() != null) {
            String url = ConstValue.BASE_URL + "/application/findAppNameById?id=" + historyJobDto.getApp();
            HttpResponse response = HttpRequest.get(url).timeout(5000).execute();

            if (response.getStatus() == StatusCode.SUCCESS) {
                String body = response.body();
                appName = String.valueOf(body);
            } else {
                throw new MyTriggerException("find appId faild");
            }
            if (appName != null) {
                sourceBuilder.query(QueryBuilders.termQuery("appName", appName));
            }
        }
        sourceBuilder.from((int) pageable.getOffset());
        sourceBuilder.sort("_score");
        sourceBuilder.size(pageable.getPageSize());
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchRequest.source(sourceBuilder);
        try (RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(elasticsearchHost, elasticsearchPort, "http")))) {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                HistoryJobDto task = new HistoryJobDto();
                task.setId(Long.valueOf((Integer) sourceAsMap.get("id")));
                task.setCallType((String) sourceAsMap.get("callType"));

                String url = ConstValue.BASE_URL + "/application/findAppIdByAppName?name=" + sourceAsMap.get("appName");
                HttpResponse response = HttpRequest.get(url).timeout(5000).execute();
                if (response.getStatus() != 200) {
                    throw new MyTriggerException("find appId faild");
                }
                String body = response.body();
                Long appId = Long.valueOf(body);

                task.setApp(appId);
                task.setCron(String.valueOf(sourceAsMap.get("cron")));
                task.setCallData(String.valueOf(sourceAsMap.get("callData")));
                task.setCallHost(String.valueOf(sourceAsMap.get("callHost")));
                task.setRunRetry((Integer) sourceAsMap.get("runRetry"));
                task.setCallName((String) sourceAsMap.get("callName"));
                task.setCallerrorRetryCount((Integer) sourceAsMap.get("callerrorRetryCount"));
                String triggerTime = (String) sourceAsMap.get("triggerTime");
                String createTime = (String) sourceAsMap.get("createTime");
                String modifyTime = (String) sourceAsMap.get("modifyTime");
                task.setTriggerTime(new Timestamp(simpleDateFormat.parse(triggerTime).getTime()));
                task.setCreateTime(new Timestamp(simpleDateFormat.parse(createTime).getTime()));
                task.setModifyTime(new Timestamp(simpleDateFormat.parse(modifyTime).getTime()));
                historyJobDtos.add(task);
            }
            resiultMap.put("content", historyJobDtos);
            resiultMap.put("totalElements", historyJobDtos.size());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(resiultMap, HttpStatus.OK);
    }

    @PostMapping
    @Log("新增api/history")
    @ApiOperation("新增api/history")
    @PreAuthorize("@el.check('historyJob:add')")
    public ResponseEntity<Object> createHistoryJob(@Validated @RequestBody HistoryJob resources) {
        return new ResponseEntity<>(historyJobService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改api/history")
    @ApiOperation("修改api/history")
    @PreAuthorize("@el.check('historyJob:edit')")
    public ResponseEntity<Object> updateHistoryJob(@Validated @RequestBody HistoryJob resources) {
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