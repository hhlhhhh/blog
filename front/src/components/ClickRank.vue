<script setup>
import { ref,reactive,onMounted } from 'vue'
import { getRankArticleList } from '@/http/api/article.js'
import { useRouter } from 'vue-router'
const router = useRouter()

const articleList = ref([]);

const toArticle = (id) => {
    console.log(id);
    router.push("/detail/"+id)
}

onMounted(async () => {
    const res = await getRankArticleList();
    articleList.value = res.data.data;
})

</script>

<template>
    <div class="whitebg paihang">
        <h2 class="htitle">浏览排行</h2>
        <ul>
            <li v-for="(item,index) in articleList" :key="index">
                <i></i>
                <a @click = "toArticle(item.id)" target="_blank">{{item.title}}</a>
            </li>
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
.paihang ul { 
    overflow: hidden 
}
.paihang ul li { 
    height: 30px; 
    line-height: 30px; 
    margin-bottom: 10px; 
    padding-left: 5px; 
    text-overflow: ellipsis; 
    white-space: nowrap; 
    overflow: hidden; 
}
.paihang ul li:nth-child(even) { 
    background: #f6f6f6 
}
.paihang ul li i { 
    display: block; 
    width: 16px; 
    height: 16px; 
    background: #B1B1B1; 
    float: left; 
    margin-top: 8px; 
    margin-right: 10px; 
    position: relative; 
    font-style: normal 
}
.paihang ul li i::before { 
    position: absolute; 
    left: 0; 
    top: 0; 
    font-size: 9px; 
    color: #fff; 
    line-height: 16px; 
    width: 16px; 
    text-align: center 
}
.paihang ul li:first-child i, .paihang ul li:nth-child(2) i, .paihang ul li:nth-child(3) i { 
    background: #FF6600; 
}
.paihang ul li:first-child i::before { 
    content: "1"; 
}
.paihang ul li:nth-child(2) i::before { 
    content: "2"; 
}
.paihang ul li:nth-child(3) i::before { 
    content: "3"; 
}
.paihang ul li:nth-child(4) i::before { 
    content: "4"; 
}
.paihang ul li:nth-child(5) i::before { 
    content: "5"; 
}
.paihang ul li:nth-child(6) i::before { 
    content: "6"; 
}
.paihang ul li:nth-child(7) i::before { 
    content: "7"; 
}
.paihang ul li:nth-child(8) i::before { 
    content: "8";
 }
 .paihang ul li:nth-child(9) i::before { 
    content: "9";
 }
.paihang ul li:nth-child(10) i::before { 
    content: "10";
}
.paihang ul li:hover { 
    -moz-transition: all .2s ease; 
    -webkit-transition: all .2s ease; 
    transition: all .2s ease; 
}
</style>