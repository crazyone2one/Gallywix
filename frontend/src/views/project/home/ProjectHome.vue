<script setup lang="ts">
import { computed, onMounted, ref } from "vue"

import { useUserStore } from "/@/store/modules/user-store"

import { hasPermission } from "/@/utils/permission"

import { getProject, IProject } from "/@/apis/project"
import { useRequest } from "alova"
import { NCard, NGi, NGrid } from "naive-ui"

const loading = ref(false)
const project = ref({} as IProject)
const projectId = computed(() => {
  return useUserStore().project_id
})
const { send } = useRequest((val) => getProject(val), { immediate: false })

const click = (str: string, permissions: Array<string>) => {
  for (let permission of permissions) {
    if (hasPermission(permission)) {
      // this.$router.push(str);
      console.log(`output->str`, str)
      return
    }
  }
  window.$message.warning("无操作权限！")
}
onMounted(() => {
  send(projectId.value).then((res) => (project.value = res))
})
</script>
<template>
  <n-card>
    <template #header>
      <n-skeleton v-if="loading" text width="60%" />
      <template v-else> I'm OK </template>
    </template>
    <n-skeleton v-if="loading" text :repeat="6" />
    <template v-else>
      <n-grid x-gap="12" :cols="2">
        <n-gi>
          <n-card class="project-info-card">
            <div class="project-info-card-div shepherd-project-name">
              <span class="project-name">{{ project.name }}</span>
              <i v-permissions="['PROJECT_MANAGER:READ+EDIT']" class="i-tabler:edit project-edit"></i>
            </div>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card class="project-menu-card">
            <div class="project-menu-card-div">
              <div class="div-item">
                <div style="float: left">
                  <i class="i-tabler:users icon-color" @click="click('/project/member', ['PROJECT_USER:READ'])" />
                </div>
                <div style="float: left">
                  <span class="title" @click="click('/project/member', ['PROJECT_USER:READ'])">
                    {{ $t("project.member") }}
                  </span>
                  <br />
                  <span class="desc">{{ $t("project.member_desc") }}</span>
                </div>
              </div>
              <div class="div-item">
                <div style="float: left">
                  <i
                    class="i-tabler:users-group icon-color"
                    @click="click('/project/usergroup', ['PROJECT_GROUP:READ'])">
                  </i>
                </div>
                <div style="float: left">
                  <span class="title" @click="click('/project/usergroup', ['PROJECT_GROUP:READ'])">
                    {{ $t("group.group_permission") }} </span
                  ><br />
                  <span class="desc">{{ $t("project.group_desc") }}</span>
                </div>
              </div>
              <div class="div-item">
                <div style="float: left">
                  <i
                    class="i-tabler:device-desktop icon-color"
                    @click="click('/project/env', ['PROJECT_ENVIRONMENT:READ'])">
                  </i>
                </div>
                <div style="float: left">
                  <span class="title" @click="click('/project/env', ['PROJECT_ENVIRONMENT:READ'])">
                    {{ $t("project.env") }} </span
                  ><br />
                  <span class="desc">{{ $t("project.env_desc") }}</span>
                </div>
              </div>
              <div class="div-item">
                <div style="float: left">
                  <i
                    class="i-tabler:package icon-color"
                    @click="click('/project/file/manage', ['PROJECT_FILE:READ', 'PROJECT_FILE:READ+JAR'])">
                  </i>
                </div>
                <div style="float: left">
                  <span
                    class="title"
                    @click="click('/project/file/manage', ['PROJECT_FILE:READ', 'PROJECT_FILE:READ+JAR'])">
                    {{ $t("project.file_manage") }} </span
                  ><br />
                  <span class="desc">{{ $t("project.file_desc") }}</span>
                </div>
              </div>
              <div class="div-item">
                <div style="float: left">
                  <i
                    class="i-tabler:file-type-doc icon-color"
                    @click="click('/project/code/segment', ['PROJECT_CUSTOM_CODE:READ'])">
                  </i>
                </div>
                <div style="float: left">
                  <span class="title" @click="click('/project/code/segment', ['PROJECT_CUSTOM_CODE:READ'])">
                    {{ $t("project.code_segment.code_segment") }} </span
                  ><br />
                  <span class="desc">{{ $t("project.code_segment_desc") }}</span>
                </div>
              </div>
              <div class="div-item">
                <div style="float: left">
                  <i class="i-tabler:flag icon-color" @click="click('/project/log', ['PROJECT_OPERATING_LOG:READ'])">
                  </i>
                </div>
                <div style="float: left">
                  <span class="title" @click="click('/project/log', ['PROJECT_OPERATING_LOG:READ'])">
                    {{ $t("project.log") }} </span
                  ><br />
                  <span class="desc">{{ $t("project.log_desc") }}</span>
                </div>
              </div>
              <div class="div-item">
                <div style="float: left">
                  <i
                    class="i-tabler:device-mobile icon-color"
                    @click="click('/project/app', ['PROJECT_APP_MANAGER:READ+EDIT'])">
                  </i>
                </div>
                <div style="float: left">
                  <span class="title" @click="click('/project/app', ['PROJECT_APP_MANAGER:READ+EDIT'])">
                    {{ $t("project.app_manage") }} </span
                  ><br />
                  <span class="desc">{{ $t("project.app_manage_desc") }}</span>
                </div>
              </div>
            </div>
          </n-card>
        </n-gi>
      </n-grid>
    </template>
  </n-card>
</template>

<style scoped>
.project-info-card,
.project-menu-card {
  height: 500px;
  position: relative;
}

.project-info-card-div,
.project-menu-card-div {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.project-menu-card {
  height: 500px;
}

.project-name {
  font-weight: bold;
  text-align: center;
  color: var(--primary_color);
  font-size: 18px;
  user-select: none;
}

.project-item {
  margin-top: 25px;
  font-size: 16px;
  min-width: 220px;
}

.icon-color {
  font-size: 35px;
  color: var(--primary_color);
  margin-right: 6px;
}

.div-item {
  width: 100%;
  padding: 10px;
  height: 50px;
  min-width: 230px;
}

.icon-color:hover,
.title:hover {
  cursor: pointer;
}

.title {
  font-size: 10px;
}

.card {
  height: calc(100vh - 100px);
  position: relative;
}

.number {
  font-size: 15px;
  color: var(--primary_color);
}

.number:hover {
  cursor: pointer;
}

.project-item-title {
  font-size: 15px;
  user-select: none;
}

.project-item-desc {
  font-size: 14px;
  user-select: none;
}

.card-col {
  margin-top: 80px;
}

.project-edit {
  color: var(--primary_color);
  font-size: 14px;
  margin-left: 8px;
}

.project-edit:hover {
  cursor: pointer;
}

.desc {
  color: #989292;
}

.project-desc {
  display: inline-block;
  width: 180px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 14px;
}
</style>
