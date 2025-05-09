import request from '@/utils/request.js'
//用户登录请求
export const userLoginApi = (loginDate) => {
    //借助UrlSearchParams完成传递
    const params = new URLSearchParams(loginDate)
    return request.post('/user/login', params)
}
//用户注册请求
export const userRegisterApi=(registerDate)=>{
    const params=new URLSearchParams(registerDate)
    return request.post('/user/register',params)
}
//用户注销请求
export const userCancelApi=()=>{
    return request.get('/user/cancel');
}
//用户退出登录请求
export const userLogoutApi=()=>{
    return request.get('/user/logout');
}