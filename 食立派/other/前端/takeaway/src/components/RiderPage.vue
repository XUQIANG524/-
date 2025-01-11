<template>
  <div id="merchant-page">
    <!-- 导航栏 -->
    <nav class="navbar">
      <div class="logo">
        <h1>食立派</h1>
        <p style="color: #ff5733; font-size: 0.5em; font-weight: bold">
          干饭实力派
        </p>
      </div>
      <ul>
        <li><router-link to="/">首页</router-link></li>
        <li><a href="#">关于我们</a></li>
        <li><a href="#">服务</a></li>
        <li><a href="#">联系我们</a></li>
      </ul>
    </nav>

    <!-- 侧栏和主内容区域 -->
    <el-container style="height: 100vh">
      <!-- 侧栏 -->
      <el-aside width="200px" class="sidebar">
        <el-menu
          :default-active="activeMenu"
          @select="handleMenuSelect"
          class="vertical-menu"
        >
          <router-link to="/rider/menu">
          <el-menu-item index="1">
              餐单广场
          </el-menu-item>
          </router-link>
          <router-link to="/rider/order">
          <el-menu-item index="2">
              订单查看
          </el-menu-item>
          </router-link>
          <router-link to="/rider/riderinfo">
          <el-menu-item index="3">
              个人信息
          </el-menu-item>
          </router-link>
        </el-menu>
      </el-aside>

      <!-- 主内容区域 -->
      <el-main class="main-content">
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "RiderPage",
  setup() {},
};
</script>

<style scoped>
/* 设置页面背景为灰色 */
#merchant-page {
  background-color: #f5f5f5; /* 设置灰色背景 */
  height: 100%;
}

/* 导航栏 */
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  height: 60px;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* logo 样式 */
.logo {
  display: flex;
  align-items: center;
}

.logo h1 {
  font-size: 1.5em;
  font-weight: bold;
  margin-right: 10px;
}

.logo p {
  font-size: 0.5em;
  font-weight: bold;
  color: #ff5733;
}

/* 导航项样式 */
ul {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
}

li {
  margin-right: 20px;
}

li a {
  color: #333;
  text-decoration: none;
  font-size: 1em;
  font-weight: bold;
  transition: all 0.3s ease;
}

li a:hover {
  color: #ff5733;
}

/* 侧栏 */
.sidebar {
  background-color: #f4f4f4;
  height: 100vh; /* 确保侧栏填满屏幕 */
  padding: 8px 10px; /* 为侧栏增加一些内边距 */
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1); /* 侧栏右边添加阴影 */
}

.vertical-menu {
  display: flex;
  flex-direction: column;
  height: 100%;
  justify-content: flex-start;
}

/* Element UI 菜单样式 */
.el-menu .el-menu-item.is-active {
  color: #ff5733 !important; /* 选中项的颜色 */
}

.el-menu .el-menu-item.is-hover {
  background-color: #f4f4f4 !important; /* 鼠标悬停时的背景色 */
  padding: 5px 20px; /* 调整内边距，减小高度和宽度 */
  height: 30px; /* 如果需要限制高度，调整这里的数值 */
  line-height: 30px; /* 保持文本垂直居中 */
}
/* 主内容区域 */
.main-content {
  background-color: #ffffff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1); /* 内容盒子添加阴影 */
  height: 100vh; /* 确保内容区域填满屏幕 */
  overflow-y: auto; /* 如果内容超出则允许纵向滚动 */
}

/* 内容显示区 */
.side-content {
  padding: 20px;
  background-color: #ffffff;
  margin-bottom: 20px;
  border-radius: 8px; /* 每个内容区添加圆角 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05); /* 内容区添加微弱阴影 */
}

/* 菜单样式 */
.el-menu-item {
  font-size: 1.2em;
}

.el-main {
  padding: 20px;
}

/* 主内容区域样式 */
.main-content {
  padding: 20px;
  background-color: #f4f4f4;
  height: 100vh;
  overflow-y: auto;
}

/* 侧栏和主内容区域 */
.el-container {
  display: flex;
  height: 100vh;
}
</style>
