<script setup lang="ts">
import { computed, h, onMounted, ref } from "vue"

import CustomFieldEdit from "./CustomFieldEdit.vue"
import BaseCard from "/@/components/BaseCard.vue"
import BaseSearch from "/@/components/BaseSearch.vue"
import GaTableOperation from "/@/components/table/GaTableOperation.vue"

import { useUserStore } from "/@/store/modules/user-store"

import { FIELD_TYPE_MAP, SCENE_MAP, SYSTEM_FIELD_NAME_MAP } from "/@/utils/table-constants"

import { ITableDataInfo, PageReq } from "/@/apis/interface"
import { getCustomFieldPages, ICustomField } from "/@/apis/template/custom-field"
import { i18n } from "/@/i18n"
import { useRequest } from "alova"
import { DataTableColumns } from "naive-ui"

const customFieldEdit = ref<InstanceType<typeof CustomFieldEdit> | null>(null)
const condition = ref<PageReq>({
  name: "",
  ids: [],
  pageNumber: 1,
  pageSize: 10,
})
const tableInfo = ref<ITableDataInfo<ICustomField[]>>({
  data: [],
  total: 0,
})
const rowKey = (row: ICustomField) => row.id as unknown as string
const columns: DataTableColumns<ICustomField> = [
  {
    type: "selection",
    align: "center",
  },
  {
    title: i18n.t("custom_field.field_name"),
    key: "name",
    align: "center",
    render(rowData) {
      return h("span", null, {
        default: () => (rowData.system ? i18n.t(systemNameMap.value[rowData.name]) : rowData.name),
      })
    },
  },
  {
    title: i18n.t("custom_field.scene"),
    key: "scene",
    align: "center",
    render(rowData) {
      return h("span", null, {
        default: () => i18n.t(sceneMap.value[rowData.scene]),
      })
    },
  },
  {
    title: i18n.t("custom_field.field_type"),
    key: "type",
    align: "center",
    render(rowData) {
      return h("span", null, {
        default: () => i18n.t(fieldTypeMap.value[rowData.type]),
      })
    },
  },
  {
    title: i18n.t("custom_field.system_field"),
    key: "system",
    align: "center",
    render(rowData) {
      return h("span", null, {
        default: () => i18n.t(rowData.system ? "commons.yes" : "commons.no"),
      })
    },
  },
  {
    title: i18n.t("custom_field.field_remark"),
    key: "remark",
    align: "center",
  },
  {
    title: i18n.t("commons.create_time"),
    key: "createTime",
    align: "center",
  },

  {
    title: i18n.t("commons.operating"),
    key: "operator",
    width: 200,
    fixed: "right",
    align: "center",
    render(rowData) {
      return h(
        GaTableOperation,
        {
          onEditClick: () => handleEdit(rowData),
        },
        {},
      )
    },
  },
]
const fieldTypeMap = computed(() => {
  return FIELD_TYPE_MAP
})
const sceneMap = computed(() => {
  return SCENE_MAP
})
const systemNameMap = computed(() => {
  return SYSTEM_FIELD_NAME_MAP
})
const { loading, send: loadTableData } = useRequest((val) => getCustomFieldPages(val), {
  immediate: false,
})
const handleAdd = () => {
  customFieldEdit.value?.open(i18n.t("custom_field.create"), undefined)
}
const handleEdit = (rowData: ICustomField) => {
  customFieldEdit.value?.open(i18n.t("custom_field.update"), rowData)
}
const loadData = () => {
  condition.value.projectId = useUserStore().project_id
  loadTableData(condition.value).then((res) => {
    const { records, totalRow } = res
    tableInfo.value.data = records
    tableInfo.value.total = totalRow
  })
}
onMounted(() => {
  loadData()
})
</script>
<template>
  <base-card>
    <template #header>
      <base-search :condition="condition" :create-tip="$t('custom_field.create')" @create="handleAdd" />
    </template>
    <template #content>
      <n-skeleton v-if="loading" :sharp="false" height="550px" />
      <n-data-table v-else :columns="columns" :data="tableInfo.data" :row-key="rowKey" />
    </template>
  </base-card>
  <custom-field-edit ref="customFieldEdit" @refresh="loadData" />
</template>

<style scoped></style>
