//第一个是创建router实例对象
//第二个是创建history模式的路由
import { createRouter, createWebHistory } from 'vue-router'
import Layout from'@/views/Layout/index.vue'
import Login from '@/views/Login/index.vue'
import Home from'@/views/Home/index.vue'
import Category from'@/views/Category/index.vue'
import Detail from'@/views/Detail/index.vue'
import Cartlist from'@/views/Cartlist/index.vue'
import Order from'@/views/Order/index.vue'
import Search from '@/views/Search/index.vue'
import SalerLayout from'@/views/SalerLayout/index.vue'
import CateManage from '@/views/CateManage/index.vue'
import GoodsStatus from '@/views/GoodsStatus/index.vue'
import PurchaseRecord from '@/views/PurchaseRecord/index.vue'
import AdminLayout from'@/views/AdminLayout/index.vue'
import SalerCategory from'@/views/SalerCategory/index.vue'
import GoodsCategory from'@/views/GoodsCategory/index.vue'
import SalesReport from'@/views/SalesReport/index.vue'
import UserProfile from '@/views/userProfile/index.vue'
import LoginData from '@/views/LoginData/index.vue'
import OperateData from '@/views/OperateData/index.vue'
import AnomalyDetection from '@/views/AnomalyDetection/index.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path:'/',
            component:Layout,
            children:[
                {
                    path:'',
                    component:Home
                },
                {
                    path:'/category/:id',
                    component:Category
                },
                {
                    path:'/detail/:id',
                    component:Detail
                },
                {
                    path:'/cartlist',
                    component:Cartlist
                },
                {
                    path:'/order',
                    component:Order
                },
                {
                    path:'/search',
                    component:Search
                }
            ]
        },
        {
            path: '/login',
            component: Login
        },
        {
            path:'/salerlayout',
            component:SalerLayout,
            children:[
                {
                    path:'',
                    component:CateManage
                },
                {
                    path:'/status',
                    component:GoodsStatus
                },
                {
                    path:'/record',
                    component:PurchaseRecord
                }
            ]
        },
        {
            path:'/adminlayout',
            component:AdminLayout,
            children:[
                {
                    path:'',
                    component:SalerCategory
                },
                {
                    path:'/goods',
                    component:GoodsCategory
                },
                {
                    path:'/report',
                    component:SalesReport
                },
                {
                    path: '/userprofile',
                    component: UserProfile
                },
                {
                    path: '/logindata',
                    component: LoginData
                },
                {
                    path: '/operatedata',
                    component: OperateData
                },
                {
                    path: '/anomal',
                    component: AnomalyDetection
                }
            ]
        }
    ]
})

export default router
