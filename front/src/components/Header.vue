<script setup>
import { ref ,onMounted ,watch } from 'vue';
import {getNavChannelList} from '../http/api/channel.js';
import {ChannelStore,ArticleStore,UserStore} from '../store/index.js'
import {getArticleList,getArticleByChannelId} from '@/http/api/article.js'
import {logoutApi} from "@/http/api/user.js"
import {useRouter,useRoute} from 'vue-router'

const router = useRouter()
const route = useRoute()


const articleStore = ArticleStore()
const channelStore = ChannelStore()
const userStore = UserStore()

const navChannelList = ref([])

const pageView = ref({})
 
const searchArticle = async ()=>{
    pageView.value.keyword = pageView.value.keyword.trim()
    let channelId = route.params.id
    if(pageView.value.keyword){
        pageView.value.size = 5
        pageView.value.current = 1
        
        let res = {}
        if(channelId){
            res = await getArticleByChannelId(channelId,pageView.value)
        }else res = await getArticleList(pageView.value)

        articleStore.setArticleList(res.data.data.records)
        articleStore.setTopArticle([])
        articleStore.setSearchFlag(true)
        if(!channelId){
            router.push("/")
        }
    }else{
        articleStore.setSearchFlag(false)
        if(!channelId){
            router.push("/")
        }
    }
    articleStore.setNoMore(false)
}


watch(()=>articleStore.tagSearchFlag,()=>{
    if(articleStore.tagSearchFlag){
        console.log("tagSearchFlag");
        searchArticle()
        articleStore.setTagSearchFlag(false)
    }
})


const logout = () => {
    logoutApi().then(({data}) => {
        if(data.state == "SUCCESS"){
            userStore.setUser({})
            userStore.setToken("")
            localStorage.removeItem("user")
            localStorage.removeItem("token")

            router.push("/login")
        }
    })
}

const init = ()=>{
    getNavChannelList().then(res=>{
        navChannelList.value = res.data.data
        channelStore.setNavChannelList(res.data.data)
    })
    pageView.value = articleStore.articlePageView
}


onMounted(() => {
    init()
})

</script>
<template>
    <div class="header-container">
        <a-row>
            <a-col :span="4">
                <h1 class="blog-title">Blog</h1>
            </a-col>
            <a-col :span="10">
                <a-row>
                    <a-col :span="3">
                        <span class="nav-item" @click="articleStore.articlePageView.keyword='';articleStore.setSearchFlag(false);router.push('/')">首页</span>
                    </a-col>
                    <a-col :span="4" v-for="(nav, index) in navChannelList" :key="nav.id">
                        <a-dropdown position="bottom" trigger="hover">
                            <a-button class="nav-item-button" @click="router.push('/channel/'+nav.id)">{{nav.name}}</a-button>
                            <template #content v-if="nav.channelList&&nav.channelList.length>0">
                                <a-doption v-for="(subNav) in nav.channelList" :key="subNav.id">
                                    <a-button @click="router.push('/channel/'+subNav.id)">{{subNav.name}}</a-button>
                                </a-doption>
                            </template>
                        </a-dropdown>
                    </a-col>
                </a-row>
            </a-col>
            <a-col :span="8">
                <a-input-search  placeholder="请输入关键字" v-model="pageView.keyword" @search="searchArticle"  class="search-icon" search-button :button-props="{type: 'text'}">
                    <template #button-icon >
                        <icon-search/>
                    </template>
                </a-input-search>
            </a-col>
            <a-col :span="2">
                <a-trigger
                    v-if="userStore.token"
                    :trigger="['click', 'hover']"
                    :style="{textAlign:'center'}"
                    clickToClose
                    position="top"
                    v-model:popupVisible="popupVisible"
                    >
                    <div :class="`button-trigger ${popupVisible ? 'button-trigger-active' : ''}`">
                        <a-avatar :size="60" >
                            <img
                                :alt="userStore.user.nickname"
                                :src="userStore.user.avatar"
                                />
                        </a-avatar>
                        
                    </div>
                    <template #content>
                        <a-menu>
                            <a-menu-item key="1">
                                <span @click="router.push('/profile')">个人信息</span>
                            </a-menu-item>
                            <a-menu-item key="2">
                                <span @click="logout">退出登录</span>
                            </a-menu-item>
                        </a-menu>
                    </template>
                </a-trigger>
                <div v-else>
                    <a class="login-button" @click = "router.push('/login')">
                        登录
                    </a>
                </div>
            </a-col>
        </a-row>
    </div>
</template>

<style scoped lang="less">
.header-container{
    text-align: center;
    .login-button{
        display: inline-block; 
        color: #222226; 
        width: 50px;
        height:50px;
        line-height: 50px; 
        border-radius: 50%;
        background-color: #e8e8ed;
    }
    .blog-title{
        color: aliceblue;
    }
    .arco-row{
        text-align: center;
        line-height: 70px;
    }
    .nav-item{
        color: aliceblue;
        text-align: center;
        font-size: 16px;
        cursor: pointer;
    }
    .nav-item-button{
        background-color: rgb(0, 139, 139);
        color: aliceblue;
        border: none;
        padding: 5px 10px;
        font-size: 14px;
        cursor: pointer;
    }
    .search-icon{
        .arco-btn{
            color: aliceblue;
            background-color: #fff;
        }
    }
    h1{
        margin: 0;
    }
}
</style>
