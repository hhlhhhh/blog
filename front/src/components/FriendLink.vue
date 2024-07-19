<script setup>
import { ref,reactive,onMounted } from 'vue'
import { getHeadFriendLinkList } from '@/http/api/friendLink.js'
const friendLinkList = ref([]);

onMounted(async () => {
    const res = await getHeadFriendLinkList();
    friendLinkList.value = res.data.data;
})

</script>
<template>

    <div>
        <div class="links whitebg">
            <h2 class="htitle"><span class="sqlink"></span>友情链接</h2>
            <ul>
                <li v-for="(item,index) in friendLinkList" :key="index">
                    <a :href="item.url" target="_blank">{{item.title}}</a>
                </li>
            </ul>
        </div>
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
.links ul { 
    overflow: hidden; 
    margin-top: 20px 
}
.links li { 
    display: inline-block; 
    width: 50%; 
    float: left; 
    line-height: 30px; 
    text-align: center 
}
.links li a { 
    display: block; 
}
.links li:hover { 
    background: #f7f7f7 
}

</style>