<script setup>
import { useRouter } from 'vue-router'
import { useTokenStore } from '@/stores/token';
import { ElMessage } from 'element-plus';
import { userCancelApi } from '@/apis/user';
import { userLogoutApi } from '@/apis/user';
const router=useRouter();
const useToken=useTokenStore();

const confirm = async() => {
  await userLogoutApi();
  console.log('用户要退出登录了')
  useToken.removeToken();
  // 退出登录业务逻辑实现
  router.push('/login')
}

//获取登录用户信息
import { useUserStore } from '@/stores/userStore';
const userStore=useUserStore()

const cancel=async()=>{
  await userCancelApi();
  ElMessage("用户注销成功")
  useToken.removeToken();
  router.push('/login')
}
</script>

<template>
  <nav class="app-topnav">
    <div class="container">
      <ul>
        <!-- 多模版渲染 区分登录状态和非登录状态 -->

        <!-- 适配思路: 登录时显示第一块 非登录时显示第二块  是否有token -->
        <template v-if=useToken.token>
          <li><a href="javascript:;"><i class=" iconfont icon-user"></i>{{ userStore.userInfo.userInfo.username }}</a></li>
          <li>
            <el-popconfirm @confirm="confirm" title="确认退出吗?" confirm-button-text="确认" cancel-button-text="取消">
              <template #reference>
                <a href="javascript:;">退出登录</a>
              </template>
            </el-popconfirm>
          </li>
          <li><router-link :to="`/order`">我的订单</router-link></li>
          <li><router-link :to="`/cartlist`">我的购物车</router-link></li>
          <li>
            <el-popconfirm @confirm="cancel" title="确认注销账户吗?" confirm-button-text="确认" cancel-button-text="取消">
              <template #reference>
                <a href="javascript:;">用户注销</a>
              </template>
            </el-popconfirm>
          </li>
        </template>
        <template v-else>
          <li><a href="javascript:;" @click="$router.push('/login')">请先登录</a></li>
          <li><a href="javascript:;">帮助中心</a></li>
          <li><a href="javascript:;">关于我们</a></li>
        </template>
      </ul>
    </div>
  </nav>
</template>


<style scoped lang="scss">
.app-topnav {
  background: #333;

  ul {
    display: flex;
    height: 53px;
    justify-content: flex-end;
    align-items: center;

    li {
      a {
        padding: 0 15px;
        color: #cdcdcd;
        line-height: 1;
        display: inline-block;

        i {
          font-size: 14px;
          margin-right: 2px;
        }

        &:hover {
          color: $xtxColor;
        }
      }

      ~li {
        a {
          border-left: 2px solid #666;
        }
      }
    }
  }
}
</style>