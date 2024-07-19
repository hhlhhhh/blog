<template>
	<div style="width: 100%;height: 100%;">
		<a-form class="whitebg" style="margin-top: 20px;" ref="formRef" :model="channel" >
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
							:src="channel.channelImg"
							/>
							<template #trigger-icon>
								<IconEdit  />
							</template>
						</a-avatar> 
					</template>
				</a-upload>
			</a-form-item>
			<a-form-item label="名字">
				<a-input v-model="channel.name" placeholder="请输入名字" />
			</a-form-item>
			<a-form-item label="位置">
				<a-input v-model="channel.pos" placeholder="请输入位置" />
			</a-form-item>
			<a-form-item label="摘要">
				<a-textarea v-model="channel.summary" placeholder="请输入摘要" />
			</a-form-item>
			<a-form-item>
				<a-textarea :auto-size="{minRows: 8,maxRows: 15}" v-model="channel.content" placeholder="请输入摘要" />
			</a-form-item>
			<a-form-item>
				<a-button @click="handleClick">保存</a-button>
			</a-form-item>
		</a-form>
	</div>
</template>

<script lang="ts" setup>
import { ref,reactive,onMounted } from 'vue';
import {useRoute} from 'vue-router'
import {saveChannelApi,uploadImageApi} from '../../api/channel.js'
import { Message } from '@arco-design/web-vue';
const props = defineProps({
	data: {
		type: Object,
		required: true
	},
	update: {
		type: Function,
		required: true
	}
});

const form = ref(props.data);

const route = useRoute()

const text = ref('')

const channel = reactive({
    id:null,
    summary:"",
    content:"ddddddddddddddddd",
    channelImg:"",
    parentId:null,
    name:"",
	pos:""
})

const channelList = ref([])

const loadImageData = reactive({
    file: []
})


//上传照片
const uploadImage = (list) => {
    uploadImageApi(list[0]).then(res => {
        if(res.data.state == "SUCCESS"){
            channel.channelImg = res.data.data
        }
    })
}

const handleClick = () =>{
    saveChannelApi(channel).then(res => {
        if(res.data.state == "SUCCESS"){
            Message.success("保存成功！")
        }else{
            Message.error("保存失败！")
        }
    })
}

const init = () => {
    if(props.data){
		channel.id = props.data.id
		channel.summary = props.data.summary
		channel.content = props.data.content
		channel.channelImg = props.data.channelImg
		channel.parentId = props.data.parentId
		channel.name = props.data.name
	}
    
}

onMounted(() => {
    init()
})



</script>

<style scoped>
.avatar-uploader .el-upload {
	border: 1px dashed var(--el-border-color);
	border-radius: 6px;
	cursor: pointer;
	position: relative;
	overflow: hidden;
	transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
	border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
	font-size: 28px;
	color: #8c939d;
	width: 178px;
	height: 178px;
	text-align: center;
}
</style>
