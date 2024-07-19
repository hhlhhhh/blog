<script setup>
import { ref,reactive,onMounted } from 'vue'
import { getHeadNoticeList } from '@/http/api/notice.js'
import { useRouter } from 'vue-router';
const router = useRouter()

const noticeList = ref([]);
const visible = ref(false);
const showData = ref({})

const handleClick = (data) => {
    showData.value = data
    visible.value = true
}
const handleOk = () => {
    visible.value = false;
}
const handleCancel = () => {
    visible.value = false;
}

onMounted(async () => {
    const res = await getHeadNoticeList();
    noticeList.value = res.data.data;
})
</script>
<template>
    <div class="whitebg notice">
        <h2 class="htitle" @click="router.push('/notice')">公告</h2>
        <ul>
            <li v-for="(item,index) in noticeList" :key="index">
                <a @click = "handleClick(item)">{{item.title}}</a>
            </li>
        </ul>
        <a-drawer :width="340" :visible="visible" @ok="handleOk" @cancel="handleCancel" unmountOnClose>
            <template #title>
                {{ showData.title }}
            </template>
            <div style="text-indent: 2em;">
                {{ showData.text }}
            </div>
        </a-drawer>
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
.notice ul { 
    padding-top: 18px 
}
.notice li { 
    font-size: 12px; 
    line-height: 30px; 
    margin-bottom: 12px; 
    display: block; 
    -moz-border-radius: 4px; 
    border-radius: 4px; 
    background: #f6f6f6; 
    padding: 4px 12px 4px 30px; 
    overflow: hidden; 
    text-overflow: ellipsis; 
    white-space: nowrap; 
    position: relative; 
    -moz-transition: all .2s ease; 
    -webkit-transition: all .2s ease; 
    transition: all .2s ease; 
}
.notice li:before { 
    position: absolute; 
    content: ""; 
    width: 3px; 
    height: 3px; 
    background: #000; 
    border-radius: 10px; 
    left: 15px; 
    top: 20px 
}
.notice li:hover { 
    background: #f2f2f2; 
    box-shadow: 0 0 10px #ccc; 
    -moz-transition: all .2s ease; 
    -webkit-transition: all .2s ease; 
    transition: all .2s ease; 
    }
</style>