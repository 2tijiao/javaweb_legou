<script setup>
import { getCategoryApi } from '@/apis/goods';
import { ref } from 'vue';
import { useGoodsStore } from '@/stores/goodsStore';
import { useRouter } from 'vue-router';
import { useTokenStore } from '@/stores/token';
//种类列表
const categoryList=ref([])
const getCategory=async()=>{
    let result=await getCategoryApi();
    categoryList.value=result.data;
}
getCategory();

const router=useRouter()
const goodsname=ref("")
const goodsStore=useGoodsStore()
const search=async()=>{
  if(!goodsname.value){alert("请输入要搜索的商品名称")}
  else{
    await goodsStore.searchGoods(goodsname.value)
    router.push('/search')
    goodsname.value=""
  }
}


</script>

<template>
  <header class='app-header'>
    <div class="container">
      <h1 class="logo">
        <RouterLink to="/">乐购</RouterLink>
      </h1>
      <ul class="app-header-nav">
        <li>
          <router-link :to="`/`">首页</router-link>
        </li>
        <li class="home" v-for="item in categoryList" :key="item.id">
            <router-link :to="`/category/${item.id}`">{{ item.categoryname }}</router-link>
        </li>
      </ul>
      <div class="search">
        <i class="iconfont icon-search"></i>
        <input type="text" placeholder="搜一搜" v-model="goodsname">
        <el-button @click="search" class="search-btn">搜索商品</el-button>
      </div>
      <!-- 头部购物车 -->
    </div>
</header>
</template>


<style scoped lang='scss'>
.app-header-nav {
  width: 820px;
  display: flex;
  padding-left: 50px;
  position: relative;
  z-index: 998;

  li {
    margin-right: 60px;
    width: 38px;
    text-align: center;

    a {
      font-size: 16px;
      line-height: 32px;
      height: 32px;
      display: inline-block;

      &:hover {
        color: $xtxColor;
        border-bottom: 1px solid $xtxColor;
      }
    }

    .active {
      color: $xtxColor;
      border-bottom: 1px solid $xtxColor;
    }
  }
}
.app-header {
  background: #fff;

  .container {
    display: flex;
    align-items: center;
  }

  .logo {
    width: 200px;

    a {
      display: block;
      height: 132px;
      width: 100%;
      text-indent: -9999px;
      background: url('@/assets/images/logo.png') no-repeat center 18px / contain;
    }
  }


  .search {
  display: flex;
  align-items: center;
  gap: 8px;
  border-bottom: 1px solid #ababab;
  padding-bottom: 5px;
}

.search input {
  flex: 1;
  height: 32px;
  padding: 0 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.search-btn {
  height: 32px;
  padding: 0 15px;
  font-size: 14px;
  background-color: $xtxColor;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}


  .cart {
    width: 50px;

    .curr {
      height: 32px;
      line-height: 32px;
      text-align: center;
      position: relative;
      display: block;

      .icon-cart {
        font-size: 22px;
      }

      em {
        font-style: normal;
        position: absolute;
        right: 0;
        top: 0;
        padding: 1px 6px;
        line-height: 1;
        background: $helpColor;
        color: #fff;
        font-size: 12px;
        border-radius: 10px;
        font-family: Arial;
      }
    }
  }
}
</style>