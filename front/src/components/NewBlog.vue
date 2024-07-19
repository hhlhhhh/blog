<script setup>
import { ref ,onMounted ,reactive ,watch } from 'vue';
import {getTopArticle, getArticleList} from '../http/api/article.js';
import {ArticleStore} from '../store/index.js'
import {dealTime} from '../utils/index.js'
import { useRouter } from 'vue-router';

const router = useRouter()


const articleStore = ArticleStore()

const articleTopList = ref([])
const articleList = ref([])
const articlePageView = ref({})


const init = ()=>{
    articleStore.setNoMore(false)
    articlePageView.value.size = 5
    articlePageView.value.current = 1
    getTopArticle().then(res=>{
        articleStore.setTopArticle(res.data.data)
    })
    getArticleList(articlePageView.value).then(res=>{
        articleStore.setArticleList(res.data.data.records)
    })
}

//监视搜索
watch(()=>articleStore.searchFlag,()=>{
    if(articleStore.searchFlag){
        articleList.value = articleStore.articleList
        articlePageView.value = articleTopList.articlePageView
        // articleTopList.value = articleStore.topArticle
        articleTopList.value = []
        console.log(55);
    }else{
        init()
        console.log("init");
    }
})

onMounted(()=>{
    articleList.value = articleStore.articleList
    articleTopList.value = articleStore.topArticle
    articlePageView.value = articleStore.articlePageView
    console.log(515);
    console.log(articleStore.tagSearchFlag);
    //如果不在搜索，才加载
    if(!articleStore.searchFlag){
        init()
        console.log("init");
    }
})

</script>
<template>
    <div class="new-blog-container" >
        <a-card :style="{ width: '100%' }" title="最新文章">
            <ul>
                <li v-for="(item,index) in articleTopList" :key="item.id">
                    <a-divider v-if="index!=0" class="half-divider" />
                    <h3 class="blogtitle"><router-link :to='"/detail/"+item.id'  arget="_blank"><b>【置顶】</b>{{item.title}}</router-link></h3>
                    <span class="blogpic imgscale"><i><router-link :to='"/detail/"+item.id'>{{item.channel.name}}</router-link></i>
                        <router-link :to='"/detail/"+item.id' target="_blank">
                            <img v-if="item.titleImg==null ||item.titleImg=='' " width="130px" height="120px" src="https://img-blog.csdnimg.cn/direct/997f0d4c60774a09887a64fab2d8300a.jpeg?x-oss-process=image/resize,m_fixed,h_300,image/format,png" alt="">
                            <img v-else  width="130px" height="130px"  :src="item.titleImg" alt="">
                        </router-link>
                    </span>
                    <p class="blogtext">{{item.summary}}</p>
                    <p class="bloginfo" style="cursor: pointer;" @click="router.push({path:'/other',query:{uid:item.user.id}})">
                        <i class="avatar"><img class="avatar-small" :src="item.user.avatar"></i>
                        <span>{{item.user.nickname}}</span><span>{{dealTime(item.createTime)}}</span>
                    </p>
                    <router-link :to='"/detail/"+item.id' target="_blank"  class="viewmore"> 阅读更多</router-link >
                </li>
                
                <li v-for="(item,index) in articleList" :key="item.id" style="min-height: 180px;">
                    <a-divider v-if="index!=0||articleTopList.length!=0" class="half-divider" />
                    <h3 class="blogtitle"><router-link :to='"/detail/"+item.id'  arget="_blank">{{item.title}}</router-link></h3>
                    <span class="blogpic imgscale"><i><router-link :to='"/detail/"+item.id'>{{item.channel.name}}</router-link></i>
                        <router-link :to='"/detail/"+item.id' target="_blank">
                            <img v-if="item.titleImg==null ||item.titleImg=='' " width="130px" height="120px" src="https://img-blog.csdnimg.cn/direct/997f0d4c60774a09887a64fab2d8300a.jpeg?x-oss-process=image/resize,m_fixed,h_300,image/format,png" alt="">
                            <img v-else  width="130px" height="130px"  :src="item.titleImg" alt="">
                        </router-link>
                    </span>
                    <p class="blogtext">{{item.summary}}</p>
                    <p class="bloginfo" style="cursor: pointer;" @click="router.push({path:'/other',query:{uid:item.user.id}})">
                        <i class="avatar"><img class="avatar-small" :src="item.user.avatar"></i>
                        <span>{{item.user.nickname}}</span><span>{{dealTime(item.updateTime)}} </span>
                    </p>
                    <router-link :to='"/detail/"+item.id' target="_blank"  class="viewmore"> 阅读更多</router-link >
                </li>
            </ul>
        </a-card>
    </div>
</template>

<style scoped lang="less">
.new-blog-container {
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

</style>
