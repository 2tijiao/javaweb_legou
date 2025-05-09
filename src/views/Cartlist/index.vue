<script setup>
import { getCartlistApi } from '@/apis/cartlist';
import { ref, computed } from 'vue';
import { delCartlistApi } from '@/apis/cartlist';
import { submitcartApi } from '@/apis/cartlist';
const cartlist = ref([]);
// 全选状态
const allChecked = ref(false);

// 计算总商品数量
const allCount = computed(() => cartlist.value.length);
// 计算选中商品数量
const selectedCount = computed(() => 
  cartlist.value.filter(item => item.selected).length
);
// 计算选中商品总价
const selectedTotalPrice = computed(() => 
  cartlist.value
    .filter(item => item.selected)
    .reduce((sum, item) => sum + item.goodsprice * item.goodsnum, 0)
);

// 全选逻辑
const handleAllCheck = (val) => {
  cartlist.value.forEach(item => {
    item.selected = val;
  });
  allChecked.value = val;
};

const getCart = async () => {
  let result = await getCartlistApi();
  cartlist.value = result.data;
  // 初始化 totalprice 属性
  cartlist.value.forEach(item => {
    item.totalprice = item.goodsprice * item.goodsnum;
  });
};
getCart();

const delCart=async(id)=>{
  await delCartlistApi(id);
  await getCart();
}

const handleSubmit=async()=>{
  const selectedGoods=cartlist.value.filter(item=>item.selected)
  if(!selectedGoods.length){
    alert("您并没有选择商品")
  }
  else{
    try{
      let result=await submitcartApi(selectedGoods);
      alert(result.msg);
      await getCart();
    }catch(error){
      console.error('下单失败:',error)
    }
  }
}

// 商品数量变化时更新 totalprice
const handleQuantityChange = (newValue, item) => {
  item.goodsnum = newValue;
  item.totalprice = item.goodsprice * item.goodsnum;
};
</script>

<template>
  <div class="xtx-cart-page">
    <div class="container m-top-20">
      <div class="cart">
        <table>
          <thead>
            <tr>
              <th width="120">
                <!-- 绑定全选状态和事件 -->
                <el-checkbox v-model="allChecked" @change="handleAllCheck" />
              </th>
              <th width="400">商品信息</th>
              <th width="220">单价</th>
              <th width="180">数量</th>
              <th width="180">小计</th>
              <th width="140">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="i in cartlist" :key="i.id">
              <td>
                <!-- 绑定商品选中状态 -->
                <el-checkbox v-model="i.selected" />
              </td>
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
                <el-input-number v-model="i.goodsnum" @change="(val) => handleQuantityChange(val, i)" />
              </td>
              <td class="tc">
                <p class="f16 red">&yen;{{ i.totalprice.toFixed(2) }}</p>
              </td>
              <td class="tc">
                <p>
                  <el-popconfirm title="确认删除吗?" confirm-button-text="确认" cancel-button-text="取消" @confirm="delCart(i.id)">
                    <template #reference>
                      <a href="javascript:;">删除</a>
                    </template>
                  </el-popconfirm>
                </p>
              </td>
            </tr>
            <tr v-if="cartlist.length === 0">
              <td colspan="6">
                <div class="cart-none">
                  <el-empty description="购物车列表为空">
                    <el-button type="primary" @click="$router.push('/')">随便逛逛</el-button>
                  </el-empty>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="action">
        <div class="batch">
          共{{ allCount }}件商品，已选择 {{ selectedCount }} 件，商品合计：
          <span class="red">¥ {{ selectedTotalPrice.toFixed(2) }}</span>
        </div>
        <div class="total">
          <el-button size="large" type="primary" @click="handleSubmit">下单结算</el-button>
        </div>
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