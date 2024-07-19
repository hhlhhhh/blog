<script setup>
import { ref,reactive,onMounted } from 'vue';
import {useRoute} from 'vue-router'
import {getArticleById,saveArticleApi,uploadImageApi,getTagByArticleId} from '@/http/api/article.js'
import {getAllChannelList} from '@/http/api/channel.js'
import { Message } from '@arco-design/web-vue';

const route = useRoute()

const text = ref('')

const article = reactive({
    id:null,
    title:"",
    summary:"",
    content:"",
    titleImg:"",
    commentStatus:1,
    channelId:null,
    tagList:[],
    tags:[]
})

const channelList = ref([])

const loadImageData = reactive({
    file: []
})

// const changeUpdateImageState = (state) => {
//     loadImageData.isUpdate = state
// }
//上传照片
const uploadImage = (list) => {
    uploadImageApi(list[0]).then(res => {
        if(res.data.state == "SUCCESS"){
            article.titleImg = res.data.data
        }
    })
}

const handleClick = () =>{
    article.tagList.length = 0
    article.tags.forEach(item =>{
        article.tagList.push({
            name:item
        })
    })
    saveArticleApi(article).then(res => {
        if(res.data.state == "SUCCESS"){
            Message.success("保存成功！")
        }else{
            Message.error("保存失败！")
        }
    })
}

const init = () => {
    let articleId = route.query.article_id
    if(articleId){
        getArticleById(articleId).then(res => {
            if(res.data.state == "SUCCESS"){
                let data = res.data.data
                article.id = data.id
                article.title = data.title
                article.summary = data.summary
                article.content = data.content
                article.titleImg = data.titleImg
                article.commentStatus = data.commentStatus
                article.channelId = data.channelId
                getTagByArticleId(articleId).then(res => {
                    if(res.data.state == "SUCCESS"){
                        let data = res.data.data
                        article.tagList = data
                        article.tagList.forEach(item => {
                            article.tags.push(item.name)
                        })
                    }
                })  
            }
        })
    }
    getAllChannelList().then(({data})=>{
        channelList.value = data.data
    })
}

onMounted(() => {
    init()
})

</script>
<template>
    <a-form class="whitebg" style="margin-top: 20px;" ref="formRef" :model="article" >
        <a-form-item label="封面">
            <a-upload
                :file-list="loadImageData.file"
                @change="uploadImage"
                :show-file-list="false"
                :auto-upload="false"
                >
                <template #upload-button>
                    <a-avatar trigger-type="mask" :size="100">
                    <img
                        alt="avatar"
                        style="width: 150px;height: 150px;"
                        :src="article.titleImg"
                        />
                        <template #trigger-icon>
                            <IconEdit  />
                        </template>
                    </a-avatar> 
                </template>
            </a-upload>
        </a-form-item>
        <a-form-item label="标题">
            <a-input v-model="article.title" placeholder="请输入标题" />
        </a-form-item>
        <a-form-item label="摘要">
            <a-textarea v-model="article.summary" placeholder="请输入摘要" />
        </a-form-item>
        <a-form-item label="专栏">
            <a-select v-model="article.channelId" :style="{width:'320px'}" placeholder="请选择专栏" allow-search>
                <a-option :value="channel.id" :key="channel.id" v-for="channel in channelList">{{ channel.name }}</a-option>
            </a-select>
        </a-form-item>
        <a-form-item label="标签">
            <a-input-tag  v-model="article.tags" :style="{width:'380px'}" placeholder="请输入标签" :max-tag-count="5" allow-clear/>
        </a-form-item>
        <a-form-item label="是否允许评论">
            <a-switch v-model="article.commentStatus" :unchecked-value="0" :checked-value="1" type="round">
                <template #checked>
                    ON
                </template>
                <template #unchecked>
                    OFF
                </template>
            </a-switch>
        </a-form-item>
        <a-form-item>
            <a-button @click="handleClick">保存</a-button>
        </a-form-item>
    </a-form>
    <v-md-editor v-model="article.content" height="800px"></v-md-editor>
</template>

<style scoped>
.whitebg { 
    background: #fff; 
    border-radius: 3px; 
    padding: 20px; 
    margin-bottom: 20px; 
    overflow: hidden; 
}
</style>