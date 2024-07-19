<template>
    <div class="login">
      <div class="content">
        <div class="box1">
          <img src="@/assets/login-image-01.png">
        </div>
        <div class="box2">
          <a-tabs default-active-key="1" @change="tabChange">
            <a-tab-pane key="1" title="密码登录">
              <div class="login-by-password">
                <ul>
                  <li>
                    <a-input-number placeholder="账号" hide-button allow-clear v-model="form.account">
                      <template #prefix>
                        <icon-user />
                      </template>
                    </a-input-number>
                  </li>
                  <li>
                    <a-input-password placeholder="密码" v-model="form.password">
                      <template #prefix>
                        <icon-lock />
                      </template>
                    </a-input-password>
                  </li>
                  <li>
                  </li>
                  <li>
                    <button class="login-btn" @click="loginEvent">登录</button>
                  </li>
                  <li>
                    <a class="to-register" @click="router.push({name:'Register'})" >立即注册</a>
                  </li>
                </ul>
              </div>
            </a-tab-pane>
            <a-tab-pane key="2" title="邮箱登录">
              <div class="login-by-email">
                <ul>
                  <li>
                    <a-input placeholder="邮箱" allow-clear v-model="form.email">
                      <template #prefix>
                        <icon-email />
                      </template>
                    </a-input>
                  </li>
                  <li class="code-row">
                    <a-input-number placeholder="验证码" hide-button v-model="form.authCode">
                      <template #prefix>
                        <icon-code-square />
                      </template>
                    </a-input-number>
                    <a-button class="get-email-code-btn" @click="getEmailCode">{{getEmailCodeWaitTime===0?"获取验证码":getEmailCodeWaitTime}}</a-button>
                  </li>
                  <li style="margin-top: 36px;">
                    <button class="login-btn" :disabled="EmailLoginWait" @click="loginEvent">登录</button>
                  </li>
                  <li>
                    <a class="to-register" @click="router.push({name:'Register'})">立即注册</a>
                  </li>
                </ul>
              </div>
            </a-tab-pane>
          </a-tabs>
        </div>
      </div>
    </div>
</template>

<script setup>
import {ref,reactive,getCurrentInstance,onMounted} from "vue";
import {useRoute, useRouter} from "vue-router";
import {loginByAccountApi,loginByEmailApi,getUserInfoApi ,getEmailCodeApi} from "@/http/api/user.js";
import { Message } from '@arco-design/web-vue';
import http from "@/http/index.js";
import {UserStore} from "@/store/index.js"

const userStore = UserStore()
//页面路由
const route = useRoute()
const router = useRouter()

//true为密码登录
//false为邮箱验证码登录
let loginType = ref(true)

let getEmailCodeWaitTime=ref(0);  //获取验证码等待时间
let EmailLoginWait = ref(false)  //邮箱登录等待

const form  = reactive({
    account:null,
    password:"",
    email:"",
    authCode:null
})

const tabChange = ()=>{
    loginType.value=!loginType.value
}

onMounted(() => {
    let acc = route.query.account   //是否有注册的新账号
    if(acc){
        form.account = parseInt(acc)
    } 
})

//邮箱验证格式
let emailVerify = /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/

//获取邮箱验证码
const getEmailCode = ()=>{
    if(!emailVerify.test(form.email)){  //邮箱格式验证
        Message.error('邮箱格式错误！')
        return
    }
    if(getEmailCodeWaitTime.value>0){   //提示勿重复发送
        Message.info( '发送给' + form.email + "!")
        return;
    }
    getEmailCodeWaitTime.value=60
    const timeLoop =setInterval(function (){      //倒计时
        if(getEmailCodeWaitTime.value<=0){
            clearInterval(timeLoop)
            return
        }
        getEmailCodeWaitTime.value-=1;
    },1000)

    getEmailCodeApi({email:form.email,type:"Login"}).then(res=>{
        if(res.data.state=="SUCCESS") {
            Message.success('发送给' + form.email + "!")
        }else{
            Message.error( '发送给'+form.email+"失败!")
        }
    })
    Message.info('正在发送，请稍等！')
}

//密码登录
function loginByPassword(){
    if(!/^[1-9]\d{9}$/.test(form.account)){  //账号格式验证
        Message.error('账号格式错误！')
        return
    }
    if(!/^.{6,16}$/.test(form.password)){
        Message.error('密码应为6到16位！')
        return
    }
    EmailLoginWait.value = true
    return loginByAccountApi({
        "account": form.account,
        "password": form.password
    })
}

//邮箱登录
function loginByEmail(){
    if(!emailVerify.test(form.email)){  //邮箱格式验证
        Message.error('邮箱格式错误！')
        return
    }
    if(!/^\d{6}$/.test(form.authCode)){
        Message.error('验证码格式错误！')
        return
    }
    EmailLoginWait.value = true
    return loginByEmailApi({
        "email": form.email,
        "code": form.authCode
    })
}

//登录
const loginEvent = ()=>{
    const req = loginType.value?loginByPassword:loginByEmail;
    let f_promise = req()
    if(!f_promise)return
    
    f_promise.then(async res=>{
      console.log(res);
        if(res.data.state == "SUCCESS"){
            userStore.setToken(res.headers['authorization'])
            Message.success('登陆成功！')
            console.log(res,res.headers['authorization']);
            localStorage.setItem("token",res.headers['authorization']) //设置token
            
            const {data} = await getUserInfoApi()   //获取用户数据
            localStorage.setItem("user",JSON.stringify(data.data))
            userStore.setUser(data.data)
            console.log(userStore.user);
            router.push("/")
        }else {
            Message.error(res.data.msg)
    }
    },(err)=>{
        Message.error(err.msg)
    })
}
</script>

<style scoped lang="less">

.login{
    display: flex;
    height: 100vh;
    justify-content: center;
    align-items: center;
    background-color: rgb(233, 235, 237);
    .content{
        display:  flex;
        width: 1000px;
        height: 500px;
        box-shadow: 5px 5px 5px #f7f9fa;
        background-color: #fff;
        border-radius: 8px;
        .box1{
            >img{
            height: 500px;
            }
        }
        .box2{
            width: 320px;
            padding-top: 50px;
            margin: 0 auto;
            ul{
            text-align: center;
            >li{
                width: 320px;
                min-height: 20px;
                margin-top: 20px;
                >.arco-input-wrapper{   //修改arco的input样式
                width: 100%;
                height: 45px;
                //color: #FFF;
                border: 1px solid #e3e8f0;
                border-radius: 25px;
                background-color: rgba(0,0,0,0);
                padding: 0 20px;
                .arco-input-prefix{
                    >svg{
                    color: rgb(185, 190, 209);
                    font-size: 20px;
                    }
                }
            }
            a{
            float: right;
            color: rgba(158,161,177,1);
            margin-right: 20px;
            &:hover{
                color: rgba(0,118,254,1);
                text-decoration: underline rgba(0,118,254,1);
            }
            }
            .to-register{
            color: rgba(0,118,254,1);
            }
            .login-btn{
            width: 100%;
            height: 50px;
            cursor: pointer;
            line-height: 50px;
            color: #FFFFFF;
            font-size: 20px;
            letter-spacing: 15px;
            border: none;
            border-radius: 25px;
            background-color: rgb(0, 118, 254);
            &:disabled{
                background-color: #84add8;
            }
            }
        }
        .code-row{
            display: flex;
            .get-email-code-btn{
            height: 45px;
            border-radius: 25px;
            padding: 0 24px;
            margin-left: 15px;
            }
        }
        }
        :deep(.arco-tabs-nav-horizontal){
        .arco-tabs-nav-tab-list{
            width: 320px;
        }
        .arco-tabs-tab{   //使居中
            width: 160px;
            text-align: center;
            margin: 0;
            &:hover .arco-tabs-tab-title::before{   //消除hover黑影
            background-color: rgba(0,0,0,0);
            }
            .arco-tabs-tab-title{
            font-size: 20px;
            width: 160px;
            }
        }
        .arco-tabs-nav-ink{
            width: 160px!important;
        }
        }

    }
    }
}
</style>