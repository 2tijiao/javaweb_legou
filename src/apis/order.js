import request from '@/utils/request'
//获取用户订单请求
export const getOrderApi=()=>{
    return request.get("/order/get")
}
//确认收货请求
export const changeStateApi=(id)=>{
    return request.get("/order/change?id="+id);
}
//删除订单请求
export const deleteApi=(id)=>{
    return request.get("/order/del?id="+id);
}
