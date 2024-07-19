import request from '../utils/request';


export const deleteChannelApi = (id) => {
    return request.delete("http://localhost:8090/channel/" + id)
}

export const getChannelList = (pageView) => {
    return request({ 
        url: "http://localhost:8090/channel/list", 
        method: "post", 
        data: JSON.stringify(pageView) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const saveChannelApi = (channel) => {
    return request({ 
        url: "http://localhost:8090/channel/save", 
        method: "post", 
        data: JSON.stringify(channel) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const uploadImageApi = (data) => {
    return request({
        url: "http://localhost:8090/media/upload",
        method: "post",
        data:data,
        headers:{
            "Content-Type":"multipart/form-data"
        }
    })
}