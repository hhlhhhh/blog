import request from '../utils/request';

export const deleteCommentApi = (id) => {
    return request.delete("http://localhost:8090/comment/" + id)
}

export const getCommentList = (pageView) => {
    return request({ 
        url: "http://localhost:8090/comment/query", 
        method: "post", 
        data: JSON.stringify(pageView) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}