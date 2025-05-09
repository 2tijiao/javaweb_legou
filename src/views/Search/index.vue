<script setup>
import HomePanel from '@/views/Home/components/HomePanel.vue'
import { useGoodsStore } from '@/stores/goodsStore';
import { onMounted, ref } from 'vue'

const goodsStore = useGoodsStore()

</script>

<template>
  <HomePanel title="搜索结果">
    <ul class="goods-list">
      <li v-for="item in goodsStore.goodsList" :key="item.id">
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
.goods-list {
  display: flex;
  justify-content: flex-start; 
  flex-wrap: wrap; 
  gap: 20px; 
  height: auto; 

  li {
    width: calc((100% - 80px) / 5); 
    height: auto; /* 商品项高度自适应 */
    transition: all .5s;

    &:hover {
      transform: translate3d(0, -3px, 0);
      box-shadow: 0 3px 8px rgb(0 0 0 / 20%);
    }

    img {
      width: 100%; 
      height: 306px;
    }

    p {
      padding: 8px 12px; /* 调整内边距让文字更舒适 */
      text-align: left; /* 文字左对齐更符合阅读习惯 */
      word-wrap: break-word; /* 允许文字自动换行 */
    }

    .name {
      font-size: 18px; /* 缩小标题字体大小 */
      min-height: 40px; /* 保证标题显示空间 */
      font-weight: 500;
    }

    .desc {
      color: #999;
      font-size: 16px; /* 缩小描述字体大小 */
      line-height: 1.5; /* 增加行高提升可读性 */
    }
  }
}
</style>