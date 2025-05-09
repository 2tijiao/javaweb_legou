<script setup>
import { browsingRecordApi, getGoodsDetailApi } from '@/apis/goods'
import { onMounted, ref } from 'vue'
import { useRoute,useRouter } from 'vue-router'
import { addCartlistApi } from '@/apis/goods'
import { useTokenStore } from '@/stores/token'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router=useRouter()
const goods = ref({})
const useToken=useTokenStore()
// 记录开始时间（以毫秒数表示）
const startTime = ref(null) 
// 记录结束时间（以毫秒数表示）
const endTime = ref(null) 

const getGoods = async () => {
  const res = await getGoodsDetailApi(route.params.id)
  goods.value = res.data
}
getGoods()

const count=ref(1);
const countChange=(count)=>{
  console.log(count);
}

const addCart=async()=>{
  if(!useToken.token){
    alert("加入购物车失败，请先登录");
    return;
  }
  // 判断库存
  if(count.value > goods.value.amount){
    alert("库存不足，无法加入购物车");
    count.value=1
    return;
  }
  try {
    let result=await addCartlistApi(route.params.id,count.value);
    alert(result.msg);
    await getGoods();
    count.value=1;
  } catch (error) {
    alert("加入购物车失败，请重试");
    console.error(error);
  }
}

// 新增返回方法
// 新增返回方法
const goBack = async () => {
  endTime.value = new Date().getTime()
  const timeDiff = endTime.value - startTime.value
  console.log('商品 ID:', route.params.id);
  
  // 检查 token 是否存在
  if (!useToken.token) {
    router.go(-1) // 未登录仍允许返回
    return
  }
  
  // 调用后端接口传递时间差
  try {
    await browsingRecordApi(route.params.id, timeDiff);
    router.go(-1);
  } catch (error) {
    alert("传递时间失败，请重试")
    console.error(error)
  }
}

onMounted(() => {
  startTime.value = new Date().getTime()
})
</script>

<template>
  <div class="xtx-goods-page">
    <div class="container" v-if="goods.id">
      <!-- 商品信息 -->
      <div class="info-container">
        <div>
          <div class="goods-info">
            <div class="media">
              <!-- 图片预览区 -->
              <img :src="goods.picture" alt="" />
              <!-- 统计数量 -->
            </div>
            <div class="spec">
              <!-- 商品信息区 -->
              <p class="g-name"> {{ goods.goodsname }} </p>
              <p class="g-desc">{{ goods.desc }} </p>
              <p class="g-price">
                <span>{{ goods.price }}</span>
                <span> {{ goods.price }}</span>
              </p>
              <p class="g-amount">
                商品库存数量：{{ goods.amount }}
              </p>
              <div class="g-service">
                <dl>
                  <dt>促销</dt>
                  <dd>12月好物放送，App领券购买直降120元</dd>
                </dl>
                <dl>
                  <dt>服务</dt>
                  <dd>
                    <span>无忧退货</span>
                    <span>快速退款</span>
                    <span>免费包邮</span>
                  </dd>
                </dl>
              </div>
              <div class="count">
                <el-input-number v-model="count" @change="countChange" min="0"/>
              </div>
              <div>
                <el-button size="large" class="btn" @click="addCart">
                  加入购物车
                </el-button> 
              </div>
              <el-button size="large" @click="goBack" class="back">返回</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<style scoped lang='scss'>
.back{
  margin-top: 20px;
}
.count{
  margin-top: 20px;
}
.xtx-goods-page {
  .goods-info {
    min-height: 600px;
    background: #fff;
    display: flex;

    .media {
      width: 580px;
      height: 600px;
      padding: 30px 50px;
    }

    .spec {
      flex: 1;
      padding: 30px 30px 30px 0;
    }
  }

  .goods-footer {
    display: flex;
    margin-top: 20px;

    .goods-article {
      width: 940px;
      margin-right: 20px;
    }

    .goods-aside {
      width: 280px;
      min-height: 1000px;
    }
  }

  .goods-tabs {
    min-height: 600px;
    background: #fff;
  }

  .goods-warn {
    min-height: 600px;
    background: #fff;
    margin-top: 20px;
  }

  .number-box {
    display: flex;
    align-items: center;

    .label {
      width: 60px;
      color: #999;
      padding-left: 10px;
    }
  }

  .g-name {
    font-size: 22px;
  }

  .g-desc {
    color: #999;
    margin-top: 10px;
  }
  .g-amount {
      margin: 10px 0; // 上下间距
      font-size: 16px;
      color: #999; // 字体颜色
    }

  .g-price {
    margin-top: 10px;

    span {
      &::before {
        content: "¥";
        font-size: 14px;
      }

      &:first-child {
        color: $priceColor;
        margin-right: 10px;
        font-size: 22px;
      }

      &:last-child {
        color: #999;
        text-decoration: line-through;
        font-size: 16px;
      }
    }
  }

  .g-service {
    background: #f5f5f5;
    width: 500px;
    padding: 20px 10px 0 10px;
    margin-top: 10px;

    dl {
      padding-bottom: 20px;
      display: flex;
      align-items: center;

      dt {
        width: 50px;
        color: #999;
      }

      dd {
        color: #666;

        &:last-child {
          span {
            margin-right: 10px;

            &::before {
              content: "•";
              color: $xtxColor;
              margin-right: 2px;
            }
          }

          a {
            color: $xtxColor;
          }
        }
      }
    }
  }

  .goods-sales {
    display: flex;
    width: 400px;
    align-items: center;
    text-align: center;
    height: 140px;

    li {
      flex: 1;
      position: relative;

      ~li::after {
        position: absolute;
        top: 10px;
        left: 0;
        height: 60px;
        border-left: 1px solid #e4e4e4;
        content: "";
      }

      p {
        &:first-child {
          color: #999;
        }

        &:nth-child(2) {
          color: $priceColor;
          margin-top: 10px;
        }

        &:last-child {
          color: #666;
          margin-top: 10px;

          i {
            color: $xtxColor;
            font-size: 14px;
            margin-right: 2px;
          }

          &:hover {
            color: $xtxColor;
            cursor: pointer;
          }
        }
      }
    }
  }
}

.goods-tabs {
  min-height: 600px;
  background: #fff;

  nav {
    height: 70px;
    line-height: 70px;
    display: flex;
    border-bottom: 1px solid #f5f5f5;

    a {
      padding: 0 40px;
      font-size: 18px;
      position: relative;

      >span {
        color: $priceColor;
        font-size: 16px;
        margin-left: 10px;
      }
    }
  }
}

.goods-detail {
  padding: 40px;

  .attrs {
    display: flex;
    flex-wrap: wrap;
    margin-bottom: 30px;

    li {
      display: flex;
      margin-bottom: 10px;
      width: 50%;

      .dt {
        width: 100px;
        color: #999;
      }

      .dd {
        flex: 1;
        color: #666;
      }
    }
  }

  >img {
    width: 100%;
  }
}

.btn {
  margin-top: 20px;

}

.bread-container {
  padding: 25px 0;
}
</style>