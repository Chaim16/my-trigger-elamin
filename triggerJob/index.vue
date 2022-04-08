<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <label class="el-form-item-label">任务ID</label>
        <el-input v-model="query.id" clearable placeholder="任务ID" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">状态</label>
        <el-input v-model="query.status" clearable placeholder="状态" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">是否删除</label>
        <el-input v-model="query.remove" clearable placeholder="是否删除" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">回调名称</label>
        <el-input v-model="query.callName" clearable placeholder="回调名称" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">回调数据</label>
        <el-input v-model="query.callData" clearable placeholder="回调数据" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">回调类型</label>
        <el-input v-model="query.callType" clearable placeholder="回调类型" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">回调主机</label>
        <el-input v-model="query.callHost" clearable placeholder="回调主机" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">CRON</label>
        <el-input v-model="query.cron" clearable placeholder="CRON" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">应用id</label>
        <el-input v-model="query.app" clearable placeholder="应用id" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="回调名称" prop="callName">
            <el-input v-model="form.callName" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="回调数据">
            <el-input v-model="form.callData" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="回调类型">
            <el-radio v-model="form.callType" v-for="item in dict.call_type" :key="item.id" :label="item.value">{{ item.label }}</el-radio>
          </el-form-item>
          <el-form-item label="回调主机">
            <el-input v-model="form.callHost" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="CRON" prop="cron">
            <el-input v-model="form.cron" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="应用id" prop="app">
            <el-input v-model="form.app" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="任务ID" />
        <el-table-column prop="status" label="状态">
          <template slot-scope="scope">
            {{ dict.label.trigger_job_status[scope.row.status] }}
          </template>
        </el-table-column>
        <el-table-column prop="triggerTime" label="调度时间" />
        <el-table-column prop="remove" label="是否删除">
          <template slot-scope="scope">
            {{ dict.label.trigger_job_remove[scope.row.remove] }}
          </template>
        </el-table-column>
        <el-table-column prop="callName" label="回调名称" />
        <el-table-column prop="callData" label="回调数据" />
        <el-table-column prop="callType" label="回调类型">
          <template slot-scope="scope">
            {{ dict.label.call_type[scope.row.callType] }}
          </template>
        </el-table-column>
        <el-table-column prop="callHost" label="回调主机" />
        <el-table-column prop="cron" label="CRON" />
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column prop="modifyTime" label="修改时间" />
        <el-table-column prop="app" label="应用id" />
        <el-table-column prop="callerrorRetryCount" label="重试次数" />
        <el-table-column prop="runRetry" label="未ack重试次数" />
        <el-table-column v-if="checkPer(['admin','triggerJob:edit','triggerJob:del'])" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudTriggerJob from '@/api/triggerJob'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { id: null, status: null, triggerTime: null, remove: null, callName: null, callData: null, callType: null, callHost: null, cron: null, createTime: null, modifyTime: null, app: null, callerrorRetryCount: null, runRetry: null }
export default {
  name: 'TriggerJob',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  dicts: ['trigger_job_status', 'trigger_job_remove', 'call_type'],
  cruds() {
    return CRUD({ title: 'api/triggerJob', url: 'api/triggerJob', idField: 'id', sort: 'id,desc', crudMethod: { ...crudTriggerJob }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'triggerJob:add'],
        edit: ['admin', 'triggerJob:edit'],
        del: ['admin', 'triggerJob:del']
      },
      rules: {
        callName: [
          { required: true, message: '回调名称不能为空', trigger: 'blur' }
        ],
        cron: [
          { required: true, message: 'CRON不能为空', trigger: 'blur' }
        ],
        app: [
          { required: true, message: '应用id不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'id', display_name: '任务ID' },
        { key: 'status', display_name: '状态' },
        { key: 'remove', display_name: '是否删除' },
        { key: 'callName', display_name: '回调名称' },
        { key: 'callData', display_name: '回调数据' },
        { key: 'callType', display_name: '回调类型' },
        { key: 'callHost', display_name: '回调主机' },
        { key: 'cron', display_name: 'CRON' },
        { key: 'app', display_name: '应用id' }
      ]
    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }
  }
}
</script>

<style scoped>

</style>
