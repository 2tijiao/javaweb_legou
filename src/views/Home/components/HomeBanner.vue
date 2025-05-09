<script setup>
import { getRandomGoods } from '@/apis/goods';
import { ref } from 'vue';

const randomGoodsList=ref([]);
const getrandomgoods=async()=>{
  let result=await getRandomGoods();
  randomGoodsList.value=result.data;
}
getrandomgoods()
</script>
<template>
  <div class="home-banner">
    <el-carousel height="500px">
      <el-carousel-item v-for="item in randomGoodsList" :key="item.id">
        <img :src="item.picture" alt="">
      </el-carousel-item>
    </el-carousel>
  </div>
</template>



<style scoped lang='scss'>
.home-banner {
  width: 1240px;
  height: 500px;
  //position: absolute;
  left: 0;
  top: 0;
  z-index: 98;

  img {
    width: 100%;
    height: 500px;
  }
}
</style>