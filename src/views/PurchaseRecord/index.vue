<script setup>
import {
    Select,
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ref } from 'vue'
import { getRecordApi } from '@/apis/saler'
import * as echarts from 'echarts'
import { getRatioApi } from '@/apis/saler'

//获取商品销售记录
const categorys = ref([
    {
            "userid": 4,
            "username": "lilma",
            "goodsid": 47,
            "goodsname": "iPhone 14充电器",
            "goodsprice": 149.00,
            "goodsnum": 1,
            "totalprice": 149.00,
        }
])

const getRecord=async()=>{
    let result=await getRecordApi();
    categorys.value=result.data;
}
getRecord();

//商品收益统计
const dialogVisible=ref(false)
const stacategorys=ref([
    {
        "goodsname": "拯救者",
        "goodsnum": 2,
        "goodssum": 15999.60,
        "ratio": 0.126974
    }
])
const stashow=async()=>{
    let result=await getRatioApi()
    stacategorys.value=result.data
}
stashow()
const formatRatio=(row, column, value)=>{
      return value.toFixed(2) * 100 + '%';
}
//商品数据可视化
const visualizationDialogVisible=ref(false)

const initChart = () => {
  const chart = echarts.init(document.getElementById('chart'));
  const option = {
    series: [
      {
        name: '销售占比',
        type: 'pie',
        radius: '50%',
        data: stacategorys.value.map(item => ({
          value: item.ratio.toFixed(2) * 100, // 假设ratio是小数，需要转换为百分比
          name: item.goodsname
        })),
        label: {
          show: true,
          position: 'outside', // 标签的位置
          formatter: '{b}: {c}%', // 标签的内容，{b}是数据名称，{c}是数据值
        }
      }
    ]
  };
  chart.setOption(option);
}

const showVisualizationDialog=()=>{
      visualizationDialogVisible.value = true;
      setTimeout(() => {
    initChart();
  }, 0);
}

</script>
<template>
    <el-card class="page-container">

        <template #header>
            <div class="header">
                <span>订单列表</span>
            </div>
        </template>

        <el-table :data="categorys" style="width: 100%">
            <el-table-column label="序号" width="100" type="index"> </el-table-column>
            <el-table-column label="用户名"   prop="username"> </el-table-column>
            <el-table-column label="商品名称" prop="goodsname"></el-table-column>
            <el-table-column label="商品价格" prop="goodsprice"></el-table-column>
            <el-table-column label="商品数量" prop="goodsnum"></el-table-column>
            <el-table-column label="商品收益" prop="totalprice"></el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>

        <el-button class="bt" @click="dialogVisible=true">销售统计报表</el-button>


        <!--数据展示弹窗-->
        <el-dialog v-model="dialogVisible" width="50%">
            <el-table :data="stacategorys" style="width: 100%">
                <el-table-column label="序号" width="100" type="index"> </el-table-column>
                <el-table-column label="商品名称" prop="goodsname"></el-table-column>
                <el-table-column label="商品数量" prop="goodsnum"></el-table-column>
                <el-table-column label="商品总销量" prop="goodssum"></el-table-column>
                <el-table-column label="销售占比" prop="ratio" :formatter="formatRatio"></el-table-column>
            </el-table>
            <el-button class="bt" @click="showVisualizationDialog">数据可视化</el-button>
        </el-dialog>

        <el-dialog v-model="visualizationDialogVisible" width="50%" title="数据可视化">
            <div id="chart" style="width: 100%; height: 400px;"></div>
        </el-dialog>

    </el-card>
</template>

<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .search {
        width: 50%;
        margin-right: 5%;

        .select {
            float: left;
            width: 20%;
        }

        .input {
            width: 50%;
        }
    }

    .search .button {
        width: 10%;
        float: right;
    }

    .tip {
        margin-left: 20px;
        font-size: 15px;
    }

    .bt {
        margin-top: 20px;
    }

    .el-table__header-wrapper th {
        flex: 1;
        min-width: 100px;
    }

    .el-table__body-wrapper td {
        flex: 1;
        min-width: 100px;
    }
}
</style>