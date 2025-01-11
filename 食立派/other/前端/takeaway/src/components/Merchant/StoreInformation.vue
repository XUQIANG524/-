<template>
  <div class="store-information">
    <div class="store-info">
      <div class="avatar-container">
        <el-avatar shape="square" :size="100" :src="store.avatar" @click="triggerFileInput"
          style="cursor: pointer; border-radius: 8px" />
        <input type="file" ref="fileInput" @change="uploadAvatar" accept="image/*" style="display: none" />
      </div>

      <div class="info-item">
        <label for="storeName">店铺名称:</label>
        <el-input v-model="store.name" id="storeName" placeholder="请输入店铺名称" clearable size="medium" />
      </div>
      <div class="info-item">
        <label for="storeAddress">店铺地址:</label>
        <el-input v-model="store.address" id="storeAddress" placeholder="请输入店铺地址" clearable size="medium" />
      </div>
      <div class="info-item">
        <label for="storePhone">联系电话:</label>
        <el-input v-model="store.phone" id="storePhone" placeholder="请输入联系电话" clearable size="medium" />
      </div>
      <div class="info-item">
        <label for="storeHours">营业时间:</label>
        <el-input v-model="store.hours" id="storeHours" placeholder="请输入营业时间" clearable size="medium" />
      </div>
      <div class="info-item">
        <label for="storeDescription">商家描述:</label>
        <el-input v-model="store.description" id="storeDescription" placeholder="请输入商家描述" clearable size="medium" />
      </div>
    </div>

    <div class="button-group">
      <el-button type="primary" @click="confirmChanges" size="large">
        确认修改
      </el-button>
      <el-button @click="cancelChanges" size="large">
        取消
      </el-button>
    </div>
  </div>
</template>


<script>
import axios from "axios";
import qs from "qs";
import { ref, reactive } from "vue";

export default {
  data() {
    return {
      store: {
        avatar: "",
        name: "",
        address: "",
        phone: "",
        hours: "",
        description: "",
      },
      originalStoreData: {}, // 用于存储原始数据
      File:null,
    };
  },
  methods: {
    // 用于触发文件选择框
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    // 上传头像
    uploadAvatar(event) {
      this.File = event.target.files[0];
      const file = event.target.files[0];
      if (file) {
        this.store.avatar = URL.createObjectURL(file);
      }
    },
    // 保存商家信息
    async saveStoreInfo() {
      console.log("保存商家信息:", this.store);
      const token = sessionStorage.getItem('token');
      console.log('token:', token);
      const response = await axios.post(
        "http://localhost:8088/api/merchants/merchantupdateinfo",
        qs.stringify({
          name: this.store.name,
          address: this.store.address,
          phone: this.store.phone,
          time: this.store.hours,
          description: this.store.description,
        }),
        {
          headers: {
            "tjpu22s10": token,
          },
        }
      );
      const file = this.File;
      console.log("文件选择:", this.File);
      const form = new FormData()
      form.append('file', file)
      axios.post("http://localhost:8088/api/merchants/merchantupload", form, {
        headers: {
          "tjpu22s10": token,
        },
      }).then(
        (res) => {
          if (res.data.code == 200) {
            console.log('上传成功');
          } else {
            console.log(res.data.msg);
          }

        },
        (error) => {
          console.log('上传失败,请检查网络');
        }

      )
    },
    // 确认修改
    confirmChanges() {
      console.log("修改确认:", this.originalStoreData);
      this.originalStoreData = { ...this.store }; // 确认修改后保存当前数据
      console.log("修改确认:", this.store);
      this.saveStoreInfo(); // 保存修改后的信息
    },
    // 取消修改
    cancelChanges() {
      this.store = { ...this.originalStoreData }; // 取消时恢复原始数据
      console.log("修改已取消，恢复原始数据:", this.store);
    },
  },
  mounted() {
    const token = sessionStorage.getItem('token');
    console.log('token:', token);
    axios
      .post("http://localhost:8088/api/merchants/getMerchant", {}, {
        headers: {
          "tjpu22s10": token,
        },
      })
      .then((response) => {
        this.store.avatar = 'http://106.3.99.64:20028/tjpu22s06/' + response.data.data.image;
        this.store.name = response.data.data.shopName;
        this.store.phone = response.data.data.phone;
        this.store.address = response.data.data.address;
        this.store.hours = response.data.data.time;
        this.store.description = response.data.data.description;
        console.log("获取商家信息成功:", this.store);
        this.originalStoreData = { ...this.store };
      })
      .catch((error) => { })
  },
};
</script>


<style scoped>
.store-information {
  padding: 20px;
  background-color: #ffffff;
  font-family: "Arial", sans-serif;
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
  /* 启用垂直滚动 */
}

.store-info {
  margin-bottom: 5px;
  width: 100%;
}

.info-item {
  margin-bottom: 3px;
  width: 100%;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
  color: #333;
}

.el-input {
  width: 80%;
  border-radius: 6px;
  font-size: 14px;
  padding: 12px;
}

.el-input__inner {
  height: 40px;
  line-height: 40px;
  /* 让输入框的文字垂直居中 */
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.avatar-container {
  margin-bottom: 20px;
}

.save-button {
  text-align: left;
  margin-top: 20px;
}

.save-button .el-button {
  padding: 10px 25px;
  font-size: 16px;
  background-color: #ff5733;
  border: none;
  border-radius: 5px;
  color: white;
  cursor: pointer;
}

.save-button .el-button:hover {
  background-color: #e64a24;
}

.el-avatar {
  border: 2px solid #ddd;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* 按钮组样式 */
.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.button-group .el-button {
  width: 48%;
  padding: 10px 25px;
  font-size: 16px;
  background-color: #ff5733;
  /* 默认背景色 */
  border: none;
  /* 清除边框 */
  border-radius: 5px;
  color: white;
  /* 文字颜色 */
}

.button-group .el-button:hover {
  background-color: #e04f24;
  /* 悬浮时变为深红色 */
}

.button-group .el-button:focus {
  background-color: #e04f24;
  /* 聚焦时背景色为深红色 */
}

.button-group .el-button[type="primary"] {
  background-color: #4caf50;
  /* 原色为绿色 */
}

.button-group .el-button[type="primary"]:hover {
  background-color: #45a049;
  /* 悬浮时背景色变深 */
}
</style>
