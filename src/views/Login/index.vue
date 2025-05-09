<script setup>
import { ref } from 'vue';
import { userRegisterApi } from '@/apis/user';
import { useRouter } from 'vue-router';
import { useTokenStore } from '@/stores/token';
import { useUserStore } from '@/stores/userStore';
import { useSalerStore } from '@/stores/salerStore';
import { useAdminStore } from '@/stores/adminStore';
import {User,Avatar} from '@element-plus/icons-vue'

const isLogin=ref(true);
const title=ref("用户登录");

//注册方法
const registerDate=ref({
    username:'',
    email:'',
    age:'',
    location:'',
    pwd:'',
    repwd:'',
})

//注册表单校验规则
const rules={
    name:[
        {required:true,message:'用户名不能为空',trigger:'blur'}
    ],
    username:[
        {required:true,message:'用户名不能为空',trigger:'blur'}
    ],
    email:[
        { required: true, message: '邮箱不能为空', trigger: 'blur' },
        { 
            pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
            message: '请输入有效的邮箱地址',
            trigger: 'blur'
        }
    ],
    age:[
        {required:true,message:'年龄不能为空',trigger:'blur'},
        {
            validator: (rule, value, callback) => {
                if (value < 1 || value > 100) {
                    callback(new Error('年龄必须在1到100岁之间'));
                } else {
                    callback();
                }
            },
            trigger: 'blur'
        }
    ],
    location:[
        {required:true,message:'地址不能为空',trigger:'blur'},
        {
            validator: (rule, value, callback) => {
                const pattern = /^[\u4e00-\u9fa5]+省[\u4e00-\u9fa5]+市$/;
                if (!pattern.test(value)) {
                    callback(new Error('地址必须符合xx省xx市的格式'));
                } else {
                    callback();
                }
            },
            trigger: 'blur'
        }
    ],
    pwd:[
        { required: true, message: '密码不能为空', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度为6-20个字符', trigger: 'blur' }
    ],
    repwd:[
        {
            validator: (rule, value, callback) => {
                if (value === '') {callback(new Error('请再次输入密码'))}
                else if (value !== registerDate.value.pwd) {callback(new Error("两次密码不一致!"))}
                else {callback()}
            },
            trigger: 'blur'
        }
    ]
}

const registerFormRef=ref(null);

const doRegister=()=>{
    registerFormRef.value.validate(async(valid)=>{
        if(valid){
            let result=await userRegisterApi(registerDate.value);
            alert(result.msg? result.msg:'注册成功');
            isLogin.value=true
        }
        else{
            alert("输入信息有误，请重新输入");
        }
})
}


//登录方法
const loginDate=ref({
    name:'',
    pwd:''
})
const router=useRouter();
const tokenStore=useTokenStore();
const userStore=useUserStore();
const salerStore=useSalerStore();
const adminStore=useAdminStore();
const loginFormRef=ref(null)
const doLogin = async () => {
    try {
        // 将表单验证转换为Promise
        const isValid = await new Promise(resolve => {
            loginFormRef.value.validate(valid => resolve(valid));
        });

        if (isValid) {
            if(title.value==='用户登录'){
                // 调用用户信息获取接口
                await userStore.getUserInfo(loginDate.value);
                // 检查token是否存在
                if (!userStore.userInfo?.token) {
                    alert("未注册，请先注册");
                    return;
                }
                // 设置token并跳转
                tokenStore.setToken(userStore.userInfo.token);
                router.push("/");
            }
            else if(title.value==='销售登录'){
                await salerStore.getSalerInfo(loginDate.value);
                if (!salerStore.salerInfo?.token) {
                    alert("该销售员不存在");
                    return;
                }
                // 设置token并跳转
                tokenStore.setToken(salerStore.salerInfo.token);
                router.push("/salerlayout");
            }
            else if(title.value==='管理员登录'){
                await adminStore.getAdminInfo(loginDate.value);
                if (!adminStore.adminInfo?.token) {
                    alert("该管理员不存在");
                    return;
                }
                // 设置token并跳转
                tokenStore.setToken(adminStore.adminInfo.token);
                router.push("/adminlayout");
            }
        }
    } catch (error) {
        console.error("登录失败:", error);
    }
};

//注册清空
const register=()=>{
    isLogin.value=false;
    registerDate.value.age='';
    registerDate.value.email='';
    registerDate.value.location='';
    registerDate.value.pwd='';
    registerDate.value.repwd='';
    registerDate.value.username='';
}

//销售登录清空
const salerlogin=()=>{
    title.value='销售登录';
    loginDate.value.name='';
    loginDate.value.pwd='';
}

//管理员登录清空
const adminlogin=()=>{
    title.value='管理员登录';
    loginDate.value.name='';
    loginDate.value.pwd='';
}

//返回登陆清空
const returnuserlogin=()=>{
    title.value='用户登录';
    loginDate.value.name='';
    loginDate.value.pwd='';
}


</script>


<template>
    <div>
        <header class="login-header">
            <div class="container m-top-20">
                <h1 class="logo">
                    <RouterLink to="/">乐购</RouterLink>
                </h1>
                <RouterLink class="entry" to="/">
                    进入网站首页
                    <i class="iconfont icon-angle-right">></i>
                    <i class="iconfont icon-angle-right">></i>
                </RouterLink>
            </div>
        </header>
        <section class="login-section">
            <div class="wrapper">

                <!--登录表单-->
                <div v-if="isLogin">
                    <nav class="loginNav">
                        <el-button :icon="User" class="sales" @click="salerlogin"></el-button>
                        <div>{{ title }}</div>
                        <el-button :icon="Avatar" class="admin" @click="adminlogin"></el-button>
                    </nav>
                    <div class="account-box">
                        <div class="form">
                            <el-form ref="loginFormRef" :model="loginDate" :rules="rules" label-position="right" label-width="60px"
                                status-icon>
                                <el-form-item prop="name" label="账户">
                                    <el-input placeholder="请输入用户名" v-model="loginDate.name"></el-input>
                                </el-form-item>
                                <el-form-item prop="pwd" label="密码">
                                    <el-input placeholder="请输入密码" v-model="loginDate.pwd" type="password"></el-input>
                                </el-form-item>
                                <el-button size="large" class="subBtn" @click="doLogin">点击登录</el-button>
                                <div class="link-container">
                                    <el-link class="returnLink" type="info" :underline="false" @click="returnuserlogin">⭠返回</el-link>
                                    <el-link class="registerLink" type="info" :underline="false" @click="register">注册⭢</el-link>
                                </div>
                            </el-form>
                        </div>
                    </div>
                </div>

                <!--注册表单-->
                <div v-else>
                    <nav class="registerNav">
                        <div>用户注册</div>
                    </nav>
                    <div class="account-box">
                        <div class="form">
                            <el-form ref="registerFormRef" :model="registerDate" :rules="rules" label-position="right" label-width="60px"
                                status-icon>
                                <el-form-item prop="username" label="账户">
                                    <el-input placeholder="请输入用户名" v-model="registerDate.username"></el-input>
                                </el-form-item>
                                <el-form-item prop="email" label="邮箱">
                                    <el-input placeholder="请输入邮箱地址" v-model="registerDate.email"></el-input>
                                </el-form-item>
                                <el-form-item prop="age" label="年龄">
                                    <el-input placeholder="请输入年龄" v-model="registerDate.age"></el-input>
                                </el-form-item>
                                <el-form-item prop="location" label="地点">
                                    <el-input placeholder="请输入地址（xx省xx市）" v-model="registerDate.location"></el-input>
                                </el-form-item>
                                <el-form-item prop="pwd" label="密码">
                                    <el-input placeholder="请输入密码" v-model="registerDate.pwd" type="password"></el-input>
                                </el-form-item>
                                <el-form-item prop="repwd" label="密码">
                                    <el-input placeholder="请再一次输入密码" v-model="registerDate.repwd" type="password"></el-input>
                                </el-form-item>
                                <el-button size="large" class="subBtn" @click="doRegister">点击注册</el-button>
                                <div class="link-container">
                                    <el-link class="returnLink" type="info" :underline="false" @click="isLogin=true">⭠返回</el-link>
                                </div>
                            </el-form>
                        </div>
                    </div>
                </div>

            </div>
        </section>

        <footer class="login-footer">
            <div class="container">
                <p>
                    <a href="javascript:;">关于我们</a>
                    <a href="javascript:;">帮助中心</a>
                    <a href="javascript:;">售后服务</a>
                    <a href="javascript:;">配送与验收</a>
                    <a href="javascript:;">商务合作</a>
                    <a href="javascript:;">搜索推荐</a>
                    <a href="javascript:;">友情链接</a>
                </p>
                <p>CopyRight &copy; 乐购</p>
            </div>
        </footer>
    </div>
</template>

<style scoped lang='scss'>
.registerNav{
    display: flex;
    justify-content: center;
    align-items: center;
    height: 50px; /* 可根据需求调整导航栏高度 */
}
.loginNav{
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 24px; /* 增大字号，可根据需求调整 */

    .sales,
    .admin {
    border: none;
    background: none;
    box-shadow: none;
    padding: 0;
}
}
.login-header {
    background: #dfd7d7;
    border-bottom: 1px solid #e4e4e4;

    .container {
        display: flex;
        align-items: flex-end;
        justify-content: space-between;
    }

    .logo {
        width: 200px;

        a {
            display: block;
            height: 132px;
            width: 100%;
            text-indent: -9999px;
            background: url("@/assets/images/logo.png") no-repeat center 18px / contain;
        }
    }

    .sub {
        flex: 1;
        font-size: 24px;
        font-weight: normal;
        margin-bottom: 38px;
        margin-left: 20px;
        color: #666;
    }

    .entry {
        width: 120px;
        margin-bottom: 38px;
        font-size: 16px;
        color:$xtxColor;

        i {
            font-size: 14px;
            color: $xtxColor;
            letter-spacing: -5px;
        }
    }
}

.login-section {
    background: url('@/assets/images/login-bg.png') no-repeat center / cover;
    height: 488px;
    position: relative;

    .wrapper {
        width: 380px;
        background: #ffffff;
        position: absolute;
        left: 50%;
        top: 54px;
        transform: translate3d(100px, 0, 0);
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.15);

        nav {
            font-size: 14px;
            height: 55px;
            margin-bottom: 20px;
            border-bottom: 1px solid #f5f5f5;
            display: flex;
            padding: 0 40px;
            text-align: right;
            align-items: center;

            a {
                flex: 1;
                line-height: 1;
                display: inline-block;
                font-size: 18px;
                position: relative;
                text-align: center;
            }
        }
    }
}

.login-footer {
    padding: 30px 0 50px;
    background: #f0ecec;
    
    p {
        text-align: center;
        color: #999;
        padding-top: 20px;

        a {
            line-height: 1;
            padding: 0 10px;
            color: #999;
            display: inline-block;

            ~a {
                border-left: 1px solid #ccc;
            }
        }
    }
}

.account-box {
    
    .toggle {
        padding: 15px 40px;
        text-align: right;

        a {
            color: $xtxColor;

            i {
                font-size: 14px;
            }
        }
    }

    .form {
        padding: 0 20px 20px 20px;

        &-item {
            margin-bottom: 28px;

            .input {
                position: relative;
                height: 36px;

                >i {
                    width: 34px;
                    height: 34px;
                    background: #cfcdcd;
                    color: #fff;
                    position: absolute;
                    left: 1px;
                    top: 1px;
                    text-align: center;
                    line-height: 34px;
                    font-size: 18px;
                }

                input {
                    padding-left: 44px;
                    border: 1px solid #cfcdcd;
                    height: 36px;
                    line-height: 36px;
                    width: 100%;

                    &.error {
                        border-color: $priceColor;
                    }

                    &.active,
                    &:focus {
                        border-color: $xtxColor;
                    }
                }

                .code {
                    position: absolute;
                    right: 1px;
                    top: 1px;
                    text-align: center;
                    line-height: 34px;
                    font-size: 14px;
                    background: #f5f5f5;
                    color: #666;
                    width: 90px;
                    height: 34px;
                    cursor: pointer;
                }
            }

            >.error {
                position: absolute;
                font-size: 12px;
                line-height: 28px;
                color: $priceColor;

                i {
                    font-size: 14px;
                    margin-right: 2px;
                }
            }
        }

        .agree {
            a {
                color: #069;
            }
        }

        .btn {
            display: block;
            width: 100%;
            height: 40px;
            color: #fff;
            text-align: center;
            line-height: 40px;
            background: $xtxColor;

            &.disabled {
                background: #cfcdcd;
            }
        }
    }

    .action {
        padding: 20px 40px;
        display: flex;
        justify-content: space-between;
        align-items: center;

        .url {
            a {
                color: #999;
                margin-left: 10px;
            }
        }
    }
}

.subBtn {
    background: $xtxColor;
    width: 100%;
    color: #fff;
    margin-bottom: 10px;
}
.link-container {
    display: flex;
    justify-content: space-between;
}
</style>