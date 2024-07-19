<template>
	<div>
		<div class="container">
			<div class="search-box">
				<el-input v-model="query.keyword" placeholder="请输入关键字" class="search-input mr10" clearable></el-input>
				<el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
			</div>
			<el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
				<el-table-column prop="id" label="ID" width="55" align="center"></el-table-column>
				<el-table-column prop="userId" label="用户ID" align="center"></el-table-column>
				<el-table-column prop="content" label="内容" width="200" align="center"></el-table-column>
				<el-table-column prop="articleId" label="文章ID" align="center"></el-table-column>
				<el-table-column prop="parentId" label="父评论ID" align="center"></el-table-column>
				<el-table-column prop="topParent" label="顶层评论ID" align="center"></el-table-column>
				<el-table-column prop="count" label="被评论数" align="center"></el-table-column>
				<el-table-column label="创建时间" align="center">
					<template #default="scope">
						{{ dealTime(scope.row.createTime) }}
					</template>
				</el-table-column>
				<el-table-column label="操作" width="280" align="center">
					<template #default="scope">
						<el-button type="warning" size="small" :icon="View" @click="handleView(scope.row)">
							查看
						</el-button>
						<el-button
							type="danger"
							size="small"
							:icon="Delete"
							@click="handleDelete(scope.$index)"
						>
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="pagination">
				<el-pagination
					background
					layout="total, prev, pager, next"
					:current-page="query.current"
					:page-size="query.size"
					:total="pageTotal"
					@current-change="handlePageChange"
				></el-pagination>
			</div>
		</div>
		<el-dialog title="查看评论详情" v-model="visible1" width="700px" destroy-on-close>
			<TableDetail :data="rowData"  />
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="UserTable">
import { ref, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Delete, Edit, Search, CirclePlusFilled, View } from '@element-plus/icons-vue';
import { deleteCommentApi, getCommentList } from '../api/comment';
import TableDetail from '../components/comment/table-detail.vue';
import {getUserListApi,deleteUserApi,getUserMesApi} from  "../api/user"
import {dealTime} from "../utils/dealTime"

const query = reactive({
	keyword:"",
	feild:"",
	current: 1,
	size: 6
});
const tableData = ref([]);
const pageTotal = ref(0);
// 获取表格数据
const getData = async () => {
	const res = await getCommentList(query);
	tableData.value = res.data.data.records;
	pageTotal.value = res.data.data.total;
};
getData();

// 查询操作
const handleSearch = () => {
	query.current = 1;
	getData();
};
// 分页导航
const handlePageChange = (val: number) => {
	query.current = val;
	getData();
};

// 删除操作
const handleDelete = (index: number) => {
	// 二次确认删除
	ElMessageBox.confirm('确定要删除吗？', '提示', {
		type: 'warning'
	})
		.then(() => {
			deleteCommentApi(tableData.value[index].id).then(res => {
			    ElMessage.success('删除成功');
				tableData.value.splice(index, 1);
				getData()
			})
		})
		.catch(() => {});
};

const visible = ref(false);
let idx: number = -1;
const idEdit = ref(false);
const rowData = ref({});
const handleEdit = (index: number, row) => {
	idx = index;
	rowData.value = row;
	idEdit.value = true;
	visible.value = true;
};
const updateData = (row) => {
	idEdit.value ? (tableData.value[idx] = row) : tableData.value.unshift(row);
	console.log(tableData.value);
	closeDialog();
};

const closeDialog = () => {
	visible.value = false;
	getData();
	idEdit.value = false;
};

const visible1 = ref(false);
const handleView = (row) => {
	rowData.value = row;
	visible1.value = true;
};
</script>

<style scoped>
.search-box {
	margin-bottom: 20px;
}

.search-input {
	width: 200px;
}

.mr10 {
	margin-right: 10px;
}
.table-td-thumb {
	display: block;
	margin: auto;
	width: 40px;
	height: 40px;
}
</style>
