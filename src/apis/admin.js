import request from '@/utils/request'

//管理员登录请求
export const adminLoginApi=(loginData)=>{
    const params=new URLSearchParams(loginData)
    return request.post("/admin/login",params)
}
//获取销售员请求
export const getSalerApi=()=>{
    return request.get('/admin/getsaler');
}
//删除销售员请求
export const delSalerApi=(id)=>{
    return request.get('/admin/delsaler?id='+id);
}
//更改销售员登录口令请求
export const modifyPwdApi=(pwdData)=>{
    const params=new URLSearchParams(pwdData)
    return request.post('/admin/modifypwd',params);
}
//增加销售员请求
export const addSalerApi=(salerData)=>{
    const params=new URLSearchParams(salerData)
    return request.post('/admin/addsaler',params);
}
//获取商品销售记录请求
export const getSalesApi=(id)=>{
    return request.get('/admin/getsales?id='+id);
}
//获取商品销售状态请求
export const getReportApi=()=>{
    return request.get('/admin/getreport')
}
//获取商品销售额比率请求
export const getGoodsRatioApi=()=>{
    return request.get('/admin/getratio')
}
//获取各种类销售情况请求
export const getCategorysApi=()=>{
    return request.get("/admin/getcategorys")
}
//增加商品种类请求
export const addCategoryApi=(categoryname)=>{
    return request.get("/admin/addcategory?categoryname="+categoryname);
}
//管理员退出请求
export const adminLogoutApi=()=>{
    return request.get('/admin/logout')
}
//增加商品数量请求
export const addAmountApi=(amountData)=>{
    const params = new URLSearchParams(amountData)
    return request.post('/admin/addamount', params);
}
//获取商品销售趋势请求
export const getTrendApi=(goodsid)=>{
    return request.get('/admin/trend?goodsid='+goodsid);
}
//获取用户画像请求
export const getUserProfileApi=()=>{
    return request.get('/admin/getlabel')
}
//获取登录数据请求
export const getLoginDataApi=()=>{
    return request.get('/admin/getlogindata')
}
//获取操作数据请求
export const getOperateDataApi = () => {
    return request.get('/admin/getoperatedata')
}
//获取异常销售记录请求
export const anomalyDetectionApi=()=>{
    return request.get('/admin/anomal')
}
//删除订单请求
export const deleteOrderApi=(orderid)=>{
    return request.get('/admin/deleteorder?orderid='+orderid)
}

export const deleteCategory=(cateid)=>{
    return request.get('/admin/delcategory?id='+cateid)
}