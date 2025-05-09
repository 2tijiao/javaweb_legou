<script setup>
import { ref, computed } from 'vue';
import { getOrderApi } from '@/apis/order';
import { changeStateApi } from '@/apis/order';
import { deleteApi } from '@/apis/order';

const orderlist = ref([]);
const getOrder = async () => {
    let result = await getOrderApi();
    orderlist.value = result.data;
};
getOrder();

const change=async(id)=>{
    await changeStateApi(id);
    getOrder();
}

const del=async(id)=>{
    await deleteApi(id);
    getOrder();
}
</script>

<template>
    <div class="xtx-cart-page">
        <div class="container m-top-20">
            <div class="cart">
                <table>
                    <thead>
                        <tr>
                            <th width="400">商品名称</th>
                            <th width="220">单价</th>
                            <th width="180">数量</th>
                            <th width="180">小计</th>
                            <th width="180">商品状态</th>
                            <th width="140">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="i in orderlist" :key="i.id">
                            <td>
                                <div class="goods">
                                    <RouterLink :to="`/detail/${i.goodsid}`"><img :src="i.goodspicture" alt="" /></RouterLink>
                                    <div>
                                        <p class="name ellipsis">{{ i.goodsname }}</p>
                                    </div>
                                </div>
                            </td>
                            <td class="tc">
                                <p>&yen;{{ i.goodsprice }}</p>
                            </td>
                            <td class="tc">
                                <p>{{ i.goodsnum }}</p>
                            </td>
                            <td class="tc">
                                <p class="f16 red">&yen;{{ i.totalprice.toFixed(2) }}</p>
                            </td>
                            <td class="tc">
                                <p>{{ i.state === 0 ? '未收货' : '已收货' }}</p>
                            </td>
                            <td class="tc">
                                <div style="display: flex; justify-content: center; gap: 10px;">
                                    <el-button v-if="i.state === 0" @click="change(i.id)">已收货</el-button>
                                    <el-button @click="del(i.id)">删除</el-button>
                                </div>
                            </td>
                        </tr>
                        <tr v-if="orderlist.length === 0">
                            <td colspan="6">
                                <div class="cart-none">
                                    <el-empty description="订单列表为空">
                                        <el-button type="primary" @click="$router.push('/')">随便逛逛</el-button>
                                    </el-empty>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<style scoped lang="scss">
/* 原有样式代码保持不变 */
.xtx-cart-page {
    margin-top: 20px;

    .cart {
        background: #fff;
        color: #666;

        table {
            border-spacing: 0;
            border-collapse: collapse;
            line-height: 24px;

            th,
            td {
                padding: 10px;
                border-bottom: 1px solid #f5f5f5;

                &:first-child {
                    text-align: left;
                    padding-left: 30px;
                    color: #999;
                }
            }

            th {
                font-size: 16px;
                font-weight: normal;
                line-height: 50px;
            }
        }
    }

    .cart-none {
        text-align: center;
        padding: 120px 0;
        background: #fff;

        p {
            color: #999;
            padding: 20px 0;
        }
    }

    .tc {
        text-align: center;

        a {
            color: $xtxColor;
        }

        .xtx-numbox {
            margin: 0 auto;
            width: 120px;
        }
    }

    .red {
        color: $priceColor;
    }

    .green {
        color: $xtxColor;
    }

    .f16 {
        font-size: 16px;
    }

    .goods {
        display: flex;
        align-items: center;

        img {
            width: 100px;
            height: 100px;
        }

        >div {
            width: 280px;
            font-size: 16px;
            padding-left: 10px;

            .attr {
                font-size: 14px;
                color: #999;
            }
        }
    }

    .action {
        display: flex;
        background: #fff;
        margin-top: 20px;
        height: 80px;
        align-items: center;
        font-size: 16px;
        justify-content: space-between;
        padding: 0 30px;

        .xtx-checkbox {
            color: #999;
        }

        .batch {
            a {
                margin-left: 20px;
            }
        }

        .red {
            font-size: 18px;
            margin-right: 20px;
            font-weight: bold;
        }
    }

    .tit {
        color: #666;
        font-size: 16px;
        font-weight: normal;
        line-height: 50px;
    }
}
</style>    