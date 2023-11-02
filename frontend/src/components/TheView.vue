<script setup lang="ts">
import { useLoadingBar, useDialog, useMessage, useNotification } from "naive-ui"
import router from "/@/router"
// todo: route transtion
const loadingBar = useLoadingBar()
router.beforeEach(() => loadingBar?.start())
router.afterEach(() => loadingBar?.finish())

window.$message = useMessage()
window.$dialog = useDialog()
window.$notification = useNotification()
</script>
<template>
  <router-view />
</template>

<style scoped></style>
