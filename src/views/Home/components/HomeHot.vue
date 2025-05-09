<script setup>
import HomePanel from '@/views/Home/components/HomePanel.vue'
import {getHotGoods, getUserHotGoods } from '@/apis/goods'
import { onMounted, ref, computed } from 'vue'
import { useTokenStore } from '@/stores/token' // 引入token store

const tokenStore = useTokenStore()
const hotList = ref([])
const getHotList = async () => {
  try {
    let result
    // 判断token是否为空
    if (tokenStore.token === '') {
      // 无token，调用公共热门商品接口
      result = await getHotGoods()
    } else {
      // 有token，调用用户个性化热门商品接口
      result = await getUserHotGoods(tokenStore.token)
    }
    hotList.value = result.data || []
  } catch (error) {
    console.error('获取热门商品失败:', error)
    // 这里可以添加错误提示或降级处理
  }
}
// 组件挂载后立即执行
onMounted(() => {
  getHotList()
})
</script>
<template>
  <HomePanel title="人气推荐" sub-title="人气爆款 不容错过">
    <ul class="goods-list">
      <li v-for="item in hotList" :key="item.id">
        <RouterLink :to="`/detail/${item.id}`">
          <img :src="item.picture" alt="">
          <p class="name">{{ item.goodsname }}</p>
          <p class="desc">{{ item.desc }}</p>
        </RouterLink>
      </li>
    </ul>
  </HomePanel>
</template>

<style scoped lang='scss'>
/* 样式保持不变 */
.goods-list {
  display: flex;
  justify-content: space-between;
  height: 406px;

  li {
    width: 306px;
    height: 406px;

    background: #f0f9f4;
    transition: all .5s;

    &:hover {
      transform: translate3d(0, -3px, 0);
      box-shadow: 0 3px 8px rgb(0 0 0 / 20%);
    }

    img {
      width: 306px;
      height: 306px;
    }

    p {
      font-size: 22px;
      padding-top: 12px;
      text-align: center;
      text-overflow: ellipsis;
      overflow: hidden;
      white-space: nowrap;
    }

    .price {
      color: $priceColor;
    }
  }
}
</style>