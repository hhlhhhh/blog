<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">后台管理系统</div>
            <el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
                <el-form-item prop="username">
                    <el-input v-model="param.account" placeholder="账户">
                        <template #prepend>
                            <el-button :icon="User"></el-button>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input
                        type="password"
                        placeholder="密码"
                        v-model="param.password"
                        @keyup.enter="submitForm(login)"
                    >
                        <template #prepend>
                            <el-button :icon="Lock"></el-button>
                        </template>
                    </el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm(login)">登录</el-button>
                </div>
            </el-form>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useTagsStore } from '../store/tags';
import { getUserInfoApi, loginApi} from '../api/user'
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import type { FormInstance, FormRules } from 'element-plus';
import { Lock, User } from '@element-plus/icons-vue';

interface LoginInfo {
    account: string;
    password: string;
}

const lgStr = localStorage.getItem('login-param');
const defParam = lgStr ? JSON.parse(lgStr) : null;
const checked = ref(lgStr ? true : false);

const router = useRouter();
const param = reactive<LoginInfo>({
    account: defParam ? defParam.account : '',
    password: defParam ? defParam.password : '',
});

const rules: FormRules = {
    account: [
        {
            required: true,
            message: '请输入用户名',
            trigger: 'blur',
        },
    ],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
};
const login = ref<FormInstance>();
const submitForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return;
    formEl.validate((valid: boolean) => {
        if (valid) {
            loginApi(param).then((res) => {
                if(res.data.state=="SUCCESS"){
                    localStorage.setItem('auth', res.headers.authorization);
                    getUserInfoApi().then(({data}) => {
                      localStorage.setItem('user-i', JSON.stringify(data.data));
                      router.push('/user-table');
                    })
                    ElMessage.success('登录成功');
                }else{
                    ElMessage.error(res.data.msg);

                    return false;
                }
            })
            
        } else {
            ElMessage.error('登录失败');
            return false;
        }
    });
};

const tags = useTagsStore();
tags.clearTags();
</script>

<style scoped>
.login-wrap {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    background-image: url(../assets/img/login-bg.jpg);
    background-size: 100%;
}
.ms-title {
    line-height: 50px;
    text-align: center;
    font-size: 20px;
    color: #333;
    font-weight: bold;
    padding-top: 10px;
}
.ms-login {
    width: 350px;
    border-radius: 5px;
    background: #fff;
}
.ms-content {
    padding: 10px 30px 30px;
}
.login-btn {
    text-align: center;
}
.login-btn button {
    width: 100%;
    height: 36px;
    margin-bottom: 10px;
}
.login-tips {
    font-size: 12px;
    line-height: 30px;
    color: #333;
}
</style>
