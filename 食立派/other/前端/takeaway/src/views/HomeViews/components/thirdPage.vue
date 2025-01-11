<template>
  <div class="third-box">
    <div class="third-page">
      <div class="third">
        <div></div>
        <div>
          <div></div>
          <div></div>
          <div></div>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped>
.third-box {
  width: 100%;
  height: 90%;

  .third-page {
    width: 100%;
    height: 90%;
    background-color: hsla(208, 100%, 94%, 0.34);
    border-radius: 0 0 30px 30px;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;

    .third {
      width: 100%;
      height: 100%;
      z-index: 10;
      display: flex;
      justify-content: space-evenly;
      align-items: center;
    }
  }
}
</style>
