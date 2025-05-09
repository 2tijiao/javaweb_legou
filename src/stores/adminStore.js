import { defineStore } from "pinia";
import { ref } from "vue";
import { adminLoginApi } from "@/apis/admin";

export const useAdminStore=defineStore('admin',()=>{
    const adminInfo=ref({})
    
    const getAdminInfo=async(loginData)=>{
        const result=await adminLoginApi(loginData);
        adminInfo.value=result.data
    }

    const clearAdminInfo=()=>{
        adminInfo.value={}
    }

    return{
        adminInfo,
        getAdminInfo,
        clearAdminInfo
    }
},{
    persist:true,
}
)