<script setup>
import {
    Select,
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ref } from 'vue'
import { getGoodsRatioApi,addCategoryApi,deleteCategory} from '@/apis/admin'
import * as echarts from 'echarts'


//商品收益统计
const catcategorys=ref([
    {
            "categoryid": 1,
            "categoryname": "居家",
            "categorynum": 0,
            "categorysum": 0.00,
            "ratio": 0.0
    }
])
const catshow=async()=>{
    let result=await getGoodsRatioApi()
    catcategorys.value=result.data
}
catshow()
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
        data: catcategorys.value.map(item => ({
          value: item.ratio.toFixed(2) * 100, // 假设ratio是小数，需要转换为百分比
          name: item.categoryname
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

// 对话框状态
const addCategoryDialogVisible = ref(false);
const categoryname=ref("");

const addCategoryDialog = () => {
  addCategoryDialogVisible.value = true
  // 重置表单
  categoryname.value="";
}

// 提交添加商品种类表单
const handleAddCategory = async () => {
    // 调用API添加商品种类
    let result=await addCategoryApi(categoryname.value)
    alert(result.data);
    // 关闭对话框
    addCategoryDialogVisible.value = false;
    catshow();
}

// 取消添加商品种类
const cancelAddCategory = () => {
  addCategoryDialogVisible.value = false
}

//删除商品种类
const categoryDelete= async (row)=>{
  let result=await deleteCategory(row.categoryid);
  alert(result.data)
  catshow()
}

</script>
<template>
    <el-card class="page-container">

        <template #header>
            <div class="header">
                <span>商品分类统计</span>
                <el-button @click="addCategoryDialog">添加商品种类</el-button>
            </div>
        </template>

        <el-table :data="catcategorys" style="width: 100%">
            <el-table-column label="序号" width="100" type="index"> </el-table-column>
            <el-table-column label="种类名称"   prop="categoryname"> </el-table-column>
            <el-table-column label="种类销量" prop="categorynum"></el-table-column>
            <el-table-column label="种类收益" prop="categorysum"></el-table-column>
            <el-table-column label="种类占比" prop="ratio" :formatter="formatRatio"></el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-popconfirm title="确认删除吗?" confirm-button-text="确认" cancel-button-text="取消" @confirm="categoryDelete(row)">
                    <template #reference>
                      <el-button :icon="Delete" circle plain type="danger"></el-button>
                    </template>
                  </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>
        <el-button class="bt" @click="showVisualizationDialog">数据可视化</el-button>

        <el-dialog v-model="visualizationDialogVisible" width="50%" title="数据可视化">
            <div id="chart" style="width: 100%; height: 400px;"></div>
        </el-dialog>


        <el-dialog 
      v-model="addCategoryDialogVisible" 
      title="添加商品种类" 
      :close-on-click-modal="false"
    >
          <el-form-item label="种类名称" prop="categoryname">
            <el-input 
              placeholder="请输入商品种类名称" v-model="categoryname"
            ></el-input>
          </el-form-item>
      <template #footer>
        <el-button @click="cancelAddCategory">返回</el-button>
        <el-button type="primary" @click="handleAddCategory">添加</el-button>
      </template>
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