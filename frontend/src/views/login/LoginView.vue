<script setup lang="ts">
import { computed, ref } from "vue"
import { useRoute, useRouter } from "vue-router"

import { useUserStore } from "/@/store/modules/user-store"

import { ILoginParamItem, loginFunction } from "/@/apis/auth"
import { i18n } from "/@/i18n"
import { useRequest } from "alova"
import { NButton, NCard, NForm, NFormItemRow, NH1, NInput } from "naive-ui"

const model = ref<ILoginParamItem>({
  username: "",
  password: "",
})
const store = useUserStore()
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
const route = useRoute()
const router = useRouter()
const disabled = computed<boolean>(() => model.value.username === "" || model.value.password === "")
const { loading, send } = useRequest(loginFunction(model.value), {
  immediate: false,
})
const handleLogin = () => {
  send()
    .then((res) => {
      const { access_token, refresh_token, user } = res
      store.accessToken = access_token
      store.refreshToken = refresh_token
      // store.roles = roles
      store.userInfo = user
      // store.userId = userId
      store.saveSessionStorage(res)
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
    .catch((error) => window.$message.error(error.toString()))
}
</script>
<template>
  <n-h1 style="--font-size: 60px; --font-weight: 100">Sign in to your account </n-h1>
  <n-card size="large" style="--padding-bottom: 30px" class="shadow-lg shadow-rose-500/50">
    <!-- <n-h2 style="--font-weight: 400">Sign-in</n-h2> -->
    <n-form size="large" :rules="rules" :model="model">
      <n-form-item-row path="username">
        <n-input v-model:value="model.username" :placeholder="$t('organization.integration.input_api_account')">
          <template #prefix>
            <span class="i-tabler:lock-square" />
          </template>
        </n-input>
      </n-form-item-row>
      <n-form-item-row path="password">
        <n-input
          v-model:value="model.password"
          type="password"
          :placeholder="$t('commons.password')"
          show-password-on="mousedown"
          @keyup.enter="handleLogin">
          <template #prefix>
            <span class="i-tabler:lock-star" />
          </template>
        </n-input>
      </n-form-item-row>
    </n-form>
    <n-button type="primary" block :loading="loading" :disabled="disabled" @click="handleLogin">
      {{ $t("commons.login") }}
    </n-button>
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
