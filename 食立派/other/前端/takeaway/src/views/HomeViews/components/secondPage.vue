<template>
  <div class="second-box">
    <div class="second-page">
      <div class="end">
        <div
          style="
            font-size: 40px;
            font-weight: 500;
            font-family: DM Sans;
            line-height: 42px;
            text-align: center;
            letter-spacing: 0px;
          "
        >
        智能推荐，让外卖更懂你！
        </div>
        <div style="color: #757373; font-size: 16px; margin-top: 15px">
          通过智能算法推荐你最喜欢的餐品，精准、快速，让外卖更加贴心。
        </div>
      </div>
      <div class="second">
        <div class="second-item">
          <div class="second-item-1-1">
            <bg3Svg />
          </div>
          <div class="second-item-1-2">
            <div style="font-size: 32px; font-weight: bold">
              让智能推荐 <br />
              引领您的外卖选择
            </div>
            <div style="font-size: 16px; color: #757373">
              我们的智能推荐算法可以根据您的喜好、历史订单和偏好，精确匹配最佳餐品。 <br />
              无论是口味偏好还是时段选择,<br />
              我们都能为您提供最贴心的推荐。
            </div>
            <div style="font-size: 16px; color: #757373">
              通过大数据分析与机器学习，<br />
              我们为您呈现个性化的外卖推荐，节省您的时间，提升您的就餐体验，让每一次选择都充满惊喜。
            </div>
          </div>
        </div>
        <div class="second-item">
          <div class="second-item-2-1">
            <div style="font-size: 32px; font-weight: bold">
              加入我们，<br />
              共享外卖新时代
            </div>
            <div style="font-size: 16px; color: #757373">
              无论您是商家，还是骑手，我们都为您提供无限的机会与可能。<br />
              通过我们的平台，商家可以触及更多潜在客户，实现销售增长；<br />
              骑手则能够轻松接单，赚取丰厚回报，享受灵活工作时间。
            </div>
            <div style="font-size: 16px; color: #757373">
              为商家提供精准的用户匹配和智能推荐，让每一笔订单都更高效；<br />
              为骑手提供高效的配送系统和实时导航，让每一次配送都更顺畅。
            </div>
          </div>
          <div  class="second-item-2-2">
            <bg2Svg />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import bg3Svg from '../svg/bg3Svg.vue'
import bg2Svg from '../svg/bg2Svg.vue'


document.addEventListener('DOMContentLoaded', function () {
  const target = document.querySelector('.second-item-1-2')

  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        console.log('Target div is now visible')
        target.classList.add('.visible')
      } else {
        console.log('Target div is not visible')
        target.classList.remove('visible')
      }
    })
  })

  observer.observe(target)
})
</script>

<style lang="less" scoped>
.second-box {
  width: 100%;
  height: 100%;

  .second-page {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;

    .end {
      width: 39%;
      height: 28vh;
      background-color: #fff;
      position: relative;
      top: -100px;
      border-radius: 20px;
      box-shadow: 5px 10px 50px 0px rgba(204, 221, 255, 0.2);
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
    }
    .second {
      width: 100%;
      height: 100%;
      display: flex;
      justify-content: space-evenly;
      align-items: center;
      flex-direction: column;
      .second-item {
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: space-evenly;
        align-items: center;
      }
    }
  }
}
</style>
