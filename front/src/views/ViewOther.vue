<script setup>
import Footer from "@/components/Footer.vue";
import Header from "@/components/Header.vue";
import {ref ,onMounted,reactive} from "vue";
import { UserStore } from "../store";
import {getUserInfoByIdApi, getUserProfileShow} from "@/http/api/user.js"
import {getSelfArticleApi} from "@/http/api/article.js"
import {dealTime} from "@/utils/index.js"
import {useRoute,useRouter} from 'vue-router'

const userStore = UserStore()

const route = useRoute()
const router = useRouter()

const userDataArr = ref([])

const showProfile = ref({})

const article = ref([])



const init = async () => {
    let uid = route.query.uid
    if(userStore.user.id==uid){
        router.push('/profile')
    }
    const {data} = await getUserInfoByIdApi(uid);
    userDataArr.value.push(data.data)
    getUserProfileShow(uid).then(res => {
        showProfile.value = res.data.data
    })
    getSelfArticleApi(uid).then(res => {
        article.value = res.data.data
    })
}

onMounted(() => {
    init();
})


</script>

<template>
    <div class="profile">
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
                                <a-list-item-meta>
                                <template #title>
                                    <div>
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
                                                    <span>{{ item.profile }}</span>
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </template>
                                <template #avatar>
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
                                </a-list-item-meta>
                            </a-list-item>
                            </template>
                        </a-list>
                    </div>
                    <div class="whitebg">
                        <a-tabs default-active-key="1">
                            <a-tab-pane key="1" title="文章">
                                <ul style="margin-top: 10px;">
                                    <li v-for="(item,index) in article" :key="item.id">
                                        <a-divider v-if="index!=0"></a-divider>
                                        <h3 class="blogtitle"><router-link :to='"/detail/"+item.id'  arget="_blank">{{item.title}}</router-link></h3>
                                        <span class="blogpic imgscale"><i><router-link :to='"/detail/"+item.id'>{{item.channel.name}}</router-link></i>
                                            <router-link :to='"/detail/"+item.id' target="_blank">
                                                <img v-if="item.titleImg==null ||item.titleImg==''" width="130px" height="120px" src="https://img-blog.csdnimg.cn/direct/997f0d4c60774a09887a64fab2d8300a.jpeg?x-oss-process=image/resize,m_fixed,h_300,image/format,png" alt="">
                                                <img v-else  width="130px" height="130px"  :src="item.titleImg" alt="">
                                            </router-link>
                                        </span>
                                        <p class="blogtext">{{item.summary}}</p>
                                        <p class="bloginfo"  style="cursor: pointer;" @click="router.push({path:'/other',query:{uid:item.user.id}})">
                                            <i class="avatar"><img class="avatar-small" :src="item.user.avatar"></i>
                                            <span>{{item.user.nickname}}</span><span>{{dealTime(item.createTime)}} </span></p>
                                            <router-link :to='"/detail/"+item.id' target="_blank"  class="viewmore"> 阅读更多</router-link >
                                    </li>
                                    <a-empty v-if="article.length==0"></a-empty>
                                </ul>
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