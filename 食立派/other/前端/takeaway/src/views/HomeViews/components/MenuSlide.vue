<template>
  <el-menu
    :default-active="activeIndex"
    class="el-menu-demo"
    mode="horizontal"
    :ellipsis="false"
    @select="handleSelect"
  >
    <el-menu-item index="0">
      <img
        src="/images/icon (1).png"
        style="margin-top: 15px; margin-bottom: 10px; margin-left: 15px;width: 70px;height: 70px;"
        @click="$router.push('/')"
      />
      <div class="logo">
        <h1>食立派</h1>
        <p style="color: #ff5733; font-size: 0.8em; font-weight: bold">
          干饭实力派
        </p>
      </div>
    </el-menu-item>
    <el-menu-item index="0">
      <titSvg style="margin-top: -20px; margin-left: -10px" @click="$router.push('/')" />
    </el-menu-item>
    <div class="flex-grow" />
    <el-menu-item index="1" @click="$router.push('/')">首页</el-menu-item>
    <el-menu-item index="1" @click="$router.push('/reverso')">入驻加盟</el-menu-item>
    <el-menu-item index="1" @click="$router.push('/contact')">联系我们</el-menu-item>

    <el-menu-item index="1">
      <el-button
        style="
          background-color: #ff5733;
          color: #fff;
          height: 30px;
          line-height: 40px;
          border-radius: 8px;
        "
        @click="$router.push('/login')"
        >登录</el-button
      >
    </el-menu-item>
  </el-menu>
</template>

<script setup>
import { ref } from 'vue'
/*import logo from '@/favicon.ico'*/


const activeIndex = ref('1')
const handleSelect = (key, keyPath) => {
  console.log(key, keyPath)
}
</script>

<style lang="less" scoped>

.el-menu {
  background-color: transparent;
}
.el-menu-demo {
  border-bottom: 2px solid hsla(0, 0%, 100%, 0);
}
.flex-grow {
  flex-grow: 1;
}
.el-menu--horizontal > .el-menu-item.is-active {
  border-bottom: 2px solid hsla(0, 0%, 100%, 0);
  color: #000000 !important;
}
.el-menu--horizontal .el-menu-item:not(.is-disabled):focus,
.el-menu--horizontal .el-menu-item:not(.is-disabled):hover {
  background-color: transparent !important;
  color: #000000;
  outline: none;
}
.logo{
  display: flex;
}
</style>
