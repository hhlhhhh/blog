<script setup>
import { ref,reactive,onMounted } from 'vue'
import { getShowChannelList } from '@/http/api/channel.js'
import { useRouter } from 'vue-router';

const router = useRouter();

const channelList = ref([]);

onMounted(async () => {
    const res = await getShowChannelList("C");
    channelList.value = res.data.data;
    console.log(channelList.value);
})
</script>

<template>
    <div class="whitebg cloud" style="margin-top: 20px;">
        <ul>
            <a-carousel
                :autoPlay="true"
                animation-name="card"
                show-arrow="never"
                indicator-position="outer"
                :style="{
                    width: '100%',
                    height: '240px',
                }"
            >
                <a-carousel-item @click="router.push('/channel/'+channel.id)" v-for="channel in channelList" :style="{ width: '60%' }">
                    <img
                        :src="channel.channelImg"
                        :style="{
                        width: '100%',
                        }"
                    />
                    <div class="carousel-name">{{ channel.name }}</div>
                </a-carousel-item>
            </a-carousel>
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
.carousel-name{
    color: rgb(131, 175, 155);
    font-size: 16px;
    z-index: 999;
    position: absolute;
    bottom: 12px;
    left: 50%;
    transform: translateX(-50%);
    text-shadow: #000 1px 1px 1px ;  
}

</style>