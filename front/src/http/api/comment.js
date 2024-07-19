import http from "..";

export const getCommentList = (params) => {
    return http({ 
        url: "/article/query", 
        method: "post", 
        data: JSON.stringify(pageView) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const getCommentListByArticleId = (id,pageView) => {
    return http({ 
        url: "/comment/article/"+id, 
        method: "post", 
        data: JSON.stringify(pageView) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const getCommentListByParentId = (id,pageView) => {
    return http({ 
        url: "/comment/parent/"+id, 
        method: "post", 
        data: JSON.stringify(pageView) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const sendCommentApi = (comment) => {
    return http({ 
        url: "/comment", 
        method: "post", 
        data: JSON.stringify(comment) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const getSelfCommentApi = () => {
    return http.get("/comment/self")
}

export const deleteCommentApi = (id) => {
    return http.delete("/comment/"+id)
}