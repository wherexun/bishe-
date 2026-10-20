<template>
  <div class="login-container">
    <div class="bg-mask"></div>

    <div class="glass-card">
      <div class="brand-row">
        <img src="@/assets/imgs/cart-icon.png" alt="" class="brand-icon">
        <span class="brand-name">校园小卖部</span>
      </div>
      <div class="login-title">欢 迎 登 录</div>
      <div class="login-subtitle">登录后继续选购心仪好物</div>
      <el-form :model="data.form"  ref="formRef" :rules="data.rules">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item prop="role">
          <el-select size="large" style="width: 100%" v-model="data.form.role">
            <el-option value="普通用户" label="普通用户"></el-option>
            <el-option value="管理员" label="管理员"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button class="login-btn" size="large" type="primary" style="width: 100%" @click="login">登 录</el-button>
        </el-form-item>
      </el-form>
      <div class="login-footer">
        还没有账号？请 <a href="/register">注册</a>
      </div>
    </div>

    <div class="feature-tags">
      <span>零食日百一站购</span>
      <span>下单配送至宿舍</span>
      <span>热销榜单实时看</span>
    </div>
  </div>
</template>

<script setup>
  import { reactive, ref } from "vue";
  import { User, Lock } from "@element-plus/icons-vue";
  import request from "@/utils/request";
  import {ElMessage} from "element-plus";
  import router from "@/router";

  const data = reactive({
    form: { role: '普通用户' },
    rules: {
      username: [
        { required: true, message: '请输入账号', trigger: 'blur' },
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
      ],
    }
  })

  const formRef = ref()

  // 点击登录按钮的时候会触发这个方法
  const login = () => {
    formRef.value.validate((valid => {
      if (valid) {
        // 调用后台的接口
        request.post('/login', data.form).then(res => {
          if (res.code === '200') {
            ElMessage.success("登录成功")
            localStorage.setItem('system-user', JSON.stringify(res.data))
            if (res.data.role === '管理员'){
              router.push('/manager/home')
            } else {
              router.push('/front/home')
            }
          } else {
            ElMessage.error(res.msg)
          }
        })
      }
    })).catch(error => {
      console.error(error)
    })
  }

</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  background: url('@/assets/imgs/login-side.jpg') center/cover no-repeat;
}

.bg-mask {
  position: absolute;
  inset: 0;
  background: rgba(18, 24, 40, 0.55);
}

.glass-card {
  position: relative;
  z-index: 1;
  width: 400px;
  padding: 44px 36px 32px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.28);
  backdrop-filter: blur(6px);
  -webkit-backdrop-filter: blur(6px);
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.30);
}

.brand-row {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 18px;
}
.brand-icon {
  width: 30px;
  height: 30px;
}
.brand-name {
  font-size: 18px;
  font-weight: bold;
  margin-left: 8px;
  color: #ffffff;
  letter-spacing: 2px;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.5);
}
.login-title {
  font-weight: bold;
  font-size: 28px;
  text-align: center;
  color: #ffffff;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.45);
}
.login-subtitle {
  text-align: center;
  font-size: 14px;
  color: #e8eaf0;
  margin: 10px 0 28px;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.5);
}
.login-btn {
  font-weight: bold;
  letter-spacing: 6px;
  border-radius: 8px;
  height: 44px;
}
.login-footer {
  text-align: right;
  font-size: 14px;
  color: #e8eaf0;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.5);
}
.login-footer a {
  color: #79bbff;
  text-decoration: none;
}

.feature-tags {
  position: absolute;
  z-index: 1;
  bottom: 28px;
  left: 0;
  right: 0;
  display: flex;
  justify-content: center;
  gap: 14px;
}
.feature-tags span {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.92);
  padding: 7px 18px;
  border: 1px solid rgba(255, 255, 255, 0.4);
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.10);
  backdrop-filter: blur(4px);
}

:deep(input:-webkit-autofill),
:deep(input:-webkit-autofill:hover),
:deep(input:-webkit-autofill:focus),
:deep(input:-webkit-autofill:active) {
  -webkit-box-shadow: 0 0 0 1000px #ffffff inset;
  -webkit-text-fill-color: #606266;
  caret-color: #606266;
  transition: background-color 99999s ease-out;
}

@media (max-width: 480px) {
  .glass-card {
    width: calc(100% - 40px);
    padding: 32px 22px 24px;
  }
  .feature-tags {
    flex-wrap: wrap;
    padding: 0 20px;
  }
}
</style>
