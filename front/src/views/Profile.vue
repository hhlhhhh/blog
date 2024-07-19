<script setup>
import Footer from "@/components/Footer.vue";
import Header from "@/components/Header.vue";
import {ref ,onMounted,reactive} from "vue";
import { UserStore } from "../store";
import {getUserInfoApi, getUserProfileShow,uploadAvatar,updateUserInfoApi,getEmailCodeApi,resetPasswordApi} from "@/http/api/user.js"
import {getSelfArticleApi,deleteArticleApi} from "@/http/api/article.js"
import {getSelfCommentApi,deleteCommentApi} from "@/http/api/comment.js"
import {dealTime} from "@/utils/index.js"
import {useRouter} from 'vue-router'
import { Message } from '@arco-design/web-vue';

const router = useRouter()
const userStore = UserStore();

const userData = ref({})
const userDataArr = ref([])
const showProfile = ref({})

const selfArticle = ref([])
const selfComment = ref([])

const updateUserInfoData = reactive({
    profile:"",
    nickname:"",
    changeBtn:false
})

const changeSelfinfoBtn = (flag) => {
    updateUserInfoData.changeBtn = flag
    if(flag){
        updateUserInfoData.profile = userData.value.profile
        updateUserInfoData.nickname = userData.value.nickname
    }else{
        //update
        updateUserInfoApi({
            profile:updateUserInfoData.profile,
            nickname:updateUserInfoData.nickname
        }).then(res => {
            if(res.data.state == "SUCCESS"){
                userData.value.profile = updateUserInfoData.profile
                userData.value.nickname = updateUserInfoData.nickname
                localStorage.setItem("user",JSON.stringify(userData.value))
                userStore.user.profile = userData.value.profile
                userStore.user.nickname = userData.value.nickname
                userDataArr.value.length = 0
                userDataArr.value.push(userStore.user);
            }
        })
    }
}

let getEmailCodeWaitTime=ref(0);  //获取验证码等待时间
let EmailLoginWait = ref(false)  //邮箱登录等待

let emailVerify = /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/

const getEmailCode = ()=>{
  if(!emailVerify.test(updateUserPasswordData.email)){  //邮箱格式验证
    Message.error('邮箱格式错误！')
    return
  }
  if(getEmailCodeWaitTime.value>0){   //提示勿重复发送
    Message.info('发送给' + updateUserPasswordData.email + "!")
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

  getEmailCodeApi({email:updateUserPasswordData.email,type:"RESETPASSWORD"}).then(res=>{
    if(res.data.state == "SUCCESS") {
      Message.success('发送给' + updateUserPasswordData.email + "!")
    }else{
      Message.error( '发送给'+updateUserPasswordData.email+"失败!")
    }
  })
  Message.info('正在发送，请稍等！')
}

const updateUserPasswordData = reactive({
    password:"",
    email:"",
    code:"",
    visible: false
})

const changePasswordBtn = (state) => {
    updateUserPasswordData.visible = state
    if(!state){
        updateUserPasswordData.password = ""
        updateUserPasswordData.email = ""
        updateUserPasswordData.code = ""
    }
}

const handleOk = () => {
    resetPasswordApi({
        password:updateUserPasswordData.password,
        code:updateUserPasswordData.code
    }).then(({data})=>{
        if(data.state == "SUCCESS"){
            Message.success("修改成功！")
            localStorage.removeItem("user")
            localStorage.removeItem("token")
            userStore.user = {}
            userStore.token = ""
            router.push({path: "/login"})
        }else {
            Message.error(data.msg)
        }
    })
    changePasswordBtn(false)
};


const loadImageData = reactive({
    file: []
})


//上传头像
const updateAvatar = (list) => {
    uploadAvatar(list[0]).then(res => {
        if(res.data.state == "SUCCESS"){
            userData.value.avatar = res.data.data
            localStorage.setItem("user",JSON.stringify(userData.value))
            userStore.user.avatar = userData.value.avatar
            userDataArr.value.length = 0
            userDataArr.value.push(userStore.user);
            console.log(userDataArr.value);
            loadImageData.file.length = 0
        }
    })
}

// const changeUserInfo = () => {
//     router.push({
//         path: "/user/changeUserInfo",
//         query: {
//             userData: userData.value
//         }
//     })
// }

const deleteArticle = (id) => {
    deleteArticleApi(id).then(({data})=>{
        if(data.state == "SUCCESS"){
            Message.success("删除成功！")
            getSelfArticleApi().then(res => {
                selfArticle.value = res.data.data
            })
        }else{
            Message.error("删除失败！")
        }
    })
}

const deleteComment = (id) => {
    deleteCommentApi(id).then(({data})=>{
        if(data.state == "SUCCESS"){
            Message.success("删除成功！")
            getSelfCommentApi().then(res => {
                selfComment.value = res.data.data
            })
        }else{
            Message.error("删除失败！")
        }
    })
}


const init = async () => {
    const {data} = await getUserInfoApi();
    userData.value = data.data;
    localStorage.setItem("user",JSON.stringify(data.data))
    userStore.user = data.data
    getUserProfileShow().then(res => {
        showProfile.value = res.data.data
    })
    getSelfArticleApi().then(res => {
        selfArticle.value = res.data.data
    })
    getSelfCommentApi().then(res => {
        selfComment.value = res.data.data
    })
}

onMounted(() => {
    userData.value = userStore.user;
    userDataArr.value.length = 0
    userDataArr.value.push(userStore.user);
    init();
})


</script>

<template>
    <div class="profile">
        <a-modal v-model:visible="updateUserPasswordData.visible" @ok="handleOk" @cancel="changePasswordBtn(false)">
            <template #title>
                密码修改
            </template>
            <div>
                <div class="form">
                    <ul>
                        <li>
                            <a-input-password placeholder="密码" v-model="updateUserPasswordData.password">
                            <template #prefix>
                                <icon-lock />
                            </template>
                            </a-input-password>
                        </li>
                        <li>
                            <a-input placeholder="邮箱" allow-clear v-model="updateUserPasswordData.email">
                            <template #prefix>
                                <icon-email />
                            </template>
                            </a-input>
                        </li>
                        <li class="code-row">
                            <a-input-number placeholder="验证码" hide-button v-model="updateUserPasswordData.code">
                            <template #prefix>
                                <icon-code-square />
                            </template>
                            </a-input-number>
                            <a-button class="get-email-code-btn" @click="getEmailCode">{{getEmailCodeWaitTime===0?"获取验证码":getEmailCodeWaitTime}}</a-button>
                        </li>

                    </ul>
                </div>
            </div>
        </a-modal>
        <a-row class="header-row">
            <a-col :span="3"></a-col>
            <a-col :span="18">
                <Header></Header>
            </a-col>
            <a-col :span="3"></a-col>
        </a-row>
        <a-row>
            <a-col :span="3"></a-col>
            <a-col :span="18">
                <a-col>
                    <div class="profile-content whitebg">
                        <a-list
                            class="list-demo-action-layout"
                            :bordered="false"
                            :data="userDataArr"
                        >
                            <template #item="{ item }">
                            <a-list-item class="list-demo-item" action-layout="vertical">
                                <template #actions>
                                    
                                </template>
                                <template #extra>
                                    <div style="width: 100px;">
                                        <span v-if="!updateUserInfoData.changeBtn" style="cursor: pointer;" @click="changeSelfinfoBtn(true)"><icon-edit /></span>
                                        <span v-else style="cursor: pointer;" @click="changeSelfinfoBtn(false)"><icon-check-circle /></span>
                                        <a-divider direction="vertical" />
                                        <span style="cursor: pointer;" @click="changePasswordBtn(true)"><icon-eraser /></span>
                                        <a-divider direction="vertical" />
                                        <span style="cursor: pointer;" @click="router.push('/article')"><icon-layout /></span>
                                    </div>
                                </template>
                                <a-list-item-meta>
                                <template #title>
                                    <a-input v-if="updateUserInfoData.changeBtn" v-model="updateUserInfoData.nickname" :style="{width:'320px'}" placeholder="请输入昵称" allow-clear />
                                    <div v-else>
                                        {{ item.nickname }}
                                    </div>
                                    
                                </template>
                                <template #description>
                                    <div style="margin-left: 5px;">
                                        <div class="divider-profile">
                                            <span class="profile-num">{{ showProfile.view }}</span>
                                            <span class="profile-text">总访问量</span>
                                            <a-divider direction="vertical" />
                                            <span class="profile-num">{{ showProfile.articleCount }}</span>
                                            <span class="profile-text">创作</span>

                                        </div>
                                        <a-divider/>
                                        <div>
                                            <div>
                                                <span>加入时间：</span><span>{{ item.createTime }}</span>
                                            </div>
                                            <div style="margin-top: 12px;display: flex;">
                                                <span>个人简介：</span>
                                                <span>
                                                    <span v-if="!updateUserInfoData.changeBtn">{{ item.profile }}</span>
                                                    <a-textarea v-else style="min-width: 280px;" ref="commentArea"  v-model="updateUserInfoData.profile" :max-length="200" :auto-size="{minRows:5, maxRows:8}" allow-clear show-word-limit placeholder="请输入个人简介" />
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </template>
                                <template #avatar>
                                    <a-upload
                                        :file-list="loadImageData.file"
                                        @change="updateAvatar"
                                        :show-file-list="false"
                                        :auto-upload="false"
                                        >
                                        <template #upload-button>
                                            <a-avatar trigger-type="mask" :size="100">
                                            <img
                                                alt="avatar"
                                                style="width: 100px;height: 100px;"
                                                :src="item.avatar"
                                                />
                                                <template #trigger-icon>
                                                    <IconEdit  />
                                                </template>
                                            </a-avatar> 
                                        </template>
                                    </a-upload>
                                       
                                </template>
                                </a-list-item-meta>
                            </a-list-item>
                            </template>
                        </a-list>
                    </div>
                    <div class="whitebg">
                        <a-tabs default-active-key="1">
                            <a-tab-pane key="1" title="文章">
                                <ul style="margin-top: 10px;">
                                    <li v-for="(item,index) in selfArticle" :key="item.id">
                                        <a-divider v-if="index!=0"></a-divider>
                                        <h3 class="blogtitle"><router-link :to='"/detail/"+item.id'  arget="_blank">{{item.title}}</router-link></h3>
                                        <span class="blogpic imgscale"><i><router-link :to='"/detail/"+item.id'>{{item.channel.name}}</router-link></i>
                                            <router-link :to='"/detail/"+item.id' target="_blank">
                                                <img v-if="item.titleImg==null ||item.titleImg==''" width="130px" height="120px" src="https://img-blog.csdnimg.cn/direct/997f0d4c60774a09887a64fab2d8300a.jpeg?x-oss-process=image/resize,m_fixed,h_300,image/format,png" alt="">
                                                <img v-else  width="130px" height="130px"  :src="item.titleImg" alt="">
                                            </router-link>
                                        </span>
                                        <p class="blogtext">{{item.summary}}</p>
                                        <p class="bloginfo" style="cursor: pointer;" @click="router.push({path:'/other',query:{uid:item.user.id}})">
                                            <i class="avatar"><img class="avatar-small" :src="item.user.avatar"></i>
                                            <span>{{item.user.nickname}}</span><span>{{dealTime(item.createTime)}} </span></p>
                                            <a @click='deleteArticle(item.id)' target="_blank"  class="delete-article"> 删除文章</a>
                                            <a @click='router.push({name:"Article",query:{article_id:item.id}})' target="_blank"  class="edit-article"> 编辑文章</a>
                                            <router-link :to='"/detail/"+item.id' target="_blank"  class="viewmore"> 阅读更多</router-link >
                                    </li>
                                    <a-empty v-if="selfArticle.length==0"></a-empty>
                                </ul>
                            </a-tab-pane>
                            <a-tab-pane key="2" title="我的评论">
                                <a-comment
                                    v-for="(item,index) in selfComment"
                                    :author="userData.nickname"
                                    :align="{'actions':'right'}"
                                >
                                    <template #content>
                                        <p>{{ item.comment.content }}</p>
                                        <p style="margin-top: 10px;">
                                            来源： &nbsp;<a style="font-size: 12px;" @click="router.push('/detail/'+item.article.id)">{{ item.article.title }}</a>
                                        </p>
                                    </template>
                                    <template #datetime>
                                        <span>{{ dealTime(item.comment.createTime) }}</span>
                                        <span style="margin-left: 12px;"> <IconMessage />{{ item.comment.count }} </span>
                                    </template>
                                    <template #actions>
                                        <span class="action" >
                                            <div style="cursor: pointer;" @click="deleteComment(item.comment.id)">
                                                <icon-delete   /> 删除
                                            </div>
                                        </span>
                                    </template>
                                    <template #avatar>
                                    <a-avatar>
                                        <img
                                        alt="avatar"
                                        :src="userData.avatar"
                                        />
                                    </a-avatar>
                                    </template>
                                </a-comment>
                                
                            </a-tab-pane>
                        </a-tabs>
                    </div>
                </a-col>
            </a-col>
            <a-col :span="3"></a-col>
        </a-row>
        <a-row>
            <Footer style="margin-top: 30px;"></Footer>
        </a-row>
    </div>
</template>
 
<style scoped lang="less">
  .profile{
    width: 100vw;
    height: 100vh;
    overflow: auto;
    background-color: rgb(233, 235, 237);
    .datetime{
        color: var(--color-text-3);
        font-size: 12px;
    }
    .edit-article{
        display: block;
        right: 10px;
        bottom: 50px;
        position: absolute;
        padding: 3px 10px;
        background: rgb(60, 125, 255);
        color: #fff;
        border-radius: 3px;
    }
    .delete-article{
        display: block;
        right: 10px;
        bottom: 95px;
        position: absolute;
        padding: 3px 10px;
        background: rgb(247, 101, 96);
        color: #fff;
        border-radius: 3px;
    }
    :deep(.arco-list-item-meta){
        align-items: start;
    }
    .profile-num{
        font-weight: 600;
        margin-right: 5px;
        font-size: 20px;
        line-height: 24px;
        color: #222226;
    }
    .profile-text{
        color: #555666;
        font-size: 14px;
        line-height: 16px;
        margin-top: 2px;
        white-space: nowrap;
    }
    .header-row{
        background-color: rgb(0, 139, 139);
    }
    .whitebg { 
        background: #fff; 
        border-radius: 3px; 
        padding: 20px; 
        margin-bottom: 20px; 
        overflow: hidden; 
    }
    .divider-profile {
        box-sizing: border-box;
        margin-top: 8px;
        width: 560px;
    }
    .arco-layout-header{
      background-color: rgb(181, 186, 206);
    }
    
    .avatar-small{
        width: 45px;
        height:45px;
        border-radius: 50%;
        position: relative;
        top:12px
    }
    .blogtitle {
        margin: 0 0 10px 0; 
        font-size: 18px; 
        overflow: hidden; 
    }
    .bloglist>ul>li:hover .blogtitle a {
        color: #337ab7; 
        }
    .blogtitle b { 
        color: #a9a9a9
    }
    li{
        position: relative;
    }
    a.viewmore { 
        display: block; 
        right: 10px; 
        bottom: 5px; 
        position: absolute; 
        padding: 3px 10px; 
        background: #12b7de; 
        color: #fff; 
        border-radius: 3px; 
    }
    .blogpic { 
        float: left; 
        // width: 23.2%; 
        margin-right: 20px; 
        display: block; 
        overflow: hidden; 
        border-radius: 3px; 
        position: relative 
    }
    .blogpic i { 
        display: block; 
        position: absolute; 
        top: 0; left: 0; 
        z-index: 9; 
        font-style: normal; 
        padding: 3px 5px; 
        background: rgba(18,182,221,.8); 
        font-size: 14px; 
    }
    .blogpic i a { 
        color: #FFF 
    }
    .bplist { 
        display: block; 
        overflow: hidden 
    }
    .bplist a { 
        display: block; 
        float: left; 
        width: 25%; 
        overflow: hidden 
    }
    .bplist a img { 
        border-radius: 3px;
        width: 200px; 
        height: 140px; 
        -moz-transition: all .5s ease;
        -webkit-transition: all .5s ease; 
        -ms-transition: all .5s ease; 
        -o-transition: all .5s ease; 
        transition: all .5s ease; 
        transition: all 0.5s; 
    }
    .bplist a img:hover { 
        transform: scale(1.05) 
    }
    .blogtext { 
        font-size: 14px; 
        color: #666; 
        overflow: hidden; 
        padding-right: 120px;
        text-overflow: ellipsis; 
        -webkit-box-orient: vertical; 
        display: -webkit-box; 
        -webkit-line-clamp: 3; 
        margin-top: 20px 
    }
    .bloginfo { 
        margin-top: 20px; 
        overflow: hidden; 
        color: #999; 
        line-height: 34px; 
    }
    .bloginfo span { 
        margin: 0 5px 
    }
    .bloginfo span a { 
        color: #096 
    }
    
}

.form{
      width: 320px;
      margin: 0 auto;
      ul{
        text-align: center;
        li{
          width: 320px;
          min-height: 20px;
          margin-top: 24px;
          :deep(.arco-input-wrapper){   //修改arco的input样式
            width: 100%;
            height: 45px;
            //color: #FFF;
            border: 1px solid #e3e8f0;
            border-radius: 25px;
            background-color: rgba(0,0,0,0);
            padding: 0 20px;
            :deep(.arco-input-prefix){
              svg{
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
          .to-login{
            color: rgba(0,118,254,1);
          }
          .register-btn{
            width: 100%;
            height: 50px;
            line-height: 50px;
            color: #FFFFFF;
            font-size: 20px;
            letter-spacing: 15px;
            border: none;
            border-radius: 25px;
            background-color: rgb(0, 118, 254);

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
    }
</style>