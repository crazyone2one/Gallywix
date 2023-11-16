<script setup lang="ts">
import { computed,ref } from "vue"
import { useRoute, useRouter } from "vue-router"

import { useAuthStore } from "/@/store/auth-store"

import { LOGIN, loginFunction } from "/@/apis/auth"
import { useRequest } from "alova"
import { NButton,NCard, NForm, NFormItemRow, NH1, NInput } from "naive-ui"

const model = ref<LOGIN>({
  username: "",
  password: "",
})
const store = useAuthStore()
const rules = {
  username: {
    required: true,
    message: "Username is required.",
    trigger: "blur",
  },
  password: {
    required: true,
    message: "Password is required.",
    trigger: "blur",
  },
}
const route = useRoute()
const router = useRouter()
const disabled = computed<boolean>(() => model.value.username === "" || model.value.password === "")
const { loading, send, onSuccess } = useRequest(loginFunction(model.value), {
  immediate: false,
})
const handleLogin = () => {
  send()
}
onSuccess((resp) => {
  const { access_token, refresh_token, roles, userId, user } = resp.data
  store.accessToken = access_token
  store.refreshToken = refresh_token
  store.roles = roles
  store.userInfo = user
  store.userId = userId
  // 路由跳转
  if (route.query?.redirect) {
    router.push({
      path: <string>route.query?.redirect,
      query: route.query?.params
        ? Object.keys(<string>route.query?.params).length > 0
          ? JSON.parse(<string>route.query?.params)
          : ""
        : "",
    })
  } else {
    router.push("/")
  }
})
</script>
<template>
  <n-h1 style="--font-size: 60px; --font-weight: 100">
    Sign-in
  </n-h1>
  <n-card size="large" style="--padding-bottom: 30px" class="shadow-lg shadow-rose-500/50">
    <!-- <n-h2 style="--font-weight: 400">Sign-in</n-h2> -->
    <n-form size="large" :rules="rules" :model="model">
      <n-form-item-row path="username">
        <n-input v-model:value="model.username" placeholder="Input your username">
          <template #prefix>
            <span class="i-carbon:group-security" />
          </template>
        </n-input>
      </n-form-item-row>
      <n-form-item-row path="password">
        <n-input
          v-model:value="model.password"
          type="password"
          placeholder="Input your password"
          show-password-on="mousedown"
        >
          <template #prefix>
            <span class="i-carbon:ibm-cloud-hyper-protect-crypto-services" />
          </template>
        </n-input>
      </n-form-item-row>
    </n-form>
    <n-button
      type="primary"
      size="large"
      block
      :loading="loading"
      :disabled="disabled"
      @click="handleLogin"
    >
      Sign in
    </n-button>
    <br>
  </n-card>
</template>

<style scoped>
.n-h1 {
  margin: 20vh auto 20px;
  text-align: center;
  letter-spacing: 5px;
  opacity: 0.8;
}

.n-card {
  margin: 0 auto;
  max-width: 380px;
  /* box-shadow: var(--box-shadow); */
}
</style>
