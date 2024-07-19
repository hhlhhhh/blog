import http from "..";

export const getHeadNoticeList = () => http.get("/announcement/head");

export const getNoticeListApi = (pageView) => {
    return http({
        url: "/announcement/query", 
        method: "post", 
        data: JSON.stringify(pageView) ,
        headers: {
            'Content-Type': 'application/json'
        }
    })
}