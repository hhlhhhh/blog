<template>
	<el-descriptions title="" :column="1" border>
		<el-descriptions-item>
			<template #label> 公告ID </template>
			{{ data.id }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 标题 </template>
			{{ data.title }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 排序 </template>
			{{ data.sort }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 创建时间 </template>
			{{ dealTime(data.createTime) }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 更新时间 </template>
			{{ dealTime(data.updateTime) }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 内容 </template>
			{{ data.text }}
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

<style scoped>
:deep(.el-descriptions__label){
	width: 120px;
}

</style>
