<script setup>
import {
    Select,
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ref } from 'vue'
import { getLoginDataApi } from '@/apis/admin'

// 获取商品销售记录
const loginData = ref([
    {
        "name": "lilma",
        "loginTime": "2025-04-20 22:21:24",
        "logoutTime": "2025-05-03 16:58:53",
        "ip": "0:0:0:0:0:0:0:1"
    }
])

const getLogin = async () => {
    let result = await getLoginDataApi();
    loginData.value = result.data;
}
getLogin()


</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>登录信息记录</span>
            </div>
        </template>
        <el-table :data="loginData" style="width: 100%">
            <el-table-column label="用户名" prop="name"> </el-table-column>
            <el-table-column label="登陆时间" prop="loginTime"></el-table-column>
            <el-table-column label="退出时间" prop="logoutTime"></el-table-column>
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