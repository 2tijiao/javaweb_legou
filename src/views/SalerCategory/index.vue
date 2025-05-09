<script setup>
import {
    Select,
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ref } from 'vue'
import * as echarts from 'echarts'
import { getSalerApi } from '@/apis/admin'
import { delSalerApi } from '@/apis/admin'
import { modifyPwdApi } from '@/apis/admin'
import { addSalerApi } from '@/apis/admin'
import { getSalesApi } from '@/apis/admin'
import { getCategorysApi } from '@/apis/admin'

//获取销售员列表
const salercategorys=ref([
    {
        "id":1,
        "salername":"",
        "pwd":"",
        "categoryid":1

    }
])
const getSaler=async()=>{
    let result=await getSalerApi()
    salercategorys.value=result.data;
}
getSaler();

//删除管理员
const salerDelete=async(row)=>{
    await delSalerApi(row.id);
    getSaler()
}

//修改密码
const pwdVisible=ref(false);
const pwdData=ref({
    id:1,
    pwd:''
})

const showPwdDialog=(row)=>{
    pwdData.value.id=row.id;
    salercategorys.value.salername=row.salername;
    pwdVisible.value=true;
}

const modifypwd=async()=>{
    let result=await modifyPwdApi(pwdData.value)
    alert(result.data);
    pwdData.value.pwd=''
    pwdVisible.value=false;
}

//添加新销售员
const salerVisible=ref(false)
const salerData=ref({
    salername:'',
    pwd:'',
    categoryid:1
})
const data=ref([
    {
        value:"1",
        label:"母婴"
    }
])

const getCategorys=async()=>{
    let result=await getCategorysApi();
    data.value = result.data.map(item => ({
        value: item.id.toString(),
        label: item.categoryname
      }));
}
getCategorys()

const showSalerDialog=()=>{
    salerVisible.value=true;
    salerData.value.salername='';
    salerData.value.pwd='';
}
const addSaler=async()=>{
    let result=await addSalerApi(salerData.value);
    alert(result.data)
    salerVisible.value=false;
    getSaler();
}

//展示销售员业绩
const orderVisible=ref(false)
const orderData=ref([
    {
        "goodsname":"iphone14充电器",
        "goodsprice":149.00,
        "goodsnum":1,
        "totalprice":149.00
    }
])
const showOrderdialog=async(row)=>{
    orderVisible.value=true;
    let result=await getSalesApi(row.id)
    orderData.value=result.data;
}


</script>

<template>
    <el-card class="page-container">

        <template #header>
            <div class="header">
                <span>销售人员列表</span>
                <el-button @click="showSalerDialog">添加销售人员</el-button>
            </div>    
        </template>

        <el-table :data="salercategorys" style="width: 100%">
            <el-table-column label="序号" width="150" type="index" align="center"> </el-table-column>
            <el-table-column label="用户名"   prop="salername" align="center" width="250"> </el-table-column>
            <el-table-column label="管理商品id" prop="categoryid" width="250" align="center"></el-table-column>
            <el-table-column label="操作" align="center">
                <template #default="{ row }">
                    <el-button @click="showPwdDialog(row)">修改密码</el-button>
                    <el-popconfirm title="确认删除吗?" confirm-button-text="确认" cancel-button-text="取消" @confirm="salerDelete(row)">
                    <template #reference>
                      <el-button>删除</el-button>
                    </template>
                  </el-popconfirm>
                </template>
            </el-table-column>
            <el-table-column label="销售业绩" align="center">
                <template #default="{ row }">
                    <el-button @click="showOrderdialog(row)">查看销售业绩</el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>

        <!--修改销售员密码弹窗-->
        <el-dialog v-model="pwdVisible">
            <el-form :model="pwdData" class="custom-form">
                <el-form-item label="用户名">
                    <el-input v-model="salercategorys.salername"></el-input>
                </el-form-item>
                <el-form-item label="密码">
                    <el-input placeholder="请输入新密码" v-model="pwdData.pwd" type="password"></el-input>
                </el-form-item>
                <el-button @click="modifypwd">修改密码</el-button>
                <el-button @click="pwdVisible=false">取消</el-button>
            </el-form>
        </el-dialog>

        <!--添加销售员弹窗-->
        <el-dialog v-model="salerVisible">
            <el-form :model="salerData" class="custom-form">
                <el-form-item label="用户名">
                    <el-input placeholder="请输入销售员名" v-model="salerData.salername"></el-input>
                </el-form-item>
                <el-form-item label="密码">
                    <el-input placeholder="请输入密码" v-model="salerData.pwd" type="password"></el-input>
                </el-form-item>
                <el-form-item label="销售品种">
                    <el-tree-select
                    v-model="salerData.categoryid"
                    :data="data"
                    :render-after-expand="false"
                    style="width: 240px"
                    />
                </el-form-item>
                <el-button @click="addSaler">添加销售员</el-button>
                <el-button @click="salerVisible=false">取消</el-button>
            </el-form>
        </el-dialog>


        <!--数据展示弹窗-->
        <el-dialog v-model="orderVisible" width="50%">
            <el-table :data="orderData" style="width: 100%">
                <el-table-column label="序号" width="100" type="index"> </el-table-column>
                <el-table-column label="商品名称" prop="goodsname"></el-table-column>
                <el-table-column label="商品价格" prop="goodsprice"></el-table-column>
                <el-table-column label="商品数量" prop="goodsnum"></el-table-column>
                <el-table-column label="商品总销量" prop="totalprice"></el-table-column>
            </el-table>
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

    .search {
        width: 50%;
        margin-right: 5%;

        .select {
            float: left;
            width: 20%;
        }

        .input {
            width: 50%;
        }
    }

    .search .button {
        width: 10%;
        float: right;
    }

    .tip {
        margin-left: 20px;
        font-size: 15px;
    }

    .bt {
        margin-top: 20px;
    }

    .el-table__header-wrapper th {
        flex: 1;
        min-width: 100px;
    }

    .el-table__body-wrapper td {
        flex: 1;
        min-width: 100px;
    }
}
</style>