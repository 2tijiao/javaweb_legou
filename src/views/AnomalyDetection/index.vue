<script setup>
import {
    Select,
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ref } from 'vue'
import { anomalyDetectionApi } from '@/apis/admin'
import { deleteOrderApi } from '@/apis/admin'

// 获取商品销售记录
const anomalData = ref([
    {
        "orderId": 57,
        "anomalyType": "UNIT_PRICE_EXCEEDED",
        "anomalyContent": "当前单价 9999999.99 元，阈值 99999.99 元"
    }
])

const getAnomal = async () => {
    let result = await anomalyDetectionApi();
    anomalData.value = result.data;
}
getAnomal()

const deleteOrder=async(row)=>{
    let result=await deleteOrderApi(row.orderId);
    alert(result.data);
    getAnomal()
}

</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>销售异常检测信息</span>
            </div>
        </template>
        <el-table :data="anomalData" style="width: 100%">
            <el-table-column label="订单id" prop="orderId"> </el-table-column>
            <el-table-column label="异常类型" prop="anomalyType"></el-table-column>
            <el-table-column label="异常内容" prop="anomalyContent"></el-table-column>
            <el-table-column label="操作" align="center">
                <template #default="{ row }">
                    <el-popconfirm title="确认删除吗?" confirm-button-text="确认" cancel-button-text="取消" @confirm="deleteOrder(row)">
                    <template #reference>
                      <el-button>删除订单</el-button>
                    </template>
                  </el-popconfirm>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
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