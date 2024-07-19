<script setup>
import { ref,reactive,onMounted } from 'vue'
import { getShowChannelList } from '@/http/api/channel.js'
import { useRouter } from 'vue-router';
const router = useRouter();
const channelList = ref([]);

const colors = ['red','orangered','orange','gold','lime','green','cyan','blue','arcoblue','purple','pinkpurple','magenta','gray'];

onMounted(async () => {
    const res = await getShowChannelList("B");
    channelList.value = res.data.data;
})
</script>

<template>
    <div class="whitebg cloud" style="margin-top: 20px;">
        <ul>
            <a-tag @click="router.push('/channel/'+item.id)" v-for="(item,index) in channelList" :key="item.id" :color="colors[(Math.round(Math.random()*13))]">
                {{ item.name }}
            </a-tag>
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
.cloud ul span{
    padding: 3px 10px; 
    margin: 10px 8px 0 0; 
    cursor: pointer;
    border-radius: 3px; 
    -moz-transition: all 0.5s; 
    -webkit-transition: all 0.5s; 
    -o-transition: all 0.5s; 
    transition: all 0.5s; 
}

.cloud ul span:hover { 
    border-radius: 0; 
    text-shadow: #000 1px 1px 1px 
}
</style>