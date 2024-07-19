import request from '../utils/request';


export const deleteTagApi = (id) => {
    return request.delete("http://localhost:8090/tag/" + id)
}

export const getTagList = (pageView) => {
    return request({ 
        url: "http://localhost:8090/tag/list", 
        method: "post", 
        data: JSON.stringify(pageView) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export const saveTagApi = (tag) => {
    return request({ 
        url: "http://localhost:8090/tag/save", 
        method: "post", 
        data: JSON.stringify(tag) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}