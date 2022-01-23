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
package me.zhengjie.modules.application.service.impl;

import me.zhengjie.modules.application.domain.Application;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.application.repository.ApplicationRepository;
import me.zhengjie.modules.application.service.ApplicationService;
import me.zhengjie.modules.application.service.dto.ApplicationDto;
import me.zhengjie.modules.application.service.dto.ApplicationQueryCriteria;
import me.zhengjie.modules.application.service.mapstruct.ApplicationMapper;
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
* @date 2022-01-23
**/
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;

    @Override
    public Map<String,Object> queryAll(ApplicationQueryCriteria criteria, Pageable pageable){
        Page<Application> page = applicationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(applicationMapper::toDto));
    }

    @Override
    public List<ApplicationDto> queryAll(ApplicationQueryCriteria criteria){
        return applicationMapper.toDto(applicationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ApplicationDto findById(Long id) {
        Application application = applicationRepository.findById(id).orElseGet(Application::new);
        ValidationUtil.isNull(application.getId(),"Application","id",id);
        return applicationMapper.toDto(application);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApplicationDto create(Application resources) {
        return applicationMapper.toDto(applicationRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Application resources) {
        Application application = applicationRepository.findById(resources.getId()).orElseGet(Application::new);
        ValidationUtil.isNull( application.getId(),"Application","id",resources.getId());
        application.copy(resources);
        applicationRepository.save(application);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            applicationRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ApplicationDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ApplicationDto application : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("应用名称", application.getName());
            map.put("创建时间", application.getCreateTime());
            map.put("修改时间", application.getModifyTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}