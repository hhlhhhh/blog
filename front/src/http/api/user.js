import http from "..";


export const loginByAccountApi = (data) => {
    return http({
        url: "/user/login/account",
        method: "post",
        data:JSON.stringify(data),
        headers:{
            "Content-Type":"application/json"
        }
    })
}

export const registerApi = (data) => {
    return http({
        url: "/user/register",
        method: "post",
        data:JSON.stringify(data),
        headers:{
            "Content-Type":"application/json"
        }
    })
}

export const getUserInfoApi = () => {
    return http.get("/user")
}

export const getUserInfoByIdApi = (id) => {
    return http.get("/user/"+id)
}

export const logoutApi = () => {
    return http.post("/user/logout")
}

export const getUserProfileShow = (id) =>{
    return http.get("/user/userinfo/show",{params:{id:id}})
}

export const getEmailCodeApi = (data) =>{
    return http({
        url: "/user/email",
        method: "post",
        data:JSON.stringify(data),
        headers:{
            "Content-Type":"application/json"
        }
    })
}

export const loginByEmailApi = (data) =>{
    return http({
        url: "/user/login/email",
        method: "post",
        data:JSON.stringify(data),
        headers:{
            "Content-Type":"application/json"
        }
    })
}

export const uploadAvatar = (data) =>{
    return http({
        url: "/user/upload",
        method: "post",
        data:data,
        headers:{
            "Content-Type":"multipart/form-data"
        }
    })
}


export const updateUserInfoApi = (data) =>{
    return http({
        url: "/user/info",
        method: "put",
        data:JSON.stringify(data),
        headers:{
            "Content-Type":"application/json"
        }
    })
}

export const resetPasswordApi = (data) => {
    return http({
        url: "/user/resetPassword/email",
        method: "put",
        data:JSON.stringify(data),
        headers:{
            "Content-Type":"application/json"
        }
    })
}