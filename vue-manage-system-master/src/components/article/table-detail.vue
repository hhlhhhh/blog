<template>
	<el-descriptions title="" :column="1" border>
		<el-descriptions-item>
			<template #label> 文章ID </template>
			{{ data.id }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 标题 </template>
			{{ data.title }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 摘要 </template>
			{{ data.summary }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 内容 </template>
			<v-md-preview :text="data.content"></v-md-preview>
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
