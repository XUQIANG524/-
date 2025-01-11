<template>
  <div class="first-box">
    <div class="first-page">
      <div class="first">
        <div style="width: 35%">
          <div
            class="title1"
            style="color: #333333; font-size: 45px; font-weight: 550; margin-bottom: 10px"
          >
          一键下单，尽享美味！ <br />
          </div>
          <div
            class="title2"
            style="color: #333333; font-size: 45px; font-weight: 550; margin-bottom: 10px"
          >
            干饭实力派就选<span style="color: white; font-weight: 700;">食立派</span><br />
          </div>

          <div style="color: #757373; font-size: 15px; font-weight: 250; margin-bottom: 50px">
            我们的外卖平台不仅仅是送餐，更是您与美食的桥梁。通过智能化的订单管理系统
            <br />
            结合最专业的配送团队，为您带来极致的服务体验。开启美味之旅！
          </div>
          <div>
            <el-button
              style="
                background-color: #ff5733;
                color: #fff;
                width: 23%;
                height: 50px;
                font-size: 15px;
                font-weight: 500;
                line-height: 50px;
                border-radius: 8px;
              "
              @click="$router.push('/login')"
              >开始</el-button
            >
          </div>
        </div>
        <div class="bg2">
          <bg2Svg />
        </div>
      </div>
      <bgSvg class="bg" />
      <bgSvg class="bg" />
    </div>
  </div>
</template>

<script setup>

</script>

<style scoped lang="less">
.first-box {
  width: 100%;
  height: 100%;
  background-image: url("/images/外卖员.jpg");
  background-size: cover;
  background-position: center;
  .first-page {
    width: 100%;
    height: 100%;
    background-color: hsla(208, 100%, 94%, 0.34);
    border-radius: 0 0 30px 30px;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    .first {
      width: 100%;
      height: 100%;
      z-index: 10;
      display: flex;
      justify-content: space-evenly;
      align-items: center;
      .end {
        width: 50%;
        height: 200px;
        background-color: #fff;
      }
      .title1 {
        transition: 0.3s;
        animation: typer 4s steps(15) 1 forwards;
        width: 0;
        overflow: hidden;
        white-space: nowrap;
      }
      .title2 {
        transition: 0.3s;
        animation: typer 4s 4s steps(14) 1 forwards;
        width: 0;
        overflow: hidden;
        white-space: nowrap;
      }
      @keyframes typer {
        0% {
        }
        100% {
          width: 20ch;
        }
      }
    }

    .bg {
      z-index: -10;
      position: absolute;
      top: 40%;
      left: -12%;
      opacity: 0.6;
    }
    .bg:nth-child(2) {
      position: absolute;
      left: 64%;
    }
  }
}
</style>
