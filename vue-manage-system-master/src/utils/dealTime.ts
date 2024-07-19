
export const dealTime = (time)=>{
    if(time==null||time==''){
        return ''
    }
    let t = new Date(time)  //创建时间对象
    return t.getFullYear()+"-"+(t.getMonth()+1)+"-"+t.getDate()+" "+t.getHours()+":"+t.getMinutes()+":"+t.getSeconds()       
    //转化成yy-mm-dd格式字符串
}