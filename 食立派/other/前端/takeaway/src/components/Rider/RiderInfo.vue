<template>
  <div class="user-profile">
    <div class="profile-card">
      <!-- 头像部分 -->
      <div class="avatar-container">
        <el-avatar shape="square" :size="100" :src="user.avatar" @click="triggerFileInput"
                   style="cursor: pointer; border-radius: 8px" />
        <input type="file" ref="fileInput" @change="uploadAvatar" accept="image/*" style="display: none" />
      </div>

      <!-- 用户基本信息 -->
      <div class="profile-info">
        <div class="info-item"><strong>用户名：</strong>{{ user.workerName }}</div>
        <div class="info-item"><strong>手机号：</strong>{{ user.phone }}</div>
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
          <input type="text" v-model="editableUser.workerName" id="workerName" />
        </div>
        <div class="input-group">
          <label for="phone">手机号</label>
          <input type="text" v-model="editableUser.phone" id="phone" />
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
          <input
            type="password"
            v-model="newPassword.oldPassword"
            id="oldPassword"
          />
        </div>
        <div class="input-group">
          <label for="newPassword">新密码</label>
          <input
            type="password"
            v-model="newPassword.newPassword"
            id="newPassword"
          />
        </div>
        <div class="input-group">
          <label for="confirmPassword">确认密码</label>
          <input
            type="password"
            v-model="newPassword.confirmPassword"
            id="confirmPassword"
          />
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
import { onMounted } from "vue";

const token = sessionStorage.getItem("token");

export default {
  name: "UserProfile",
  data() {
    return {
      user: {
        avatar: "",
        workerName: "",
        phone: "",
        email: "",
        address: "",
      },
      File: null,
      editableUser:  { ...this.user },  // 初始为空对象
      isModalVisible: false,
      isPasswordModalVisible: false,
      newPassword: {
        oldPassword: "",
        newPassword: "",
        confirmPassword: "",
      },
    };
  },

  mounted() {
    this.fetchUserInfo();  // 在组件挂载时获取用户信息
  },

  methods: {
    async fetchUserInfo() {
      try {
        const workerId = sessionStorage.getItem("workerId");
        const token = sessionStorage.getItem("token");

        const response = await axios.get(`http://localhost:8088/api/workers/getWorker/${workerId}`, {
          headers: {
            "Authorization": `Bearer ${token}`
          }
        });

        if (response.status === 200) {
          const avatarPath = response.data.avatar;
          // 如果 avatar 已经是完整的 URL，则无需再次拼接
          const avatarUrl = avatarPath.startsWith('http://106.3.99.64:20028/tjpu22s06/') ? avatarPath : 'http://106.3.99.64:20028/tjpu22s06/' + avatarPath;

          this.user = {
            avatar: avatarUrl,
            workerName: response.data.workerName,
            phone: response.data.phone,
            address: response.data.address,
          };

          // 更新 editableUser
          this.editableUser = { ...this.user };

          // // 调试信息
          // console.log("获取的用户信息:", this.user);
          // console.log("用户头像路径:", this.user.avatar);
        } else {
          // 处理请求失败的情况
          alert("获取用户信息失败，请稍后重试。");
        }
      } catch (error) {
        console.error("获取用户信息失败:", error);
        alert("获取用户信息失败，请稍后重试。");
      }
    },
    // 用于触发文件选择框
    triggerFileInput() {
      console.log('触发了文件选择框');
      this.$refs.fileInput.click();
    },
    // 上传头像
    uploadAvatar: async function(event) {
      console.log("选中的文件:", event.target.files); // 打印文件列表

      if (!event.target.files || event.target.files.length === 0) {
        console.error('没有选择文件');
        return;
      }

      const file = event.target.files[0]; // 获取第一个文件
      const form = new FormData();
      form.append('file', file);

      console.log("准备上传的文件:", file);

      try {
        // 修改为正确的地址
        const uploadResponse = await axios.post("http://localhost:8088/api/workers/workerUploadAvatar", form, {
          headers: {
            "tjpu22s10": token, // 添加 token 到请求头
          },
        });

        if (uploadResponse.data.code === 200) {
          console.log('头像上传成功:', uploadResponse.data);

          // 获取相对路径
          const relativePath = uploadResponse.data.msg; // 假设 msg 字段返回的是相对路径

          // 确保拼接路径时没有重复的斜杠
          const baseUrl = "http://106.3.99.64:20028/tjpu22s06/";
          this.user.avatar = `${baseUrl}${relativePath.replace(/^\//, '')}`;
          console.log('更新后的完整头像路径:', this.user.avatar);

        } else {
          console.log('头像上传失败:', uploadResponse.data.msg);
          alert('头像上传失败');
        }
      } catch (error) {
        console.error('上传过程中发生错误:', error);
        alert('上传失败，请稍后重试');
      }
    },
    // 开启编辑个人信息模态框
    editProfile() {
      this.editableUser = { ...this.user };
      this.isModalVisible = true;
    },
    // 保存修改后的个人信息
    async saveChanges() {
      try {

        const workerId = sessionStorage.getItem("workerId");
        console.log("提取的用户ID:", workerId);
        console.log("editableUser:", this.editableUser);
        // 更新用户信息（包括头像相对路径）
        const response = await axios({
          method: 'put',
          url: `http://localhost:8088/api/workers/updateWorker/${workerId}`,
          data: this.editableUser, // 发送更新后的 editableUser 数据
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
          },
        });

        if (response.status === 200) {
          console.log('用户信息更新成功:', response.data);
          this.user = { ...this.editableUser }; // 更新本地用户信息
          this.closeModal();
          alert("信息更新成功");
        }
      } catch (error) {
        if (error.response) {
          console.error("更新信息失败:", error.response.data);
          alert(`更新信息失败: ${error.response.data}`);
        } else {
          console.error("更新信息失败:", error.message);
          alert("更新信息失败，请稍后重试。");
        }
      }
    }
,

    // 关闭编辑模态框
    closeModal() {
      this.isModalVisible = false;
    },
    // 修改密码
    changePassword() {
      this.isPasswordModalVisible = true;
    },
    // 保存密码修改
    savePasswordChanges() {
      if (this.newPassword.newPassword !== this.newPassword.confirmPassword) {
        alert("新密码与确认密码不匹配！");
        return;
      }
      this.closePasswordModal();
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

