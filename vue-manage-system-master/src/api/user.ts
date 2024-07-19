import request from '../utils/request';


export const getUserListApi = (params) => {
    return request({
        url: 'http://localhost:8090/user/list',
        method: 'get',
        params
    });
}

export const loginApi = (data) => {
    return request({
        url: 'http://localhost:8090/user/login/account',
        method: 'post',
        data
    });
}

export const deleteUserApi = (id) => {
    return request({
        url: 'http://localhost:8090/user/'+id,
        method: 'delete'
    });
}

export const getUserMesApi = (id) => {
    return request({
        url: 'http://localhost:8090/user/admin/mes/'+id,
        method: 'get'
    });
}

export const getUserInfoApi = () => {
    return request.get("http://localhost:8090/user")
}

export const updateUserApi = (data) => {
    return request({
        url: "http://localhost:8090/user/info/admin",
        method: "put",
        data:JSON.stringify(data),
        headers:{
            "Content-Type":"application/json"
        }
    })
}

export const logoutApi = () => {
    return request.post("http://localhost:8090/user/logout")
}