import request from'@/utils/request'
//获取用户购物车请求
export const getCartlistApi=()=>{
    return request.get("/cartlist/getlist")
}
//删除购物车商品请求
export const delCartlistApi=(id)=>{
    const params=new URLSearchParams()
    params.append('id',id);
    return request.post("/cartlist/del",params)
}
//结算购物车请求
export const submitcartApi=(data)=>{
    return request.post('/cartlist/finish',data);
}