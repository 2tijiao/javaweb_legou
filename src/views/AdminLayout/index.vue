<script setup>
import {
    Management,
    Promotion,
    UserFilled,
    User,
    Crop,
    EditPen,
    SwitchButton,
    CaretBottom
} from '@element-plus/icons-vue'
import { useRouter } from 'vue-router';
import { useTokenStore } from '@/stores/token';
import { useAdminStore } from '@/stores/adminStore';
import { adminLogoutApi } from '@/apis/admin';

const router = useRouter();
const token=useTokenStore();
const adminStore=useAdminStore();
const handleLogout=async()=>{
    await adminLogoutApi();
    token.removeToken();
    router.push("/login")
};

</script>

<template>
    <!--element-plus中的容器-->
    <el-container class="layout-container">
        <!-- 左侧菜单 -->
        <el-aside width="200px">
            <div class="el-aside__logo"></div>
            <!--菜单标签-->
            <el-menu active-text-color="#ffd04b" background-color="#232323"  text-color="#fff"
                router>
                
                <el-menu-item index="/adminlayout">
                    <el-icon>
                        <Management />
                    </el-icon>
                    <span>销售人员管理</span>
                </el-menu-item>
                <el-menu-item index="/report">
                    <el-icon>
                        <Management />
                    </el-icon>
                    <span>商品销售报表</span>
                </el-menu-item>
                <el-menu-item index="/goods">
                    <el-icon>
                        <Promotion />
                    </el-icon>
                    <span>商品类别统计报表</span>
                </el-menu-item>
                <el-menu-item index="/userprofile">
                    <el-icon>
                        <Management />
                    </el-icon>
                    <span>用户画像</span>
                </el-menu-item>
                <el-menu-item index="/logindata">
                    <el-icon>
                        <Management />
                    </el-icon>
                    <span>登录数据</span>
                </el-menu-item>
                <el-menu-item index="/operatedata">
                    <el-icon>
                        <Management />
                    </el-icon>
                    <span>操作数据</span>
                </el-menu-item>
                <el-menu-item index="/anomal">
                    <el-icon>
                        <Management />
                    </el-icon>
                    <span>异常检测</span>
                </el-menu-item>
                <el-menu-item @click="handleLogout">
                    <el-icon>
                        <Crop />
                    </el-icon>
                    <span>退出登录</span>
                </el-menu-item>
            </el-menu>
        </el-aside>
        <!-- 右侧主区域 -->
        <el-container>
            <!-- 头部区域 -->
            <el-header>
                <div>欢迎回来: <strong>{{adminStore.adminInfo.adminInfo.adminname}}</strong></div>
            </el-header>
            <!-- 中间区域 -->
            <el-main>
                <router-view></router-view>
            </el-main>
            <!-- 底部区域 -->
            <el-footer>乐购 ©2024 Created by 马镜瑶</el-footer>
        </el-container>
    </el-container>
</template>

<style lang="scss" scoped>
.layout-container {
    height: 100vh;

    .el-aside {
        background-color: #232323;

        &__logo {
            height: 120px;
            background: url('@/assets/images/logo.png') no-repeat center / 120px auto;
        }

        .el-menu {
            border-right: none;
            margin-top: 30px; // 增大菜单与 logo 的距离

            .el-menu-item {
                margin-bottom: 20px; // 加大菜单标签之间的间距
            }
        }
    }

    .el-header {
        background-color: #fff;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .el-dropdown__box {
            display: flex;
            align-items: center;

            .el-icon {
                color: #999;
                margin-left: 10px;
            }

            &:active,
            &:focus {
                outline: none;
            }
        }
    }

    .el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #666;
    }
}
</style>    