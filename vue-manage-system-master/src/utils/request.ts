import axios, { AxiosInstance, AxiosError, AxiosResponse, InternalAxiosRequestConfig } from 'axios';
import { verify } from '../api';
import router from '../router';

const service: AxiosInstance = axios.create({
    timeout: 5000
});

service.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        let token = localStorage.getItem('auth')
        if(token){
            config.headers.Authorization = token
        }
        return config;
    },
    (error: AxiosError) => {
        console.log(error);
        return Promise.reject();
    }
);

service.interceptors.response.use(
    (response: AxiosResponse) => {
        if (response.status === 200) {
            // if(response.data==""){
            //     verify(localStorage.getItem('auth')).then(res=>{
            //         if(res.data.state=="FAIL"){
            //             localStorage.removeItem('auth')
            //             router.push('/login')
            //         }
            //     })
            // }
            
            return response;
        } else {
            Promise.reject();
        }
    },
    (error: AxiosError) => {
        console.log(error);
        return Promise.reject();
    }
);

export default service;
