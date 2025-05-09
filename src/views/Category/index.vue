<script setup>
import { useCategory } from './components/useCategory';
import GoodsItem from './components/GoodsItem.vue';
import { getTitle } from '@/views/Category/components/title'; // 匹配导出的函数

const { categoryGoods } = useCategory();
const { categorytitle } = getTitle(); // 调用函数获取响应式数据
</script>

<template>
  <div class="top-category">
    <div class="ref-goods">
      <h3>{{ categorytitle }}</h3> <!-- 显示分类名称 -->
      <div class="body">
        <GoodsItem v-for="good in categoryGoods" :goods="good" :key="good.id" />
      </div>
    </div>
  </div>
</template>


<style scoped lang="scss">
.top-category {
  h3 {
    font-size: 28px;
    color: #666;
    font-weight: normal;
    text-align: center;
    line-height: 100px;
  }

  .sub-list {
    margin-top: 20px;
    background-color: #fff;

    ul {
      display: flex;
      padding: 0 32px;
      flex-wrap: wrap;

      li {
        width: 168px;
        height: 160px;


        a {
          text-align: center;
          display: block;
          font-size: 16px;

          img {
            width: 100px;
            height: 100px;
          }

          p {
            line-height: 40px;
          }

          &:hover {
            color: $xtxColor;
          }
        }
      }
    }
  }

  .ref-goods {
    background-color: #fff;
    margin-top: 20px;
    position: relative;

    .head {
      .xtx-more {
        position: absolute;
        top: 20px;
        right: 20px;
      }

      .tag {
        text-align: center;
        color: #999;
        font-size: 20px;
        position: relative;
        top: -20px;
      }
    }

    .body {
      display: flex;
      flex-wrap: wrap; /* 允许换行 */
      justify-content: flex-start; /* 从左到右排列 */
      padding: 0 40px 30px;
    }

    .body .goods-item {
      width: calc(20% - 20px); /* 每行显示 5 个商品，减去间距 */
      margin: 10px; /* 添加间距 */
    }
  }

  .bread-container {
    padding: 25px 0;
  }
}
</style>