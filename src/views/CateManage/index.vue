<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ref } from 'vue'
import { delGoodsApi, getGoodsApi, updateGoodsApi} from '@/apis/saler'
import { useSalerStore } from '@/stores/salerStore'
import { addGoodsApi } from '@/apis/saler'

//获取管理商品列表
const goods = ref([
    {
            "id": 1,
            "goodsname": "Cucci",
            "price": 1999.00,
            "amount": 100,
            "desc":"名牌包包",
            "picture":"picture.com",
        }
])
//声明一个异步的函数
const getGoods=async()=>{
    let result=await getGoodsApi()
    goods.value=result.data;
}
getGoods()

//控制添加分类弹窗
const dialogVisible = ref(false)

const salerStore=useSalerStore()
//添加分类数据模型
const categoryModel = ref({
    goodsname:'',
    price:100,
    amount:1,
    desc:'',
    picture:'',
    categoryid:salerStore.salerInfo.salerInfo.categoryid
})
//添加商品表单校验
const rules = {
    goodsname: [
        { required: true, message: '请输入商品名称', trigger: 'blur' },
    ],
    price: [
        { required: true, message: '请输入商品价格', trigger: 'blur' },
    ],
    amount:[
        {required: true, message: '请输入商品数量', trigger: 'blur'},
    ],
    desc:[
        {required: true, message: '请输入商品简介', trigger: 'blur'},
    ],
    picture:[
        {required: true, message: '请输入商品图片的URL', trigger: 'blur'},
    ]
}

const title=ref('')
const setValue=()=>{
    categoryModel.value.goodsname='';
    categoryModel.value.price='';
    categoryModel.value.amount=1;
    categoryModel.value.desc='';
    categoryModel.value.picture='';
}
//调用接口添加商品
const addGoods=async()=>{
    let result=await addGoodsApi(categoryModel.value);
    alert(result.data);
    dialogVisible.value=false;
    getGoods();
}

//删除商品函数
const goodsDelete=async(row)=>{
    let result=await delGoodsApi(row.id)
    alert(result.data)
    getGoods()
}

//更新商品
const showdialog=(row)=>{
    dialogVisible.value=true
    //将title值设置为编辑商品，方便后续判断
    title.value='编辑商品'
    //数据拷贝
    categoryModel.value.goodsname=row.goodsname;
    categoryModel.value.price=row.price;
    categoryModel.value.amount=row.amount;
    categoryModel.value.desc=row.desc;
    categoryModel.value.picture=row.picture;
    //扩展id属性，更新和删除商品需要
    categoryModel.value.id=row.id;
}

const updateGoods=async()=>{
    let result=await updateGoodsApi(categoryModel.value);
    alert(result.data)
    getGoods();
    dialogVisible.value=false;
}

</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>商品列表</span>
                <div class="extra">
                    <el-button type="primary" @click="dialogVisible=true,title='添加商品',setValue()">添加商品</el-button>
                </div>
            </div>
        </template>
        <el-table :data="goods" style="width: 100%">
            <el-table-column label="序号" width="100" type="index"> </el-table-column>
            <el-table-column label="商品名称" prop="goodsname"></el-table-column>
            <el-table-column label="商品价格" prop="price"></el-table-column>
            <el-table-column label="商品库存" prop="amount"></el-table-column>
            <el-table-column label="商品简介" prop="desc"></el-table-column>
            <el-table-column label="商品图片">
                <template #default="{ row }">
                    <img :src="row.picture" alt="商品图片" style="max-width: 80px; height: auto">
                </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="showdialog(row)"></el-button>
                    <el-popconfirm title="确认删除吗?" confirm-button-text="确认" cancel-button-text="取消" @confirm="goodsDelete(row)">
                    <template #reference>
                      <el-button :icon="Delete" circle plain type="danger"></el-button>
                    </template>
                  </el-popconfirm>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>

        <!--添加商品弹窗-->
        <el-dialog v-model="dialogVisible" :title="title" width="30%">
            <el-form :model="categoryModel" :rules="rules" label-width="100px" style="padding-right: 30px">
                <el-form-item label="商品名称" prop="goodsname">
                    <el-input v-model="categoryModel.goodsname" minlength="1"></el-input>
                </el-form-item>
                 <el-form-item label="商品价格" prop="price">
                    <el-input v-model="categoryModel.price" minlength="1"></el-input>
                </el-form-item>
                <el-form-item label="商品数量" prop="amount">
                    <el-input v-model="categoryModel.amount" minlength="1"></el-input>
                </el-form-item>
                <el-form-item label="商品简介" prop="desc">
                    <el-input v-model="categoryModel.desc" minlength="1"></el-input>
                </el-form-item>
                <el-form-item label="商品图片URL" prop="picture">
                    <el-input v-model="categoryModel.picture" minlength="1"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="title=='添加商品'?addGoods():updateGoods()"> 确认 </el-button>
                </span>
            </template>
        </el-dialog>

    </el-card>
</template>

<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
    .search{
        width: 30%;
        margin-right: 15%;
    }
    .search .input{
        height: 100%;
        width: 70%;
        float: left;
    }
    .search .button{
        width: 10%;
        float: right;
    }
    
}
</style>