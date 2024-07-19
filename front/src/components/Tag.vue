<script setup>
import { ref,reactive,onMounted } from 'vue'
import { getHeadTagList } from '@/http/api/tag.js'
import { useRouter , useRoute } from 'vue-router'
import {ArticleStore} from '@/store/index.js'

const articleStore = ArticleStore();
const router = useRouter();
const route = useRoute();

const tagList = ref([]);

const searchTagArticle = (tag) => {
    articleStore.setTagSearchFlag(true)
    articleStore.articlePageView.keyword = "##"+tag.name
    let r_name = route.name
    if((r_name != 'Home')&&(r_name!="Channel")){
        router.push({name:'Home'})
    }
}


onMounted(async () => {
    const res = await getHeadTagList();
    tagList.value = res.data.data;
})
</script>

<template>
    <div class="whitebg cloud">
        <h2 class="htitle">标签云</h2>
        <ul>
           <a  v-for=" (item,index) in tagList" :key="index" @click="searchTagArticle(item)" >{{item.name}}</a>
        </ul>
    </div>
</template>

<style scoped>
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
</style>