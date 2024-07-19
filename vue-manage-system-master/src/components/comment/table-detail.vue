<template>
	<el-descriptions title="" :column="1" border>
		<el-descriptions-item>
			<template #label> 评论ID </template>
			{{ data.id }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 内容 </template>
			{{ data.content }}
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
