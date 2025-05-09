import { defineStore } from "pinia";
import { ref } from "vue";
import { userLoginApi } from "@/apis/user";

export const useUserStore=defineStore('user',()=>{
    const userInfo=ref({})
    
    const getUserInfo=async(loginData)=>{
        const result=await userLoginApi(loginData);
        userInfo.value=result.data
    }

    const clearUserInfo=()=>{
        userInfo.value={}
    }

    return{
        userInfo,
        getUserInfo,
        clearUserInfo
    }
},{
    persist:true,
}
)