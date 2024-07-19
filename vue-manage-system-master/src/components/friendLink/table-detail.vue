<template>
	<el-descriptions title="" :column="1" border>
		<el-descriptions-item>
			<template #label> 友链ID </template>
			{{ data.id }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 标题 </template>
			{{ data.title }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 跳转链接 </template>
			{{ data.url }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 跳转方式 </template>
			{{ data.target }}
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
