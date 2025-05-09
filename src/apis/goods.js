import request from '@/utils/request'
//获取所有商品种类请求
export const getCategoryApi=()=>{
    return request.get("/goods/category")
}
//随机获取展示商品请求
export const getRandomGoods = () => {
    return request.get("/goods/random")
}
//获取新添加商品请求
export const getNewGoods=()=>{
    return request.get("/goods/new")
}
//获取指定用户感兴趣商品请求
export const getUserHotGoods = () => {
    return request.get("/goods/userhot")
}
//获取针对游客可能感兴趣请求
export const getHotGoods = () => {
    return request.get("/goods/hot")
}
//获取特定种类所有商品请求
export const getCategoryGoodsApi=(categoryid)=>{
    return request.get("/goods/categorygoods?categoryid="+categoryid)
}
//获取某商品详情请求
export const getGoodsDetailApi=(goodsid)=>{
    return request.get("/goods/detail?goodsid="+goodsid)
}
//添加商品至购物车请求
export const addCartlistApi=(goodsid,goodsnum)=>{
    const params=new URLSearchParams();
    params.append('goodsid',goodsid);
    params.append('goodsnum',goodsnum);
    return request.post("/cartlist/addcart",params);
}
//搜索商品请求
export const searchGoodsApi=(goodsname)=>{
    return request.get('/goods/search?goodsname='+goodsname)
}
//记录浏览记录请求
export const browsingRecordApi=(goodsid,dwelltime)=>{
    const params=new URLSearchParams();
    params.append('goodsid',goodsid);
    params.append('dwelltime',dwelltime);
    return request.post('/goods/browsing',params)
}