<template>
	<el-descriptions title="" :column="2" border>
		<el-descriptions-item>
			<template #label> 用户ID </template>
			{{ data.id }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 用户名 </template>
			{{ data.nickname }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 账号 </template>
			{{ data.account }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 状态 </template>
			{{ data.status ===1 ? "正常":"禁用" }}
		</el-descriptions-item>
		<el-descriptions-item :span="2">
			<template #label> 邮箱 </template>
			{{ data.email }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 创建时间 </template>
			{{ dealTime(data.createTime) }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 简介 </template>
			{{ data.profile }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 头像 </template>
			<img :src="data.avatar" style="width: 120px" alt="" />
		</el-descriptions-item>
	</el-descriptions>
</template>

<script lang="ts" setup>
import { dealTime } from '../../utils/dealTime';

const props = defineProps({
	data: {
		type: Object,
		required: true
	}
});
</script>
