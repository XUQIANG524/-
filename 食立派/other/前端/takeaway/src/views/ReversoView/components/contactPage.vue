<template>
  <div class="contact-page">
    <div class="contact-header">
      <div class="contact-header-text">联系我们</div>
    </div>
    <div class="contact-image"></div>
    <div class="contact-info-wrapper">
      <!-- 左侧：联系方式 -->
      <div class="contact-info-left-text">
        <div>
          <h1>联系方式</h1>
          <hr>
        </div>
        <div>
          <p>电话：4001095018</p>
          <p>Find系列专属服务热线</p>
          <hr>
        </div>
        <div>
          <p>在线客服</p>
          <p>通过官网或APP联系客服</p>
          <hr>
        </div>
        <div>
          <p>微博</p>
          <p>@官方微博账号</p>
          <hr>
        </div>
        <div>
          <p>微信</p>
          <p>官方微信公众号</p>
          <hr>
        </div>
      </div>

      <!-- 右侧：媒体联络 -->
      <div class="contact-info-right">
        <div>
          <h1>媒体联络</h1>
          <hr>
        </div>
        <div>
          <p>海外</p>
          <p>press@oppo.com</p>
          <hr>
        </div>
        <div>
          <p>更多信息</p>
          <p>media@oppo.com</p>
          <hr>
        </div>
        <div>
          <p>合作查询</p>
          <p>partnership@oppo.com</p>
          <hr>
        </div>
        <div>
          <p>支持部门</p>
          <p>support@oppo.com</p>
          <hr>
        </div>
      </div>
    </div>

    <div class="footer">
      <div class="footer-content">
        <div class="footer-links">
          <a href="#" class="footer-link">关于我们</a>
          <a href="#" class="footer-link">隐私政策</a>
          <a href="#" class="footer-link">联系我们</a>
        </div>
        <hr />
        <div class="footer-text">
          <p>&copy; 2025 帅哥刘涛有限公司. All rights reserved.</p>
          <p>京ICP备2025000001号-1</p>
          <p>公司地址：天津市南开区XXX号</p>
          <p>电话：123-456-7890</p>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
</script>
<style scoped>
.contact-page {
  height: 100%;
  width: 100%; /* 确保宽度填满父容器 */
  background-color: #ffffff;
  display: flex;
  flex-direction: column; /* 设置子元素垂直排列 */
  align-items: center; /* 设置子元素水平居中 */
  overflow-x: hidden;
}
.contact-header {
  background-color: rgb(254, 255, 255);
  background-size: cover;
  height: 15%;
  width: 100%;
  position: relative;
}
.contact-header-text {
  position: absolute;
  top: 55%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: rgb(0, 0, 0);
  font-size: 65px;
  font-weight: bold;
  background-color: rgb(255, 255, 255);
}
.contact-image {
  height: 40%;
  width: 80%;
  background-image: url("/images/联系我们.jpg");
  background-size: cover;
  background-position: center; /* 调整显示区域，向上移动10% */
}

/* 父容器 */
/* 父容器 */
.contact-info-wrapper {
  display: flex; /* 使用flex布局使两列横向排列 */
  gap: 20px; /* 左右两列之间的间距 */
  width: 80%; /* 父容器宽度占满 */
  justify-content: space-between; /* 让左右两列的子盒子平分空间 */
  height: 40%;
  margin-top: 50px;
  margin-bottom: 30px;
}

/* 左侧 */
.contact-info-left-text {
  display: grid;
  grid-template-rows: repeat(5, 1fr); /* 5行，每行高度平分 */
  gap: 20px; /* 每个格子之间的间距 */
  width: 50%; /* 占父容器宽度的50% */
  height: 100%; /* 确保左侧内容占满父容器的高度 */
}

/* 右侧 */
.contact-info-right {
  display: grid;
  grid-template-rows: repeat(5, 1fr); /* 5行，每行高度平分 */
  gap: 20px; /* 每个格子之间的间距 */
  width: 50%; /* 占父容器宽度的50% */
  height: 100%; /* 确保右侧内容占满父容器的高度 */
}

.contact-info-left-text > div,
.contact-info-right > div {
  padding: 10px;
  align-items: center; /* 垂直居中对齐 */
}

.contact-info-left-text p,
.contact-info-right p {
  margin: 5px 0; /* 为p标签添加适当的上下间距 */
  font-size: 24px;
}

/* footer 样式 */
.footer {
  background-color: #2e2125; /* 深色背景 */
  color: #fff;
  padding: 20px;
  font-size: 14px;
  text-align: center;
  position: relative;
  height: 130px; /* 高度根据需要调整 */
  width: 97.5%;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
}

.footer-links {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 10px;
}

.footer-link {
  color: #fff;
  text-decoration: none;
  font-size: 16px;
  transition: color 0.3s ease;
}

.footer-link:hover {
  color: #ff5733; /* 鼠标悬停时，变色 */
}

.footer-text {
  font-size: 12px;
  color: #ddd;
}

.footer-text p {
  margin: 5px 0;
}
.h2 {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
}
</style>
