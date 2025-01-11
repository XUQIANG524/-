<template>
  <div class="home-views">
    <Menu style="position: fixed; width: 100%; z-index: 100" />
    <First style="height: 160vh" />
    <Second style="height: 130vh" />
    <!--    <Third style="height: 90vh" />-->
  </div>
</template>

<script setup>
import Menu from "./components/MenuSlide.vue";
import First from "./components/firstPage.vue";
</script>

<style lang="less" scoped>
/* 确保 body 允许滚动 */
.home-views {
  height: 100vh;
  overflow: auto; /* 允许滚动 */
}
</style>
