<script setup>
import {
    Select,
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ref } from 'vue'
import { getOperateDataApi } from '@/apis/admin'

// 获取商品销售记录
const operateData = ref([
    {
        "name": "saler4",
        "operationTime": "2025-04-23 22:15:23",
        "operationContent": "saler4添加商品：aa",
        "ip": "0:0:0:0:0:0:0:1"
    }
])

const getOperate = async () => {
    let result = await getOperateDataApi();
    operateData.value = result.data;
}
getOperate()


</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>操作信息记录</span>
            </div>
        </template>
        <el-table :data="operateData" style="width: 100%">
            <el-table-column label="用户名" prop="name"> </el-table-column>
            <el-table-column label="操作时间" prop="operationTime"></el-table-column>
            <el-table-column label="操作内容" prop="operationContent"></el-table-column>
            <el-table-column label="ip地址" prop="ip"></el-table-column>
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