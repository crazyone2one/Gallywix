<script setup lang="ts">
import { computed, ref } from "vue"

import { useUserStore } from "/@/store/modules/user"

import { ILoginParam } from "/@/apis/auth"
import { i18n } from "/@/i18n"
import { ElButton, ElForm, ElFormItem, ElInput } from "element-plus"

const { login } = useUserStore()
const model = ref<ILoginParam>({
  username: "",
  password: "",
})
const rules = {
  username: {
    required: true,
    message: i18n.t("organization.integration.input_api_account"),
    trigger: "blur",
  },
  password: [
    {
      required: true,
      message: i18n.t("commons.input_password"),
      trigger: "blur",
    },
    { min: 6, max: 30, message: i18n.t("commons.input_limit", [6, 30]), trigger: "blur" },
  ],
}
const disabled = computed<boolean>(
  () => model.value.username === "" || model.value.password === "" || model.value.password.length < 6,
)

const handleLogin = () => {
  login(model.value)
}
</script>
<template>
  <div class="login-wrap">
    <div class="ms-login">
      <div class="ms-title">Sign in to your account</div>
      <el-form ref="login" :model="model" :rules="rules" label-width="0px" class="ms-content" size="large">
        <el-form-item prop="username">
          <el-input v-model="model.username" placeholder="username">
            <!-- <template #prepend>
              <el-button :icon="User"></el-button>
            </template> -->
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="model.password" type="password" placeholder="password" @keyup.enter="handleLogin">
            <!-- <template #prepend>
              <el-button :icon="Lock"></el-button>
            </template> -->
          </el-input>
        </el-form-item>
        <div class="login-btn">
          <el-button :disabled="disabled" type="primary" @click="handleLogin">登 录</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.login-wrap {
  position: relative;
  width: 100%;
  height: 100%;
  /* background-image: url(../assets/img/login-bg.jpg); */
  background-size: 100%;
}
.ms-title {
  width: 100%;
  line-height: 50px;
  text-align: center;
  font-size: 20px;
  /* color: #fff; */
  border-bottom: 1px solid #ddd;
}
.ms-login {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 350px;
  margin: -190px 0 0 -175px;
  border-radius: 5px;
  background: rgba(255, 255, 255, 0.3);
  overflow: hidden;
}
.ms-content {
  padding: 30px 30px;
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
  color: #fff;
}
</style>
