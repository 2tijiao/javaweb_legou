import { defineStore } from "pinia";
import { ref } from "vue";
import { searchGoodsApi } from "@/apis/goods";

export const useGoodsStore=defineStore('goods',()=>{
    const goodsList=ref([])
    
    const searchGoods=async(goodsname)=>{
        const result=await searchGoodsApi(goodsname);
        goodsList.value=result.data;
    }

    const clearGoodsList=()=>{
        goodsList.value={}
    }

    return{
        goodsList,
        searchGoods,
        clearGoodsList
    }
},{
    persist:true,
}
)