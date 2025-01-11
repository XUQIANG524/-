<template>
  <div class="first-page">
    <div class="image-header">
      <div
        class="header-content"
        style="
          display: flex;
          flex-direction: column;
          justify-content: flex-end;
          align-items: center;
          height: 100%;
          width: 100%;
          padding-bottom: 80px;
        "
      >
        <h1 style="font-size: 68px; margin-bottom: 40px; color: whitesmoke">
          合作赋能,共创美好生活
        </h1>
        <p style="font-size: 38px; color: whitesmoke">
          食立派与您共同打造优质外卖生态
        </p>
      </div>
    </div>
    <div class="content">
      <div class="item" style="background-image: url('/images/城市.jpg')">
        <div class="nested-content">
          <h1>城市代理</h1>
          <p>开发外卖商家合作洽谈,订单配送等业务</p>
          <p>帮助商家入驻,三方合作,共享成果</p>
        </div>
      </div>
      <div class="item" style="background-image: url('/images/骑手.jpg')">
        <div class="nested-content">
          <h1>骑手配送</h1>
          <p>当骑手,做配送加盟商</p>
          <p>加入实时加盟网络,高收入高回报</p>
        </div>
      </div>
      <div class="item" style="background-image: url('/images/加盟.jpg')">
        <div class="nested-content">
          <h1>品牌加盟</h1>
          <p>平台优势,成交量有保证</p>
        </div>
      </div>
      <div class="item" style="background-image: url('/images/商家.jpg')">
        <div class="nested-content">
          <h1>商家入驻</h1>
          <p>开店,享受平台优惠</p>
        </div>
      </div>
    </div>

    <!-- 添加 footer -->
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
.image-header {
  width: 100%;
  background-image: url("/images/blocks.jpg");
  background-size: cover;
  background-position: center;
  height: 400px;
  justify-content: flex-end !important;
  align-items: center;
  display: flex;
}

.content {
  width: 70%;
  margin: 0 auto;
  padding-top: 50px;
  background-color: #fff;
  height: 57%;
  display: flex;
  flex-wrap: wrap; /* 使子元素换行 */
  justify-content: space-evenly; /* 均匀分配盒子 */
  align-items: center;
  margin-bottom: 50px;
}

.content div {
  width: 48%; /* 调整宽度减少间隙 */
  height: 48%; /* 高度根据需要调整 */
  display: flex;
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
.item {
  display: flex;
  flex-direction: column; /* 垂直排列 */
  align-items: center; /* 居中对齐 */
  justify-content: center; /* 垂直居中 */
  height: 100%;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.3);
  transition: box-shadow 0.3s ease;
}

.nested-content {
  background-color: whitesmoke;
  width: 100% !important;
  height: 30%;
  margin-top: 220px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.nested-content p {
  font-size: 15px;
  color: gray;
}
</style>
