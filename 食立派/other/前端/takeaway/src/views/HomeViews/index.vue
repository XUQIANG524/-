<template>
  <div class="home-views">
    <Menu style="position: fixed; width: 97%; z-index: 100" />
    <First style="height: 100vh" />
    <Second style="height: 150vh" />
     <!-- Footer -->
     <div class="footer">
      <div class="footer-content">
        <div class="footer-links">
          <a href="#about" class="footer-link">关于我们</a>
          <a href="#privacy" class="footer-link">隐私政策</a>
          <a href="#contact" class="footer-link">联系我们</a>
        </div>
        <hr />
        <div class="footer-text">
          <p>&copy; 2025 帅哥刘涛有限公司. All rights reserved.</p>
          <p>京ICP备2025000001号-1</p>
        </div>
      </div>
    </div>
    <!--    <Third style="height: 90vh" />-->
  </div>
</template>

<script setup>
import Menu from "./components/MenuSlide.vue";
import First from "./components/firstPage.vue";
import Second from "./components/secondPage.vue";
// import Third from './components/thirdPage.vue'
</script>

<style lang="less" scoped>
/* 确保 body 允许滚动 */
.home-views {
  height: 100vh;
  overflow: auto; /* 允许滚动 */
}

.footer {
  background-color: #2e2125;
  color: white;
  padding: 40px 20px;
  text-align: center;
}

.footer-links {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 20px;
}

.footer-link {
  color: white;
  text-decoration: none;
  font-size: 16px;
}

.footer-link:hover {
  color: #ff5733;
}

.footer-text {
  font-size: 14px;
  color: #ddd;
}

.footer-text p {
  margin: 5px 0;
}
</style>
