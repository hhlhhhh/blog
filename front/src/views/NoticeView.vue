<script setup>
import { ref,reactive,onMounted } from 'vue'
import { getNoticeListApi } from '@/http/api/notice.js'
import { dealTime } from '../utils';

const pageView = reactive({
    size: 5,
    current: 1,
    field: "",
    keyword: ""
})

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


const loadData = async () => {
    const {data} = await getNoticeListApi(pageView)
    dataSource.value = data.data.records
    paginationProps.total = data.data.total
}

const searchNotice = () => {
    pageView.current = 1
    loadData()
}

const dataSource = ref([])
const paginationProps = reactive({
    current: pageView.current,
    pageSize: pageView.size,
    total: 20,
    showPageSize: true,
    showJumper: true,
    pageSizeOptions: [5,8,10,15,20],
    showTotal: (total) => `共 ${total} 条`,
    onChange: (page) => {
        pageView.current = page
        paginationProps.current = page
        loadData()
    },
    onPageSizeChange: (size) => {
        pageView.size = size
        paginationProps.pageSize = size
        pageView.current = pageView.current>(paginationProps.total/size)?(paginationProps.total/size):pageView.current
        paginationProps.page = pageView.current
        loadData()
    }
})



onMounted(() => {
    loadData()
})



</script>
<template>
    <div class="notice-view-container whitebg">
        <div class="notice-view-search">
            <a-input-search  placeholder="请输入关键字" v-model="pageView.keyword" @search="searchNotice"  class="search-icon" search-button :button-props="{type: 'text'}">
                <template #button-icon >
                    <icon-search/>
                </template>
            </a-input-search>
        </div>
        <a-list
        class="list-demo-action-layout"
        :bordered="false"
        :data="dataSource"
        :pagination-props="paginationProps"
        >
            <template #item="{ item }">
            <a-list-item class="list-demo-item" action-layout="vertical">
                <template #extra>
                    <div @click="handleClick(item)">
                        <icon-plus v-if="!visible"/>
                        <icon-close v-else/>
                    </div>
                </template>
                <template #actions>
                    <span style="color: #999;font-size: 12px;"> {{ dealTime(item.updateTime) }}</span>
                </template>
                <a-list-item-meta>
                    <template #title>
                        <h3 style="color: rgb(29,33,41);">{{ item.title }}</h3>
                    </template>
                    <template #description>
                        <p style="color: rgb(79,89,105);text-indent: 2em;margin-top: 10px;">{{ item.text }}</p>
                    </template>
                </a-list-item-meta>
            </a-list-item>
            </template>
        </a-list>
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
<style lang="less" scoped>
.notice-view-container{
    :deep(.arco-list-item-action li){
        cursor: text;
    }
    :deep(.arco-list-item-extra){
        cursor: pointer;
    }
    margin-top: 20px;
    .notice-view-search{
        margin-bottom: 30px;
    }
}
.whitebg { 
    background: #fff; 
    border-radius: 3px; 
    padding: 20px; 
    margin-bottom: 20px; 
    overflow: hidden; 
}

</style>
