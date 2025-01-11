<template>
  <div class="login-wrapper">
    <div class="login-container">
      <h2>登录</h2>
      <form @submit.prevent="handleLogin">
        <div class="input-group">
          <label for="username">用户名</label>
          <input
            type="text"
            id="username"
            v-model="username"
            placeholder="请输入用户名"
            required
          />
        </div>

        <div class="input-group">
          <label for="password">密码</label>
          <input
            type="password"
            id="password"
            v-model="password"
            placeholder="请输入密码"
            required
          />
        </div>

        <button type="submit" class="btn">登录</button>
        <a href="#" class="forgot-password">忘记密码?</a>
      </form>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import qs from "qs";
import { useToast } from "vue-toastification"; // 导入 useToast

export default {
  data() {
    return {
      username: "",
      password: "",
      loginError: false,
      errorMessage: "",
    };
  },
  methods: {
    async handleLogin() {
      const toast = useToast(); // 获取 toast 实例

      try {
        const response = await axios.post(
          "http://localhost:8088/logic/plat/UserLogin",
          qs.stringify({
            username: this.username,
            pwd: this.password,
          }),
          {
            headers: {
              "Content-Type": "application/x-www-form-urlencoded",
            },
          }
        );

        console.log("后端返回的响应数据1：", response.data);

        sessionStorage.setItem("role", response.data.data.role);
        sessionStorage.setItem("username", response.data.data.username);
        sessionStorage.setItem("phone", response.data.data.phone);
        sessionStorage.setItem("state", response.data.data.state);
        sessionStorage.setItem("address", response.data.data.address);
        sessionStorage.setItem("token", response.data.msg);
        sessionStorage.setItem("merchantId", response.data.merchantId);
        sessionStorage.setItem("workerId", response.data.workerId);

        const state = Number(sessionStorage.getItem("state"));
        const role = sessionStorage.getItem("role");
        const token = sessionStorage.getItem("token");

        console.log("认证状态：", state);
        console.log("用户角色：", role);
        console.log("登录 token：", token);

        if (state) {
          switch (String(role)) {
            case "1":
              toast.success("用户登录成功！"); // 显示成功提示
              this.$router.push("/user/dish");
              break;
            case "2":
              toast.success("商家登录成功！"); // 显示成功提示
              this.$router.push("/merchant/information");
              break;
            case "3":
              toast.success("骑手登录成功！"); // 显示成功提示
              this.$router.push("/rider/menu");
              break;
            case "4":
              toast.success("管理员登录成功！"); // 显示成功提示
              this.$router.push("/system/certification");
              break;
            default:
              toast.error("未知角色");
              break;
          }
        } else {
          switch (String(role)) {
            case "2":
              toast.error("商家尚未认证，请先进行认证！"); // 显示错误提示
              this.$router.push("/merver");
              break;
            case "3":
              toast.error("骑手尚未认证，请先进行认证！"); // 显示错误提示
              this.$router.push("/riderver");
              break;
            default:
              toast.error("未知角色，无法跳转到认证页面");
              break;
          }
        }
      } catch (error) {
        console.error("请求失败：", error);
        this.loginError = true;
        this.errorMessage =
          error.response?.data?.message || "登录失败，请稍后重试。";
        toast.error(this.errorMessage); // 显示登录失败的错误信息
      }
    },
  },
};
</script>

<style scoped>
/* 整体布局 */
.login-wrapper {
  display: flex;
  justify-content:flex-end;
  align-items: center;
  height: 100vh;
  background-image: url('/images/背景.jpg');
  background-size: cover;
  background-position: center;
}

/* 左侧盒子样式 */
.left-box {
  width: 10%; /* 设置宽度为 50% */
  padding: 20px;
  height: 44%;
  background-color: #f9f9f9;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  background-image: url("@/views/login.jpg");
  background-position: center; /* 图片居中 */
  background-repeat: no-repeat; /* 防止图片重复 */
  background-size: cover; /* 背景图片填充容器，保持比例 */
  border-radius: 8px 0 0 8px; /* 左侧圆角 */
}

.left-content {
  text-align: center;
  margin-top: auto;
  text-shadow: 2px 2px 5px rgba(0, 0, 0, 0.3); /* 设置文字阴影 */
}
.left-content h3 {
  color: #ec5454;
  font-size: 1.8em;
  font-weight: bold;
}

.left-content p {
  font-size: 1em;
  color: #ffffff;
}

/* 登录框样式 */
.login-container {
  background-color: #fff;
  padding: 40px;
  border-radius: 0;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
  text-align: center;
  margin-right :120px;
}

.login-container h2 {
  margin-bottom: 20px;
  color: #333;
}

.login-container .input-group {
  margin-bottom: 20px;
  text-align: left;
}

.login-container label {
  display: block;
  font-size: 14px;
  color: #555;
  margin-bottom: 5px;
}

.login-container input[type="text"],
.login-container input[type="password"] {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
}

.login-container input[type="text"]:focus,
.login-container input[type="password"]:focus {
  border-color: #000000;
  outline: none;
}

.login-container .btn {
  width: 100%;
  padding: 12px;
  background-color: #ec5454;
  color: white;
  font-size: 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.login-container .btn:hover {
  background-color: #a04545;
}

.login-container .forgot-password {
  display: block;
  margin-top: 10px;
  font-size: 12px;
  color: #555;
  text-decoration: none;
}

.login-container .forgot-password:hover {
  text-decoration: underline;
}

.el-alert {
  margin: 20px 0 0;
  height: 50px;
}
.el-alert:first-child {
  margin: 0;
}
</style>
