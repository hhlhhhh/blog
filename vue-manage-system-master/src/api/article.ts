import request from '../utils/request';

export const deleteArticleApi = (id) => {
    return request.delete("http://localhost:8090/article/" + id)
}

export const getArticleById = (id) => {
    return request.get("http://localhost:8090/article/" + id)
}

export const getArticleList = (pageView) => {
    return request({ 
        url: "http://localhost:8090/article/query/admin", 
        method: "post", 
        data: JSON.stringify(pageView) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const updateStatus = (data) => {
    return request({ 
        url: "http://localhost:8090/article/status/admin", 
        method: "put", 
        data: JSON.stringify(data) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}