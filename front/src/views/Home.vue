<script setup>
import ClickRank from '../components/ClickRank.vue';
import NewBlog from '../components/NewBlog.vue';
import Notice from '../components/Notice.vue';
import Tag from '../components/Tag.vue';
import FriendLink from '../components/FriendLink.vue';
import Channel from '@/components/Channel.vue';

import { ArticleStore } from '../store';
import CarouselChannel from '../components/CarouselChannel.vue';

const articleStore = ArticleStore();



</script>
<template>
  <div class="home-container">
        <a-row>
            <a-col :span="17">
                <Channel v-if="!articleStore.searchFlag"></Channel>
                <CarouselChannel v-if="!articleStore.searchFlag"></CarouselChannel>
                <NewBlog></NewBlog>
            </a-col>
            <a-col :span="1"></a-col>
            <a-col :span="6">
                <Notice></Notice>
                <ClickRank></ClickRank>
                <Tag></Tag>
                <FriendLink></FriendLink>
            </a-col>
        </a-row>
  </div>
</template>

<style scoped lang="less">
.home-container{

}
</style>
