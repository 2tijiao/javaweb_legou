import request from '@/utils/request.js'
//销售员登录请求
export const salerLoginApi=(loginData)=>{
    const params=new URLSearchParams(loginData);
    return request.post("/saler/login",params)
    
}
//获取销售员负责商品请求
export const getGoodsApi=()=>{
    return request.get("/saler/getgoods")
}
//增加商品请求
export const addGoodsApi=(goodsData)=>{
    return request.post("/saler/add",goodsData);
}
//删除商品请求
export const delGoodsApi=(id)=>{
    return request.get("/saler/del?id="+id);
}
//更新商品信息请求
export const updateGoodsApi=(goodsData)=>{
    return request.post("saler/update",goodsData)
}
//获取商品销售记录请求
export const getRecordApi=()=>{
    return request.get("/saler/getrecord")
}
//获取商品销售额比率请求
export const getRatioApi=()=>{
    return request.get("saler/ratio")
}
//获取商品销售状态请求
export const getStatusApi=()=>{
    return request.get('saler/getstatus')
}
//销售员退出请求
export const salerLogoutApi = () => {
    return request.get('/saler/logout')
}
//获取商品销售趋势请求
export const getTrendApi = (goodsid) => {
    return request.get('/admin/trend?goodsid=' + goodsid);
}