<script setup>
import ClickRank from '../components/ClickRank.vue';
import Notice from '../components/Notice.vue';
import Tag from '../components/Tag.vue';
import FriendLink from '../components/FriendLink.vue';
import { onMounted ,ref ,watch } from 'vue';
import { useRoute,useRouter } from 'vue-router';
import { ArticleStore } from '../store';
import {getChannelById} from '@/http/api/channel.js'
import { getArticleByChannelId } from '@/http/api/article.js';
import { dealTime } from '../utils';

const route = useRoute();
const router = useRouter();

const channel = ref({})
const channelData = ref([])

const articleStore = ArticleStore()
const articleList = ref([])
const articlePageView = ref({})

const init = async () => {
    articleStore.setNoMore(false)
    articlePageView.value.size = 5
    articlePageView.value.current = 1
    let channelId = route.params.id
    const {data} = await getChannelById(channelId)
    channel.value = data.data
    console.log(channel.value);
    channelData.value.length = 0
    channelData.value.push(channel.value)

    //去加载文章数据
    getArticleByChannelId(channelId,articlePageView.value).then(({data})=>{
        articleStore.setArticleList(data.data.records)
    })
}

//监视搜索
watch(()=>articleStore.searchFlag,()=>{
    if(articleStore.searchFlag){
        articleList.value = articleStore.articleList
    }else{
        init()
        console.log("init");
    }
})

//观察路由参数变化，切分类内容
watch(() => route.params.id,async () => {
    init()
})

onMounted(() => {
    articleList.value = articleStore.articleList
    articlePageView.value = articleStore.articlePageView

    if(!articleStore.searchFlag){
        init()
    }
})


</script>
<template>
  <div class="channel-container">
        <a-row>
            <a-col :span="17">
                <div class="channel-title whitebg">
                    <a-collapse :default-active-key="['1']">
                        <a-collapse-item :header="channel.name" key="1">
                            <a-list
                                class="list-demo-action-layout"
                                :bordered="false"
                                :data="channelData"
                            >
                                <template #item="{ item }">
                                <a-list-item class="list-demo-item" action-layout="vertical">
                                    <template #actions>
                                        <span><icon-schedule /> {{dealTime(item.createTime)}}</span>
                                    </template>
                                    <a-list-item-meta>
                                    <template #description>
                                        <div style="width: 100%;">
                                            <div className="image-area" style="display: flex;">
                                                <img style="max-height: 180px; max-width: 180px; margin: 0 10px 10px 0;"  alt="arco-design" :src="item.channelImg" />
                                                <div style="text-indent: 2em;">{{ item.summary }}</div>
                                            </div>
                                            <div class="con_text">
                                                <v-md-preview :text="item.content"></v-md-preview>
                                            </div>
                                        </div>
                                    </template>
                                    </a-list-item-meta>
                                </a-list-item>
                                </template>
                            </a-list>
                        </a-collapse-item>
                    </a-collapse>
                </div>
                <a-card title="模块文章">
                    <ul>
                        <li v-for="(item,index) in articleList" :key="item.id">
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
                                <span >{{item.user.nickname}}</span><span>{{dealTime(item.createTime)}} </span></p>
                            <router-link :to='"/detail/"+item.id' target="_blank"  class="viewmore"> 阅读更多</router-link >
                        </li>
                        <a-empty v-if="articleList.length==0"></a-empty>
                    </ul>
                </a-card>
            </a-col>
            <a-col :span="1"></a-col>
            <a-col :span="6">
                <Notice></Notice>
                <ClickRank></ClickRank>
                <Tag></Tag>
                <FriendLink></FriendLink>
            </a-col>
        </a-row>
  </div>
</template>

<style scoped lang="less">
.channel-container{
    :deep(.arco-list-item-meta){
        display:block
    }
    :deep(.arco-list-item){
        display: block;
    }
    :deep(.arco-collapse-item-content){
        background-color: #fff;
    }
    :deep(.arco-collapse){
        border-radius: 8px;
    }
    .con_text { 
        width: 100%;
        line-height: 24px; 
        margin-top: 20px; 
    }
    .channel-title{
        margin-top: 20px;
    }
    .whitebg { 
        background: #fff; 
        border-radius: 3px; 
        padding: 20px; 
        margin-bottom: 20px; 
        overflow: hidden; 
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
        width: 23.2%; 
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
