<template>
  <div class="welcome">
    <div class="content">
      <!-- 动画加载旋转Logo -->
      <div class="spinner"></div>
      <p class="title">欢迎进入食立派大数据后台</p>
      <p class="subtitle">将为您呈现最新的数据分析</p>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isActive: true, // 标志位，判断组件是否仍处于活动状态
      timeoutId: null, // 保存定时器ID，方便清除
    };
  },
  mounted() {
    // 模拟加载过程，加载完成后跳转到主页面
    this.timeoutId = setTimeout(() => {
      if (this.isActive) {
        this.$router.push('/data'); // 跳转到主页
      }
    }, 3000); // 3秒后加载完成
    this.enterFullscreen(); // 请求全屏
  },
  beforeDestroy() {
    // 在组件销毁前，清除定时器并设置标志位为 false
    this.isActive = false; // 确保销毁后标志位变为 false
    clearTimeout(this.timeoutId); // 清除定时器
  },
  beforeRouteLeave(to, from, next) {
    // 如果离开当前页面，确保清除标志位和定时器
    this.isActive = false;
    clearTimeout(this.timeoutId);
    next(); // 执行路由跳转
  },
  methods: {
    // 请求全屏
    enterFullscreen() {
      const element = document.documentElement; // 获取 `<html>` 元素或其他容器元素

      // 兼容性处理，不同浏览器的前缀
      if (element.requestFullscreen) {
        element.requestFullscreen(); // 标准方式
      } else if (element.mozRequestFullScreen) {
        element.mozRequestFullScreen(); // Firefox
      } else if (element.webkitRequestFullscreen) {
        element.webkitRequestFullscreen(); // Chrome, Safari, Opera
      } else if (element.msRequestFullscreen) {
        element.msRequestFullscreen(); // IE/Edge
      } else {
        console.error("浏览器不支持全屏 API");
      }
    },
  },
};
</script>

<style scoped>
/* 样式保持不变 */
.welcome {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: radial-gradient(circle, rgba(30, 42, 56, 0.8), rgba(74, 109, 145, 0.8)); /* 径向渐变背景 */
  display: flex;
  justify-content: center;
  align-items: center;
  color: #fff;
  font-size: 2rem;
  font-weight: bold;
  z-index: 1000;
  overflow: hidden;
  animation: backgroundAnimation 5s ease-in-out infinite; /* 背景渐变动画 */
  animation-duration: 5s;
}

@keyframes backgroundAnimation {
  0% {
    background: radial-gradient(circle, rgba(30, 42, 56, 0.8), rgba(74, 109, 145, 0.8));
  }
  50% {
    background: radial-gradient(circle, rgba(74, 109, 145, 0.8), rgba(30, 42, 56, 0.8));
  }
  100% {
    background: radial-gradient(circle, rgba(30, 42, 56, 0.8), rgba(74, 109, 145, 0.8));
  }
}

.content {
  text-align: center;
  animation: fadeIn 2s ease-out;
}

.spinner {
  border: 6px solid #f3f3f3; /* 外圈颜色 */
  border-top: 6px solid #ff9f00; /* 内圈颜色 */
  border-radius: 50%;
  width: 80px;
  height: 80px;
  animation: spin 2s linear infinite; /* 添加动画 */
  margin-bottom: 40px;
  position: relative;
  top: 50%;
  left: 43%;
  transform: translate(-50%, -50%);
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.title {
  font-size: 4rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 5px;
  animation: fadeInUp 1.5s ease-out;
  color: #fff;
  text-shadow: 0 0 15px rgba(255, 159, 0, 0.7);
  margin-top: 20px;
}

@keyframes fadeInUp {
  0% {
    opacity: 0;
    transform: translateY(30px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

.subtitle {
  font-size: 1.5rem;
  font-weight: 400;
  margin-top: 10px;
  color: #f0f0f0;
  letter-spacing: 2px;
  opacity: 0;
  animation: fadeInUp 2s ease-out 1.5s forwards;
}

@keyframes fadeIn {
  0% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}

@keyframes fadeInUp {
  0% {
    opacity: 0;
    transform: translateY(20px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
