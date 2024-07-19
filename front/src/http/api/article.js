import http from "..";

export const getArticleList = (pageView) => {
    return http({ 
        url: "/article/query", 
        method: "post", 
        data: JSON.stringify(pageView) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const getArticleByChannelId = (id,pageView) => {
    return http({ 
        url: "/article/query/"+id, 
        method: "post", 
        data: JSON.stringify(pageView) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const getTagByArticleId = (id) => {
    return http.get("/article/tag/"+id)
}

export const getTopArticle = () => {
    return http.get("/article/top")
}

export const getRankArticleList = () => {
    return http.get("/article/rank")
}

export const getArticleById = (id) => {
    return http.get("/article/" + id)
}

export const articleViewApi = (id) => {
    return http.post("/article/view/" + id)
}

export const getSelfArticleApi = (id) => {
    return http.get("/article/self",{params:{id:id}})
}

export const saveArticleApi = (data) => {
    return http({
        url: "/article/save", 
        method: "post", 
        data: JSON.stringify(data) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const uploadImageApi = (data) => {
    return http({
        url: "/media/upload",
        method: "post",
        data:data,
        headers:{
            "Content-Type":"multipart/form-data"
        }
    })
}

export const deleteArticleApi = (id) => {
    return http.delete("/article/" + id)
}