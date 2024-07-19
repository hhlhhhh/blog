import http from "..";


export const getNavChannelList = () => {
    return http.get("/channel/list/nav");
}

export const getShowChannelList = (pos) =>{
    return http.get("/channel/list/show",{params:{pos}});
}

export const getChannelById = (id) => {
    return http.get("/channel/"+id);
}

export const getAllChannelList = () => {
    return http.get("/channel/list/all");
}