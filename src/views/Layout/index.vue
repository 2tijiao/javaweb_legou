<script setup>

import LayoutNav from '@/views/Layout/component/LayoutNav.vue'
import LayoutHeader from '@/views/Layout/component/LayoutHeader.vue'
import LayoutFooter from '@/views/Layout/component/LayoutFooter.vue'

</script>

<template>
  <LayoutNav />
  <LayoutHeader />
  <!-- 添加key 破坏复用机制 强制销毁重建 -->
  <!-- <RouterView :key="$route.fullPath" /> -->
  <router-view></router-view>
  <LayoutFooter />
</template>