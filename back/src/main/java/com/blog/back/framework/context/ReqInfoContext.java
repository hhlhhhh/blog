package com.blog.back.framework.context;


public class ReqInfoContext {

    private static final ThreadLocal<ReqInfo> threadLocal = new ThreadLocal<ReqInfo>();


    public static ReqInfo getReqInfo(){
        ReqInfo reqInfo = threadLocal.get();
        if(reqInfo==null){
            threadLocal.set(new ReqInfo());
            reqInfo = threadLocal.get();
        }
        return reqInfo;
    }

    public static void addReqInfo(ReqInfo reqInfo){
        threadLocal.set(reqInfo);
    }

    public static void remove(){
        threadLocal.remove();
    }

//    public static ReqInfo getReqInfoFromHttpServletRequest(HttpServletRequest request){
//
//        return new ReqInfo().setHost(request.getRemoteHost()).
//                setMethod(request.getMethod())
//                .setClientIp(request.getRemoteAddr())
//                .setPath(request.getRequestURI())
//                .setPayload(JSON.toJSONString(request.getParameterMap()));
//    }

}
