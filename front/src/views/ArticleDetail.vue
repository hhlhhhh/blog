<script setup>
import ClickRank from '../components/ClickRank.vue';
import Tag from '../components/Tag.vue';
import FriendLink from '../components/FriendLink.vue';
import { ref,reactive,onMounted ,watch} from 'vue'
import {getArticleById,articleViewApi,getTagByArticleId} from '@/http/api/article.js'
import {getCommentListByParentId, getCommentListByArticleId,sendCommentApi} from '@/http/api/comment.js'
import { useRoute ,useRouter} from 'vue-router';
import {dealTime} from '../utils/index.js'
import {UserStore,ArticleStore} from '@/store/index.js'

const router = useRouter()
const route = useRoute()
const userStore = UserStore()
const articleStore = ArticleStore()

const article = ref({})
const user = ref({})
const tagList = ref([])
const commentList = ref([{user:{}}])
const commentObj = reactive({
    content:"",
    articleId:null,
    parentId:null,
    userId:null,
    comment:null,
    topParentComment:null
})
const loadMore = ref(true)  //是否还有更多的评论
const pageView = reactive({
    size:5,
    current:1
})
const commentArea = ref(null)
const commentAreaPlaceholder = ref("留下你精彩的评论吧")

const searchTagArticle = (tag) => {
    articleStore.setTagSearchFlag(true)
    articleStore.articlePageView.keyword = "##"+tag.name
    let r_name = route.name
    if((r_name != 'Home')&&(r_name!="Channel")){
        router.push({name:'Home'})
    }
}

/**
 * 加载顶层评论
 */
const loadMoreTopComment = () => {
    pageView.current++
    getCommentListByArticleId(article.value.id,pageView).then(({data}) => {
        if(data.data.records.length === 0){
            pageView.current--
            loadMore.value = false
            return
        }
        if(data.data.records.length < pageView.size){
            loadMore.value = false
        }
        commentList.value.push(...data.data.records)
    })
}

/**
 * 加载子评论
 */
const loadMoreComment = async (comment) => {
    if(comment.fold){
        comment.fold = false
        comment.count = 0
        return
    }
   
    if(!comment.commentList|| comment.commentList.length == 0){
        Reflect.set(comment,"pageView",{
            "size":5,
            "current":1
        }) 
        comment.commentList = []
    }else comment.pageView.current++
    
    const {data} = await getCommentListByParentId(comment.id,{...comment.pageView})
    if(data.data.records.length === 0){
        comment.pageView.current--
        return
    }
    comment.commentList.push(...data.data.records)
    comment.fold = false
    comment.count-=data.data.records.length
}

/**
 * 折叠评论
 */
const foldComment = (comment) => {
    comment.fold = true
    comment.count = comment.commentList.length
}

/**
 * 回复按钮触发事件
 */
const reply = (comment,topParentComment) => {
    commentArea.value.focus()
    commentAreaPlaceholder.value = `回复 @${comment.user.nickname}`
    commentObj.parentId = comment.id
    commentObj.topParentComment = topParentComment
    commentObj.comment = comment
}

/**
 * 评论事件
 */
const sendComment = () => {
    if(commentObj.content.trim() === "")return
    sendCommentApi(commentObj).then(async ({data}) => {
        commentObj.content = ""
        commentObj.parent_id = null
        commentAreaPlaceholder.value = "留下你精彩的评论吧"
        commentArea.value.focus()
        if(commentObj.topParentComment){
            if(commentObj.topParentComment.fold){   //如果是折叠状态展开就好
                commentObj.topParentComment.fold = false
                commentObj.topParentComment.count = 0
            }else{  //如果没展开，看是否有子评论，有就加一，没有就初始化
                if(!commentObj.topParentComment.commentList||commentObj.topParentComment.commentList.length === 0){
                    await loadMoreComment(commentObj.topParentComment)
                    // commentObj.topParentComment.count--
                    for (let index = 0; index < commentObj.topParentComment.commentList.length; index++) {
                        const element = commentObj.topParentComment.commentList[index];
                        if(element.id == data.data.id){ //排除刚添加的评论
                            commentObj.topParentComment.commentList.splice(index,1)
                            commentObj.topParentComment.count++
                            break
                        } 
                    }
                }
            }
            commentObj.topParentComment.commentList.unshift(data.data)
        }else{
            commentList.value.unshift(data.data)
        }
        commentObj.comment = null
        commentObj.topParentComment = null

    })
}
/**
 * 清除评论信息
 */
const clearCommentObj = () => {
    commentObj.content = ""
    commentObj.parentId = null
    commentObj.comment = null
    commentObj.topParentComment = null
}


const init = () => {
    getCommentListByArticleId(article.value.id,pageView).then(({data}) => {
        if(data.data.records.length < pageView.size){
            loadMore.value = false
        }
        commentList.value = data.data.records
    })
    commentObj.userId = userStore.user.id
    commentObj.articleId = article.value.id
    
    getTagByArticleId(article.value.id).then(({data}) => {
        tagList.value = data.data
    })

    articleViewApi(article.value.id)
}

//观察路由参数变化，切换文章内容
watch(() => route.params.id,async () => {
    let articleId = route.params.id
    const {data} = await getArticleById(articleId)
    article.value = data.data
    user.value = article.value.user
    init()
})

onMounted(async () => {
    let articleId = route.params.id
    const {data} = await getArticleById(articleId)
    article.value = data.data
    user.value = article.value.user
    init()
})

</script>

<template>
    <div class="article-detail-container">
        <div class="lbox">
            <div class="content_box whitebg">
                <h2 class="htitle"><span class="con_nav">您现在的位置是：<a href="/">首页</a>&gt;<a href="/">{{article.title}}</a></span>正文</h2>
                <h1 class="con_tilte">{{article.title}}</h1>
                <p class="bloginfo"><i class="avatar"><img class="avatar-small" :src="user.avatar"></i><span>{{user.nickname}}</span><span>{{dealTime(article.updateTime)}}</span><span>{{article.articleView}}人已观看</span></p>
                <p class="con_info"><b>摘要</b>{{article.summary}}</p>
                <div class="con_text">
                    <v-md-preview :text="article.content"></v-md-preview>
                </div>
            </div>

            <div class="whitebg cloud">
                <ul>
                    <a  @click="searchTagArticle(item)" v-for=" (item,index) in tagList" :key="index" target="_blank" >{{item.name}}</a>
                </ul>
            </div>

            <div id="comments" class="comments-area whitebg" >
                <a-comment
                v-for="comment in commentList"
                :key="comment.id"
                :avatar="comment.user.avatar"
                :content="comment.content"
                :datetime="dealTime(comment.createTime)"
                >
                    <template #author>
                        <a class="comment-name" style="cursor: pointer;" @click="router.push({path:'/other',query:{uid:comment.user.id}})">{{comment.user.nickname}}</a>
                    </template>
                    <template #actions>
                        <span @click="reply(comment,comment)" class="action cursor-pointer"> <IconMessage /> 回复 </span>
                    </template>
                    <div class="comment-children">
                        <a-comment
                            v-show="!comment.fold"
                            v-for="secondary in comment.commentList"
                            :avatar="secondary.user.avatar"
                            :content="secondary.content"
                            :datetime="dealTime(secondary.createTime)"
                            >
                            <template #author>
                                <a class="comment-name" style="cursor: pointer;" @click="router.push({path:'/other',query:{uid:secondary.user.id}})">{{secondary.user.nickname}}</a>
                                &nbsp;<icon-caret-right style="width: 15px;" />&nbsp;&nbsp;
                                <a class="comment-name" style="cursor: pointer;" @click="router.push({path:'/other',query:{uid:secondary.parentComment.user.id}})">{{secondary.parentComment.user.nickname}}</a>
                            </template>
                            <template #actions>
                            <span @click="reply(secondary,comment)" class="action cursor-pointer"> <IconMessage /> 回复 </span>
                            </template>
                        </a-comment>
                        <div v-if="comment.count!=0||(comment.commentList&&comment.commentList.length>0)">
                            <div @click="loadMoreComment(comment)" v-if="comment.count!=0" class="cursor-pointer">
                            <a-divider orientation="left">
                                展开{{comment.count}}条回复
                                <icon-down />
                            </a-divider>
                            </div>
                            <div @click="foldComment(comment)" v-else class="cursor-pointer">
                                <a-divider orientation="left">
                                    折叠
                                    <icon-up />
                                </a-divider>
                            </div>
                        </div>
                    </div>
                </a-comment>
                <div style="text-align: center;margin-top: 20px;">
                    <a-button v-if="loadMore" type="dashed" @click="loadMoreTopComment">加载更多</a-button>
                    <span v-else style="color: #888888;">没有更多评论了</span>
                </div>

                <a-divider></a-divider>

                <div class="comment-reply" v-if="article.commentStatus==1">
                    <a-comment
                        align="right"
                        avatar="https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/3ee5f13fb09879ecb5185e440cef6eb9.png~tplv-uwbnlip3yd-webp.webp"
                        >
                        <template #actions>
                            <a-button key="0" type="secondary" @click="clearCommentObj" > 取消 </a-button>
                            <a-button key="1" type="primary" @click="sendComment"> 评论 </a-button>
                        </template>
                        <template #content>
                            <a-textarea ref="commentArea"  v-model="commentObj.content" :max-length="100" :auto-size="{minRows:3, maxRows:5}" allow-clear show-word-limit :placeholder="commentAreaPlaceholder" />
                        </template>
                    </a-comment>
                </div>
            </div>
        </div>
        <div class="rbox">
            <ClickRank></ClickRank>
            <Tag></Tag>
            <FriendLink></FriendLink>
        </div>
    </div>
</template>

<style scoped lang="less">
.article-detail-container{
    .htitle { 
        font-size: 16px; 
        line-height: 40px; 
        border-bottom: 1px solid #eee; 
        color: #484848; 
        font-weight: normal; 
        position: relative; 
        margin-bottom: 10px; 
        cursor: pointer;
    }
    .htitle:after { 
        content: ""; 
        position: absolute; 
        width: 60px; 
        height: 2px; 
        content: ""; 
        background: #000; 
        left: 0; 
        bottom: 0; 
        -moz-transition: all .5s ease;
        -webkit-transition: all .5s ease; 
        transition: all .5s ease; 
    }
    .htitle:hover:after { 
        width: 80px; 
    }

    .cloud ul a { 
        line-height: 24px; 
        height: 30px; 
        display: block; 
        background: #999; 
        float: left; 
        padding: 3px 10px; 
        margin: 10px 5px 0 0; 
        border-radius: 3px; 
        -moz-transition: all 0.5s; 
        -webkit-transition: all 0.5s; 
        -o-transition: all 0.5s; 
        transition: all 0.5s; 
        color: #FFF 
    }
    .cloud ul a:nth-child(8n-7) { 
        background: #8A9B0F 
    }
    .cloud ul a:nth-child(8n-6) { 
        background: #EB6841 
    }
    .cloud ul a:nth-child(8n-5) { 
        background: #3FB8AF 
    }
    .cloud ul a:nth-child(8n-4) { 
        background: #FE4365 
    }
    .cloud ul a:nth-child(8n-3) { 
        background: #FC9D9A 
    }
    .cloud ul a:nth-child(8n-2) { 
        background: #EDC951 
    }
    .cloud ul a:nth-child(8n-1) { 
        background: #C8C8A9 
    }
    .cloud ul a:nth-child(8n) { 
        background: #83AF9B 
    }
    .cloud ul a:first-child { 
        background: #036564 
    }
    .cloud ul a:last-child { 
        background: #3299BB 
    }
    .cloud ul a:hover { 
        border-radius: 0; 
        text-shadow: #000 1px 1px 1px 
    }
    .comment-name{
        font-size: 14px;
        color: #888888;
    }
    .cursor-pointer{
        cursor: pointer;
    }
    .avatar-small{
        width: 45px;
        height:45px;
        border-radius: 50%;
        position: relative;
        top:12px
    }
    .lbox { 
        width: 75%; 
        float: left; 
        overflow: hidden;
        margin-top: 25px 
    }
    .rbox { 
        width: 23.5%; 
        float: right; 
        overflow: hidden; 
        margin-top: 25px 
    }

    .whitebg { 
        background: #fff; 
        border-radius: 3px; 
        padding: 20px; 
        margin-bottom: 20px; 
        overflow: hidden; 
    }
    .htitle { 
        font-size: 16px; 
        line-height: 40px; 
        border-bottom: 1px solid #eee; 
        color: #484848; 
        font-weight: normal; 
        position: relative; 
        margin-bottom: 10px; 
    }
    .htitle:after { 
        content: ""; 
        position: absolute;
        width: 60px; 
        height: 2px; 
        content: ""; 
        background: #000; 
        left: 0; 
        bottom: 0; 
        -moz-transition: all .5s ease; 
        -webkit-transition: all .5s ease; 
        transition: all .5s ease; 
    }
    .htitle:hover:after { 
        width: 80px; 
    }
    .con_tilte { 
        font-size: 22px; 
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
    .con_info { 
        color: #888888; 
        border: 1px solid #F3F3F3; 
        padding: 10px; 
        margin: 10px auto 0; 
        line-height: 23px; 
        background: none repeat 0 0 #F6F6F6; 
    }
    .con_info b { 
        margin-right: 10px; 
        color: #000; 
    }
    .con_text { 
        line-height: 24px; 
        margin-top: 20px; 
    }
    .con_text a { 
        color: #09C;
        word-break: break-all; 
    }
    .con_text a:hover { 
        text-decoration: underline 
    }
    .con_text p { 
        margin: 15px 0; 
    }
    .con_text img { 
        display: block; 
        max-width: 100% !important; 
        height: 100%!important;
    }
}
</style>