import request from '../utils/request';


export const deleteNoticeApi = (id) => {
    return request.delete("http://localhost:8090/announcement/" + id)
}

export const getNoticeList = (pageView) => {
    return request({ 
        url: "http://localhost:8090/announcement/query", 
        method: "post", 
        data: JSON.stringify(pageView) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const saveNoticeApi = (tag) => {
    return request({ 
        url: "http://localhost:8090/announcement/save", 
        method: "post", 
        data: JSON.stringify(tag) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}