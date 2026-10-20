<template>
  <div class="front-container">
    <div style="font-size: 20px; font-weight: bold; margin-bottom: 20px">我收藏的商品（{{ data.total }}）</div>

    <!-- 搜索栏 -->
    <div style="margin-bottom: 20px">
      <el-input @keyup.enter="load" clearable @clear="load" prefix-icon="Search"
                style="height: 40px; width: 300px" v-model="data.name"
                placeholder="请输入商品名称搜索"></el-input>
      <el-button style="height: 40px; margin-left: 10px" type="primary" @click="load">查 询</el-button>
    </div>

    <!-- 有数据时显示商品列表 -->
    <el-row :gutter="20" v-if="data.total > 0">
      <el-col :span="6" v-for="item in data.tableData" :key="item.id">
        <div @click="router.push('/front/goodsDetail?id=' + item.goodsId)" class="card"
             style="cursor: pointer; width: 100%; padding: 0; border-radius: 5px; margin-bottom: 20px">
          <img :src="item.goodsImg" alt="" style="width: 100%; height: 260px; border-radius: 5px 5px 0 0">
          <div style="padding: 5px">
            <div class="line1" style="font-size: 18px; margin-bottom: 10px">{{ item.goodsName }}</div>
            <div style="display: flex; align-items: center">
              <div style="flex: 1; color: red">￥<b style="font-size: 20px">{{ item.goodsPrice }}</b></div>
              <el-button type="danger" @click.stop="cancel(item.id)">取消收藏</el-button>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 无数据时显示空状态 -->
    <div v-else style="text-align: center; padding: 80px 0; background: #fff; border-radius: 10px;">
      <div style="font-size: 18px; color: #999; margin-top: 20px;">暂无收藏商品</div>
      <div style="font-size: 14px; color: #ccc; margin-top: 10px;">快去逛逛，添加心仪的商品吧～</div>
    </div>

    <div v-if="data.total > 0">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" v-model:page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request";
import router from "@/router";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  pageNum: 1,
  pageSize: 10,
  total: 0,
  tableData: [],
  name: null,
})

// 分页查询
const load = () => {
  request.get('/collect/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      userId: data.user.id,
      goodsName: data.name
    }
  }).then(res => {
    data.tableData = res.data?.list
    data.total = res.data?.total
  })
}
load()

// 取消收藏
const cancel = (collectId) => {
  request.delete('/collect/delete/' + collectId).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>