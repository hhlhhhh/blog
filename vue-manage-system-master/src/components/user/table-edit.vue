<template>
	<el-form ref="formRef" :model="form" label-width="100px">
		<el-form-item label="用户名" prop="nickname">
			<el-input v-model="form.nickname"></el-input>
		</el-form-item>
		<el-form-item label="状态" prop="status">
			<el-switch
				v-model="form.status"
				:active-value="1"
				:inactive-value="0"
				active-text="正常"
				inactive-text="禁止"
			></el-switch>
		</el-form-item>
		<el-form-item label="简介" prop="profile">
			<el-input type="textarea" v-model="form.profile"></el-input>
		</el-form-item>
		<el-form-item>
			<el-button type="primary" @click="saveEdit(formRef)">保 存</el-button>
		</el-form-item>
	</el-form>
</template>

<script lang="ts" setup>
import { ElMessage, FormInstance, FormRules, UploadProps } from 'element-plus';
import { ref } from 'vue';
import { updateUserApi } from '../../api/user';

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


const defaultData = {
	id: null,
	nickname: '',
	status: '',
	profile:""
};

const form = ref(props.data);

const formRef = ref<FormInstance>();
const saveEdit = (formEl: FormInstance | undefined) => {
	if (!formEl) return;
	formEl.validate(valid => {
		if (!valid) return false;
		updateUserApi(form.value).then(({data}) => {
			if (data.state=="SUCCESS") {
				props.update(form.value);
				ElMessage.success('保存成功！');
			}
			
		})
	});
};


</script>

<style>
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
