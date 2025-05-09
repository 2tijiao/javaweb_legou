<script setup>
import {
    Select,
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ref } from 'vue'
import { getUserProfileApi } from '@/apis/admin'

// 获取商品销售记录
const profileData = ref([
    {
        "userid": 4,
        "location": "广州",
        "purchasePower": "HIGH",
        "categorys": [
            "居家",
            "美食",
            "数码"
        ]
    }
])

const getLabel = async () => {
    let result = await getUserProfileApi();
    profileData.value = result.data;
}

getLabel()

// 自定义 formatter 函数
const formatCategorys = (row, column, value) => {
    if (value && value.length > 0) {
        return value.join(', ');
    } else {
        return '该用户暂未浏览商品';
    }
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>用户画像信息</span>
            </div>
        </template>
        <el-table :data="profileData" style="width: 100%">
            <el-table-column label="用户id" prop="userid"> </el-table-column>
            <el-table-column label="用户所属地" prop="location"></el-table-column>
            <el-table-column label="用户购买力等级" prop="purchasePower"></el-table-column>
            <el-table-column label="用户种类偏好" prop="categorys" :formatter="formatCategorys"></el-table-column>
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