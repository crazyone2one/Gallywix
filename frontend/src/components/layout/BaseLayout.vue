<script setup lang="ts">
import TheAside from "./components/aside/TheAside.vue"
import TheHeader from "./components/header/TheHeader.vue"

import { ElContainer, ElHeader, ElMain } from "element-plus"
</script>

<template>
  <div class="APP">
    <el-container>
      <el-header>
        <the-header />
      </el-header>
      <el-container>
        <the-aside />
        <el-container>
          <el-main>
            <router-view />
          </el-main>
          <!-- <el-footer>Footer</el-footer> -->
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped></style>
