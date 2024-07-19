import axios from "axios";
import router from '../route';
import { UserStore } from "../store";

const userStore = UserStore();


// axios.defaults.withCredentials = false;
// // axios.defaults.headers.common['token'] =  AUTH_TOKEN
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
// axios.defaults.headers.put['Content-Type'] = 'application/json;charset=UTF-8';
// // 前端设置允许跨域
// axios.defaults.headers.post["Access-Control-Allow-Origin-Type"] = "*";

axios.defaults.headers.put['Authorization'] = "";

const http = axios.create({
    baseURL:process.env.BASE_URL,
    withCredentials: false, // 跨域请求时是否需要使用凭证
    timeout: 70000, // 请求超时时间
})


// 错误处理函数
function errorHandle(response) {
    switch (response.response.data.status) {
        case 401:{
            return Promise.reject("权限不够！")
        }
        case 404:
            return Promise.reject("无法访问此页面！")
        case 500:
            return Promise.reject("服务器异常，请稍后重试！")
        default:
            return Promise.reject(response.response);
    }
}
// 成功处理函数
function successHandle(response) {
    switch (response.status) {
        case 200:
            return Promise.resolve(response);
        default:
            return response;
    }
}

// 请求拦截器，添加token
http.interceptors.request.use(
    (config) => {
        let token = userStore.token
        if(token){
            config.headers.Authorization = token
        }else if(localStorage.getItem("token")){
            config.headers.Authorization = localStorage.getItem("token")
            userStore.setToken(localStorage.getItem("token"))
        }
        return config;
    },
    (error) => {
        error.data = {};
        error.data.msg = "客户端异常";
        return Promise.resolve(error);
    }
);
/**
 * 验证token是否过期
 * @param {string} token 
 * @returns 
 */
const verify = (token) => {
    return http({
        url: 'http://localhost:8090/user/verify',
        method: 'get',
        params: {
            token: token
        }
    });
}

http.interceptors.response.use(
    (response) => {
        if (response.status === 200) {
            if(response.data.state=="AUTHENTICATIONFAIL"){
                if(!userStore.token||userStore.token.trim()==''){
                    localStorage.removeItem("token")
                    localStorage.removeItem("user")
                    router.push('/login')
                    return;
                }
                verify(userStore.token).then(res=>{
                    if(res.data.state=="FAIL"){
                        userStore.setToken("")
                        localStorage.removeItem("token")
                        localStorage.removeItem("user")
                        router.push('/login')
                    }
                })
            }
            return response;
        } else {
            Promise.reject();
        }
    },
    (err) => {
        errorHandle(err).then(r => {
        });
    }
);

export default http;
