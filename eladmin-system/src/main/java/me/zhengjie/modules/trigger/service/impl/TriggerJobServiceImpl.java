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
package me.zhengjie.modules.trigger.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.onedawn.mytrigger.request.impl.TriggerRequest;
import cn.onedawn.mytrigger.utils.ConstValue;
import cn.onedawn.mytrigger.utils.StatusCode;
import com.alibaba.fastjson.JSON;
import me.zhengjie.modules.trigger.domain.TriggerJob;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.trigger.repository.TriggerJobRepository;
import me.zhengjie.modules.trigger.service.TriggerJobService;
import me.zhengjie.modules.trigger.service.dto.TriggerJobDto;
import me.zhengjie.modules.trigger.service.dto.TriggerJobQueryCriteria;
import me.zhengjie.modules.trigger.service.mapstruct.TriggerJobMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author yuqingming
* @date 2022-04-09
**/
@Service
@RequiredArgsConstructor
public class TriggerJobServiceImpl implements TriggerJobService {

    private final TriggerJobRepository triggerJobRepository;
    private final TriggerJobMapper triggerJobMapper;

    @Override
    public Map<String,Object> queryAll(TriggerJobQueryCriteria criteria, Pageable pageable){
        Page<TriggerJob> page = triggerJobRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(triggerJobMapper::toDto));
    }

    @Override
    public List<TriggerJobDto> queryAll(TriggerJobQueryCriteria criteria){
        return triggerJobMapper.toDto(triggerJobRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TriggerJobDto findById(Long id) {
        TriggerJob triggerJob = triggerJobRepository.findById(id).orElseGet(TriggerJob::new);
        ValidationUtil.isNull(triggerJob.getId(),"TriggerJob","id",id);
        return triggerJobMapper.toDto(triggerJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TriggerJobDto create(TriggerJob resources) {
        return triggerJobMapper.toDto(triggerJobRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TriggerJob resources) {
        TriggerJob triggerJob = triggerJobRepository.findById(resources.getId()).orElseGet(TriggerJob::new);
        ValidationUtil.isNull( triggerJob.getId(),"TriggerJob","id",resources.getId());
        triggerJob.copy(resources);
        triggerJobRepository.save(triggerJob);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            triggerJobRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TriggerJobDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TriggerJobDto triggerJob : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("状态", triggerJob.getStatus());
            map.put("调度时间", triggerJob.getTriggerTime());
            map.put("是否删除", triggerJob.getRemove());
            map.put("回调名称", triggerJob.getCallName());
            map.put("回调数据", triggerJob.getCallData());
            map.put("回调类型", triggerJob.getCallType());
            map.put("回调主机", triggerJob.getCallHost());
            map.put("CRON", triggerJob.getCron());
            map.put("创建时间", triggerJob.getCreateTime());
            map.put("修改时间", triggerJob.getModifyTime());
            map.put("应用id", triggerJob.getApp());
            map.put("重试次数", triggerJob.getCallerrorRetryCount());
            map.put("未ack重试次数", triggerJob.getRunRetry());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Boolean trigger(Long id) {
        TriggerRequest triggerRequest = new TriggerRequest();
        Map<String, Object> formMap = new HashMap<>();
        triggerRequest.setJobId(id);
        String url = "http://" + ConstValue.SERVER + ":" + ConstValue.SERVER_PORT + "/job/trigger";
        formMap.put(ConstValue.REQUEST_DATA, JSON.toJSONString(triggerRequest));
        HttpResponse response = HttpRequest.post(url)
                .form(formMap)
                .timeout(10000)
                .execute();
        return response.getStatus() == StatusCode.SUCCESS;
    }
}