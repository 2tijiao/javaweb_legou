import { defineStore } from "pinia";
import { ref } from "vue";
import { salerLoginApi } from "@/apis/saler";

export const useSalerStore=defineStore('saler',()=>{
    const salerInfo=ref({})
    
    const getSalerInfo=async(loginData)=>{
        const result=await salerLoginApi(loginData);
        salerInfo.value=result.data
    }

    const clearSalerInfo=()=>{
        salerInfo.value={}
    }

    return{
        salerInfo,
        getSalerInfo,
        clearSalerInfo
    }
},{
    persist:true,
}
)