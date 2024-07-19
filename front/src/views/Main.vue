<script setup>
import { ref, reactive, onMounted} from  'vue'
import Header from '../components/Header.vue';
import Footer from '../components/Footer.vue';
import {ArticleStore} from '@/store/index.js'
import {getArticleList,getArticleByChannelId} from '@/http/api/article.js';
import { useRoute } from 'vue-router';

const route = useRoute()
const articleStore = ArticleStore()

const articlePageView = articleStore.articlePageView

const mainContainer = ref(null)

//懒加载数据
const scrollGetArticle = function(){
    //获取div元素的引用
    let dom = mainContainer.value
    console.log([dom]);
    const that = this;
    // 监听div元素的滚动事件
    dom.onscroll =  () => {
        console.log(route.name);
        const {scrollHeight, offsetHeight, scrollTop} = dom;
        // console.log(scrollHeight, offsetHeight,scrollTop);
        
        const distance = scrollHeight- offsetHeight - scrollTop;
        if (!articleStore.noMore&&distance <= 800) {
            articlePageView.current++;
            if(route.name === 'Channel'){
                let channelId = route.params.id;
                getArticleByChannelId(channelId,articlePageView).then(res =>{  
                    if(res.data.data.records.length === 0){
                        //没数据了，就等一分钟再请求
                        articleStore.setNoMore(true)
                        articlePageView.current--;
                        setTimeout(() => {
                            articleStore.setNoMore(false)
                        }, 60000);
                        return;
                    }
                    articleStore.addArticleList(res.data.data.records)
                });
            }else{
                getArticleList(articlePageView).then(res =>{  
                    if(res.data.data.records.length === 0){
                        //没数据了，就等一分钟再请求
                        articleStore.setNoMore(true)
                        articlePageView.current--;
                        setTimeout(() => {
                            articleStore.setNoMore(false)
                        }, 60000);
                        return;
                    }
                    articleStore.addArticleList(res.data.data.records)
                });
            }
        }
    }
}

onMounted(() => {
    scrollGetArticle();
})

</script>
<template>
    <div class="main-container"  ref="mainContainer">
            <a-row class="header-row">
                <a-col :span="3"></a-col>
                <a-col :span="18">
                    <Header></Header>
                </a-col>
                <a-col :span="3"></a-col>
            </a-row>
            <a-row>
                <a-col :span="4"></a-col>
                <a-col :span="16">
                    <a-col>
                        <router-view></router-view>
                    </a-col>
                </a-col>
                <a-col :span="4"></a-col>
            </a-row>
            <a-row>
                <Footer style="margin-top: 30px;"></Footer>
            </a-row>
    </div>
</template>

<style scoped>

.main-container{
    width: 100vw;
    height: 100vh;
    overflow: auto;
    background-color: rgb(233, 235, 237);
    .header-row{
        background-color: rgb(0, 139, 139);
    }
}

</style>
