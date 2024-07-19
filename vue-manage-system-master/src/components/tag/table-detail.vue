<template>
	<el-descriptions title="" :column="2" border>
		<el-descriptions-item>
			<template #label> 标签ID </template>
			{{ data.id }}
		</el-descriptions-item>
		<el-descriptions-item>
			<template #label> 标签名 </template>
			{{ data.nickname }}
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

