import request from '../utils/request';


export const deleteFriendLinkApi = (id) => {
    return request.delete("http://localhost:8090/friendLink/" + id)
}

export const getFriendLinkList = (pageView) => {
    return request({ 
        url: "http://localhost:8090/friendLink/list", 
        method: "post", 
        data: JSON.stringify(pageView) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const saveFriendLinkApi = (tag) => {
    return request({ 
        url: "http://localhost:8090/friendLink/save", 
        method: "post", 
        data: JSON.stringify(tag) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}