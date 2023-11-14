<script setup lang="ts">
import { ref } from "vue"
import { NUpload, NUploadDragger, NText, NP, NIcon, UploadInst, UploadFileInfo, NSpin } from "naive-ui"
import ModalDialog from "./ModalDialog.vue"
import { uploadFile } from "../apis/upload-down"
import { useRequest } from "alova"

const file = ref<UploadFileInfo>()
const modalDialog = ref<InstanceType<typeof ModalDialog> | null>(null)
const uploadRef = ref<UploadInst | null>(null)
const open = () => modalDialog.value?.showModal()

const { loading, send: sendUpload } = useRequest(uploadFile, {
  immediate: false,
})
const handleConfirm = () => {
  sendUpload(file.value?.file, `/file/file/upload`)
}
const handleChange = (options: { fileList: UploadFileInfo[] }) => {
  file.value = options.fileList[0]
}

defineExpose({ open })
</script>
<template>
  <n-spin :show="loading">
    <modal-dialog ref="modalDialog" title="upload" @confirm="handleConfirm">
      <template #content>
        <n-upload ref="uploadRef" directory-dnd action="" :max="1" :default-upload="false" @change="handleChange">
          <n-upload-dragger>
            <div style="margin-bottom: 12px">
              <n-icon size="48" :depth="3">
                <span class="i-tabler:archive" />
              </n-icon>
            </div>
            <n-text style="font-size: 16px"> 点击或者拖动文件到该区域来上传 </n-text>
            <n-p depth="3" style="margin: 8px 0 0 0">
              请不要上传敏感数据，比如你的银行卡号和密码，信用卡号有效期和安全码
            </n-p>
          </n-upload-dragger>
        </n-upload>
      </template>
    </modal-dialog>
  </n-spin>
</template>

<style></style>
