<script setup lang="ts">
import CustomFieldList from "./CustomFieldList.vue"
import TestCaseTemplateList from "./test-case/TestCaseTemplateList.vue"
import BaseCard from "/@/components/BaseCard.vue"

import { NTabPane, NTabs } from "naive-ui"
</script>
<template>
  <base-card>
    <template #header />
    <template #content>
      <n-tabs type="line" animated>
        <n-tab-pane name="field" :tab="$t('custom_field.name')">
          <custom-field-list />
        </n-tab-pane>
        <n-tab-pane name="caseTemplate" :tab="$t('workspace.case_template_manage')">
          <test-case-template-list />
        </n-tab-pane>
        <n-tab-pane name="issueTemplate" :tab="$t('workspace.issue_template_manage')" disabled> 七里香 </n-tab-pane>
        <n-tab-pane name="apiTemplate" :tab="$t('workspace.api_template_manage')" disabled> 七里香 </n-tab-pane>
      </n-tabs>
    </template>
  </base-card>
</template>

<style scoped></style>
