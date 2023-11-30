<script setup lang="ts">
defineProps<{ title: string }>()
</script>
<template>
  <p>
    <span>
      {{ title }}
    </span>
    <span>
      <slot></slot>
    </span>
  </p>
</template>
<style scoped>
p {
  padding: 3px 5px;
  font-size: 16px;
  border-radius: 0px;
  border-left: 4px solid #783887;
  margin: 5px 5px 10px 5px;
}
</style>
