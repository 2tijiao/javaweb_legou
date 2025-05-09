<script setup>
import { ref } from 'vue'
import { getStatusApi, getTrendApi } from '@/apis/saler'

// 获取商品销售记录
const statusData = ref([
    {
        "goodsid": 9,
        "goodsname": "",
        "goodspicture": "",
        "amount": 47,
        "goodsprice": 5999.9,
        "goodsnum": 8,
        "totalprice": 47999.20,
        "isSellWell": 1,
    }
])

const getStatus = async () => {
    let result = await getStatusApi();
    statusData.value = result.data;
    // 为每一行数据添加一个初始的销售趋势值（空字符串）
    statusData.value.forEach(row => {
        row.trend = '';
    });

    // 遍历每一行数据，异步获取销售趋势并更新
    for (const row of statusData.value) {
        const trend = await getTrend(row.goodsid);
        row.trend = trend;
    }
}

getStatus()

// 定义一个函数来获取并返回趋势值
const getTrend = async (goodsid) => {
    const result = await getTrendApi(goodsid);
    return result.data;
}

</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>商品销售统计报表(按商品总收益排序)</span>
            </div>
        </template>

        <el-table :data="statusData" style="width: 100%">
            <el-table-column label="序号" width="100" type="index"> </el-table-column>
            <el-table-column label="商品名称" prop="goodsname"></el-table-column>
            <el-table-column label="商品图片">
                <template #default="{ row }">
                    <img :src="row.goodspicture" alt="" v-if="row.goodspicture" />
                </template>
            </el-table-column>
            <el-table-column label="商品价格" prop="goodsprice"></el-table-column>
            <el-table-column label="商品数量" prop="goodsnum"></el-table-column>
            <el-table-column label="商品收益" prop="totalprice"></el-table-column>
            <el-table-column label="商品库存" prop="amount"></el-table-column>
            <el-table-column label="销售状态(按总销售量二分之一评判)">
                <template #default="{ row }">
                    {{ row.isSellWell === 1? '畅销' : '滞销' }}
                </template>
            </el-table-column>
            <el-table-column label="销售趋势" prop="trend"></el-table-column>
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