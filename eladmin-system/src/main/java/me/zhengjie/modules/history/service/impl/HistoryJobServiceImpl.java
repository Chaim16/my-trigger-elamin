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
package me.zhengjie.modules.history.service.impl;

import me.zhengjie.modules.history.domain.HistoryJob;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.history.repository.HistoryJobRepository;
import me.zhengjie.modules.history.service.HistoryJobService;
import me.zhengjie.modules.history.service.dto.HistoryJobDto;
import me.zhengjie.modules.history.service.dto.HistoryJobQueryCriteria;
import me.zhengjie.modules.history.service.mapstruct.HistoryJobMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author yuqingming
* @date 2022-04-09
**/
@Service
@RequiredArgsConstructor
public class HistoryJobServiceImpl implements HistoryJobService {

    private final HistoryJobRepository historyJobRepository;
    private final HistoryJobMapper historyJobMapper;

    @Override
    public Map<String,Object> queryAll(HistoryJobQueryCriteria criteria, Pageable pageable){
        Page<HistoryJob> page = historyJobRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(historyJobMapper::toDto));
    }

    @Override
    public List<HistoryJobDto> queryAll(HistoryJobQueryCriteria criteria){
        return historyJobMapper.toDto(historyJobRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public HistoryJobDto findById(Long id) {
        HistoryJob historyJob = historyJobRepository.findById(id).orElseGet(HistoryJob::new);
        ValidationUtil.isNull(historyJob.getId(),"HistoryJob","id",id);
        return historyJobMapper.toDto(historyJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HistoryJobDto create(HistoryJob resources) {
        return historyJobMapper.toDto(historyJobRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(HistoryJob resources) {
        HistoryJob historyJob = historyJobRepository.findById(resources.getId()).orElseGet(HistoryJob::new);
        ValidationUtil.isNull( historyJob.getId(),"HistoryJob","id",resources.getId());
        historyJob.copy(resources);
        historyJobRepository.save(historyJob);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            historyJobRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<HistoryJobDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (HistoryJobDto historyJob : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("调度时间", historyJob.getTriggerTime());
            map.put("回调名称", historyJob.getCallName());
            map.put("回调数据", historyJob.getCallData());
            map.put("回调方式", historyJob.getCallType());
            map.put("回调主机", historyJob.getCallHost());
            map.put("CRON", historyJob.getCron());
            map.put("创建时间", historyJob.getCreateTime());
            map.put("修改时间", historyJob.getModifyTime());
            map.put("应用名", historyJob.getApp());
            map.put("重试次数", historyJob.getCallerrorRetryCount());
            map.put("run重试", historyJob.getRunRetry());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}