<template>
  <div class="visualization-page">
    <iframe src="/visual/index.html" width="100%" height="100%" frameborder="0"></iframe>
  </div>
</template>

<script>
export default {
  name: "VisualizationPage",
  mounted() {
    this.enterFullscreen();
  },
  methods: {
    // 请求全屏
    enterFullscreen() {
      const element = document.documentElement; // 获取 `<html>` 元素或其他容器元素

      // 兼容性处理，不同浏览器的前缀
      if (element.requestFullscreen) {
        element.requestFullscreen(); // 标准方式
      } else if (element.mozRequestFullScreen) { // Firefox
        element.mozRequestFullScreen();
      } else if (element.webkitRequestFullscreen) { // Chrome, Safari, Opera
        element.webkitRequestFullscreen();
      } else if (element.msRequestFullscreen) { // IE/Edge
        element.msRequestFullscreen();
      } else {
        console.error("浏览器不支持全屏 API");
      }
    }
  }
};


</script>

<style scoped>
.visualization-page iframe {
  width: 100%;
  height: 100vh;
}
</style>
