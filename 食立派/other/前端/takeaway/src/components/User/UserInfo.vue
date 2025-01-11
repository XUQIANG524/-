<template>
  <div class="user-profile">
    <div class="profile-card">
      <!-- 头像部分 -->
      <div class="avatar-section">
        <img :src="user.avatar" alt="User Avatar" class="user-avatar" @click="editAvatar" />
      </div>

      <!-- 用户基本信息 -->
      <div class="profile-info">
        <div class="info-item"><strong>用户名：</strong>{{ user.name }}</div>
        <div class="info-item"><strong>手机号：</strong>{{ user.phone }}</div>
        <div class="info-item"><strong>邮箱：</strong>{{ user.email }}</div>
        <div class="info-item"><strong>地址：</strong>{{ user.address }}</div>
      </div>

      <div class="btn-container">
        <button class="edit-btn" @click="editProfile">编辑个人信息</button>
        <button class="edit-btn" @click="changePassword">修改密码</button>
      </div>
    </div>

    <!-- 编辑个人信息模态框 -->
    <div v-if="isModalVisible" class="modal-overlay">
      <div class="modal">
        <h3>编辑个人信息</h3>

        <div class="input-group">
          <label for="name">用户名</label>
          <input type="text" v-model="editableUser.name" id="name" />
        </div>
        <div class="input-group">
          <label for="phone">手机号</label>
          <input type="text" v-model="editableUser.phone" id="phone" />
        </div>
        <div class="input-group">
          <label for="email">邮箱</label>
          <input type="email" v-model="editableUser.email" id="email" />
        </div>
        <div class="input-group">
          <label for="address">地址</label>
          <input type="text" v-model="editableUser.address" id="address" />
        </div>

        <div class="btn-container">
          <button @click="saveChanges">保存</button>
          <button @click="closeModal">取消</button>
        </div>
      </div>
    </div>

    <!-- 修改密码模态框 -->
    <div v-if="isPasswordModalVisible" class="modal-overlay">
      <div class="modal">
        <h3>修改密码</h3>

        <div class="input-group">
          <label for="oldPassword">旧密码</label>
          <input type="password" v-model="Password.oldPassword" id="oldPassword" />
        </div>
        <div class="input-group">
          <label for="newPassword">新密码</label>
          <input type="password" v-model="Password.newPassword" id="newPassword" />
        </div>
        <div class="input-group">
          <label for="confirmPassword">确认密码</label>
          <input type="password" v-model="Password.confirmPassword" id="confirmPassword" />
        </div>

        <div class="btn-container">
          <button @click="savePasswordChanges">保存</button>
          <button @click="closePasswordModal">取消</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import qs from "qs";


export default {
  data() {
    return {
      user: {
        avatar: "/images/lb.jpg", // 默认头像:src="require('@/assets/images/ts.jpg')"
        name: sessionStorage.getItem("username"),
        phone: sessionStorage.getItem("phone"),
        email: "zhangsan@example.com",
        address: sessionStorage.getItem("address"),
      },
      editableUser: { ...this.user },
      isModalVisible: false,
      isPasswordModalVisible: false,
      Password: {
        oldPassword: "",
        newPassword: "",
        confirmPassword: "",
      },
    };
  },
  methods: {
    // 开启编辑个人信息模态框
    async savePasswordChanges() {
      const token = sessionStorage.getItem('token');
      console.log("登录 token：", token);
      if (this.Password.newPassword !== this.Password.confirmPassword) {
        alert("新密码与确认密码不匹配！");
        return;
      }
      const response = await axios.post(
        "http://localhost:8088/logic/user/updatePassword",
        qs.stringify({
          old_pwd: this.Password.oldPassword,
          new_pwd: this.Password.newPassword,
        }),
        {
          headers: {
            "tjpu22s10": token,
          },
        }
      );
      this.closePasswordModal();
      console.log("后端返回的响应数据：", response.data);
    },
    // 保存修改后的个人信息
    async saveChanges() {
      this.user = { ...this.editableUser };
      const token = sessionStorage.getItem('token');
      console.log("用户名：", this.user.name);
      console.log("登录 token：", token);
      const response = await axios.post(
        "http://localhost:8088/logic/user/updateUserInfo",
        qs.stringify({
          username: this.user.name,
          phone: this.user.phone,
          address: this.user.address,
        }),
        {
          headers: {
            "tjpu22s10": token,
          },
        }
      );
      console.log("后端返回的响应数据：", response.data);
      sessionStorage.setItem("username", this.user.name);
      sessionStorage.setItem("phone", this.user.phone);
      sessionStorage.setItem("address", this.user.address);
      this.closeModal();
    },
    editProfile() {
      this.editableUser = { ...this.user };
      this.isModalVisible = true;
    },
    // 关闭编辑模态框
    closeModal() {
      this.isModalVisible = false;
    },
    // 修改密码
    changePassword() {
      this.isPasswordModalVisible = true;
    },
    
    // 关闭密码修改模态框
    closePasswordModal() {
      this.isPasswordModalVisible = false;
      this.newPassword = {
        oldPassword: "",
        newPassword: "",
        confirmPassword: "",
      };
    },
    // 更改头像
    editAvatar() {
      // 这里只是示例，可以弹出文件选择框上传头像
      alert("头像修改功能待实现");
    },
  },
};
</script>

<style scoped>
.user-profile {
  padding: 20px;
  font-family: "Arial", sans-serif;
  background-color: #f4f6f9;
}

h2 {
  font-size: 28px;
  margin-bottom: 20px;
  color: #333;
}

.profile-card {
  background-color: #ffffff;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  margin-bottom: 30px;
}

.avatar-section {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  cursor: pointer;
}

.user-avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  margin-right: 20px;
}

.edit-btn {
  background-color: #ff5733;
  color: white;
  border: none;
  padding: 12px 25px;
  font-size: 16px;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  width: 13%;
  height: 30%;
  margin-top: 10px;
}

.edit-btn:hover {
  background-color: #e04a2e;
}

.profile-info {
  margin-top: 20px;
}

.info-item {
  font-size: 16px;
  margin-bottom: 10px;
  color: #555;
}

.btn-container {
  display: flex;
  gap: 15px;
}

input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  margin-bottom: 15px;
  font-size: 16px;
}

button {
  background-color: #ff5733;
  color: white;
  border: none;
  padding: 12px 25px;
  font-size: 16px;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #e04a2e;
}

/* 编辑信息模态框 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background-color: #ffffff;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  width: 450px;
  max-width: 100%;
}

.modal h3 {
  font-size: 24px;
  margin-bottom: 25px;
  color: #333;
}

.modal label {
  display: block;
  margin-bottom: 8px;
  color: #555;
  font-weight: 500;
}

.input-group {
  margin-bottom: 20px;
}

input:focus {
  outline: none;
  border-color: #ff5733;
}

input::placeholder {
  color: #aaa;
}
</style>
