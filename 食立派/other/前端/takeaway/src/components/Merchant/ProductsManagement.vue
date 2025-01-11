<template>
  <div class="product-management">
    <div class="add-dish-btn-container">
      <button class="add-dish-btn" @click="openAddDishModal">添加菜品</button>
    </div>

    <div class="dishes-gallery">
      <div
        v-for="dish in dishes"
        :key="dish.dishId"
        class="dish-card"
        :style="getDishBackground(dish)"
      >
        <h3>{{ dish.dishName }}</h3>
        <p>{{ dish.description }}</p>
        <p style="color: #ff6a00; font-size: 28px; margin-top: 5px;
    background-color: rgba(256, 256, 256, 0.35); padding: 5px 15px; border-radius: 4px; display: inline-block;">
          ¥ {{ dish.price }}
        </p>

        <img v-if="dish.image" :src="dish.image" alt="菜品图片" />
        <div class="dish-actions">
          <button class="edit-btn" @click="editDish(dish)">编辑</button>
          <label class="toggle-switch">
            <input
              type="checkbox"
              :checked="dish.state === 1"
              @change="toggleDishState(dish)"
            />
            <span class="slider round"></span>
            <span class="toggle-label">{{ dish.state === 1 ? '在售' : '售完' }}</span>
          </label>
          <button class="delete-btn" @click="deleteDish(dish.dishId)">删除</button>
        </div>
      </div>
    </div>

    <div v-if="isModalVisible" class="modal-overlay">
      <div class="modal">
        <h3>{{ isEditing ? "修改菜品" : "添加菜品" }}</h3>
        <label for="dishName">菜品名称:</label>
        <input
          type="text"
          v-model="currentDish.dishName"
          id="dishName"
          placeholder="菜品名称"
        />
        <label for="description">菜品描述:</label>
        <textarea
          v-model="currentDish.description"
          id="description"
          placeholder="菜品描述"
        ></textarea>
        <label for="price">单价:</label>
        <input
          type="number"
          v-model="currentDish.price"
          id="price"
          placeholder="单价"
        />
        <label for="state">状态:</label>
        <select v-model="currentDish.state" id="state">
          <option value="1">在售</option>
          <option value="0">售完</option>
        </select>
        <label for="image">菜品图片:</label>
        <div class="upload-area" @dragover.prevent @drop="handleFileDrop">
          <p v-if="!currentDish.image">拖拽或点击选择图片</p>
          <img v-if="currentDish.image" :src="currentDish.image" alt="菜品图片" />
          <input
            type="file"
            id="image"
            @change="handleFileChange"
            ref="fileInput"
            style="display: none"
          />
          <button @click="triggerFileInput">选择文件</button>
        </div>
        <button @click="saveDish">
          {{ isEditing ? "保存修改" : "添加菜品" }}
        </button>
        <button @click="closeModal">取消</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ProductManagement",
  data() {
    return {
      dishes: [],
      isModalVisible: false,
      isEditing: false,
      currentDish: {
        dishId: null,
        dishName: "",
        description: "",
        image: "",
        price: 0,
        state: 1,
      },
    };
  },
  mounted() {
    const token = sessionStorage.getItem('token');
    console.log('token:', token);
    let merchantId = sessionStorage.getItem("merchantId");
    console.log('请求前的商家ID:', merchantId);
    this.fetchDishes();
  },
  methods: {
    async fetchDishes() {
      const merchantId = sessionStorage.getItem("merchantId");
      console.log("请求前的商家ID:", merchantId);
      try {
        const response = await axios.get(`http://localhost:8088/api/dishes/merchant?id=${merchantId}`);
        console.log("获取的菜品数据:", response.data);
        this.dishes = response.data.map(dish => {
          return {
            ...dish,
            originalImage: dish.image,
            image: `http://106.3.99.64:20028/tjpu22s06/${dish.image}`
          };
        });
      } catch (error) {
        console.error("获取菜品数据失败:", error.response ? error.response.data : error.message);
        this.$message.error("获取菜品数据失败，请稍后重试。");
      }
    },

    async uploadDishImage(file) {
      const token = sessionStorage.getItem('token');
      const formData = new FormData();
      formData.append('file', file);
      formData.append('dishId', this.currentDish.dishId);
      try {
        const response = await axios.post('http://localhost:8088/api/dishes/dishUpload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            "tjpu22s10": token,
          }
        });
        console.log("完整的响应数据:", response.data);
        return response.data.msg;
      } catch (error) {
        console.error("图片上传失败:", error);
        this.$message.error("图片上传失败，请稍后重试");
        throw error;
      }
    },

    async toggleDishState(dish) {
      try {
        const newState = dish.state === 1 ? 0 : 1;
        const { image, ...updatedDishData } = dish;  // 直接排除 image 属性
        updatedDishData.state = newState;
        const dishData = {
          ...updatedDishData,  // 保留更新后的 dish 数据
          state: newState,  // 这里 state 已经被更新，不再需要重复
        };
        const response = await axios.put(
          `http://localhost:8088/api/dishes/${dish.dishId}`,
          dishData
        );
        console.log("更新菜品状态数据: ", dishData);
        if (response.data.success) {
          dish.state = newState;
          this.$message.success(`菜品状态已更新为${newState === 1 ? '在售' : '售完'}`);
        }
      } catch (error) {
        console.error("更新菜品状态失败:", error);
        this.$message.error("更新菜品状态失败，请稍后重试");
      }
    },

    openAddDishModal() {
      this.isEditing = false;
      this.currentDish = {dishId: null, dishName: "", description: "", image: ""};
      this.isModalVisible = true;
    },

    editDish(dish) {
      this.isEditing = true;
      this.currentDish = {...dish};
      this.isModalVisible = true;
    },

    async saveDish() {
      try {
        let imagePath = this.currentDish.originalImage;

        if (this.file) {
          imagePath = await this.uploadDishImage(this.file);
        }

        const dishData = {
          merchantId: sessionStorage.getItem("merchantId"),
          dishName: this.currentDish.dishName,
          description: this.currentDish.description,
          price: this.currentDish.price,
          state: this.currentDish.state,
          image: imagePath,
        };

        if (this.isEditing) {
          const response = await axios.put(
            `http://localhost:8088/api/dishes/${this.currentDish.dishId}`,
            dishData
          );
          console.log("更新菜品响应:", dishData);
          if (response.data.success) {
            const index = this.dishes.findIndex(d => d.dishId === this.currentDish.dishId);
            if (index !== -1) {
              this.dishes[index] = {
                ...this.currentDish,
                image: imagePath
              };
            }
            this.$message.success("菜品更新成功");
          }
        } else {
          const response = await axios.post('http://localhost:8088/api/dishes', dishData);

          if (response.data.success) {
            this.dishes.push(response.data.data);
            this.$message.success("菜品添加成功");
          }
        }

        this.closeModal();
        this.fetchDishes();
      } catch (error) {
        console.error(this.isEditing ? "更新" : "添加", "菜品失败:", error);
        this.$message.error(`${this.isEditing ? "更新" : "添加"}菜品失败，请稍后重试`);
      }
    },

    async deleteDish(id) {
      try {
        const response = await axios.delete(`http://localhost:8088/api/dishes?id=${id}`);
        console.log("删除成功:", response.data);

        const index = this.dishes.findIndex(dish => dish.dishId === id);
        if (index !== -1) {
          this.dishes.splice(index, 1);
        }
      } catch (error) {
        console.error("删除失败:", error.response ? error.response.data : error.message);
        this.$message.error("删除菜品失败，请稍后重试。");
      }
    },

    closeModal() {
      this.isModalVisible = false;
      this.currentDish = {dishId: null, dishName: "", description: "", image: ""};
    },

    getDishBackground(dish) {
      return {
        backgroundImage: `url(${dish.image})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        padding: "20px",
        borderRadius: "8px",
        color: "white",
      };
    },

    triggerFileInput() {
      this.$refs.fileInput.click();
    },

    handleFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        const maxSize = 2 * 1024 * 1024;
        if (file.size > maxSize) {
          this.$message.error("图片大小不能超过2MB！");
          return;
        }
        this.previewImage(file);
        this.file = file;
      }
    },

    handleFileDrop(event) {
      event.preventDefault();
      const file = event.dataTransfer.files[0];
      if (file) {
        this.previewImage(file);
        this.file = file;
      }
    },

    previewImage(file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        this.currentDish.image = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  },
};
</script>

  <style scoped>
  /* 商品管理页面样式 */
  .product-management {
    padding: 20px;
    background-color: #ffffff;
    border-radius: 10px;
    border: 1px solid #ddd;
  }

  .add-dish-btn-container {
    text-align: right;
    margin-bottom: 20px;
  }

  .add-dish-btn {
    background-color: #4CAF50;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }

  .add-dish-btn:hover {
    background-color: #ff5733;
  }

  .dishes-gallery {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 20px;
  }

  .dish-card {
    position: relative;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    height: 200px; /* 设置固定高度 */
    overflow: hidden; /* 隐藏超出部分 */
    background-color: rgba(0, 0, 0, 0.3); /* 半透明背景 */
    text-shadow: 1px 1px 2px white, 0 0 25px white, 0 0 5px white;
  }

  .dish-card h3,
  .dish-card p {
    position: relative; /* 相对定位 */
    z-index: 2; /* 确保文字在图片上方 */
    color: #000000;
  }

  .dish-card h3 {
    font-size: 1.5em;
    color: #000000;
    margin-bottom: 10px;
  }

  .toggle-switch {
    position: relative;
    display: inline-block;
    width: 60px;
    height: 30px;
    z-index: auto;
  }

  .toggle-switch input {
    opacity: 0;
    width: 0;
    height: 0;
    z-index: auto;
  }
  .slider {
    position: absolute;
    cursor: pointer;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: #ccc;
    transition: .4s;
    z-index: 3;
  }

  .slider:before {
    position: absolute;
    content: "";
    height: 22px;
    width: 22px;
    left: 4px;
    bottom: 4px;
    background-color: white;
    transition: .4s;
    z-index: 3;
  }
  input:checked + .slider {
    background-color: #4CAF50;
  }

  input:focus + .slider {
    box-shadow: 0 0 1px #4CAF50;
  }

  input:checked + .slider:before {
    transform: translateX(30px);
  }

  .slider.round {
    border-radius: 34px;
  }

  .slider.round:before {
    border-radius: 50%;
  }

  .toggle-label {
    position: absolute;
    width: 100%;
    text-align: center;
    color: white;
    font-size: 12px;
    line-height: 30px;
    pointer-events: none;
    text-shadow: 0 0 2px rgba(0, 0, 0, 0.4);
  }
  .edit-btn,
  .delete-btn {
    position: absolute;
    top: 10px;
    right: 10px;
    padding: 5px 10px;
    background-color: #ff5733;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    z-index: 3; /* 确保按钮在图片上方 */
  }

  .delete-btn {
    top: 43px;
    background-color: #97260d;
  }

  .edit-btn:hover {
    background-color: #ff5733;
  }

  .delete-btn:hover {
    background-color: #c9302c;
  }

  /* 弹窗样式 */
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }

  .modal {
    background-color: #fff;
    padding: 30px;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    width: 400px;
    max-width: 90%; /* 确保在小屏幕上也能显示 */
    overflow: auto; /* 处理内容溢出 */
  }

  .modal h3 {
    margin-bottom: 20px;
  }

  .modal label {
    display: block;
    margin-bottom: 8px;
    font-weight: bold;
  }

  .modal input,
  .modal textarea,
  .modal select {
    width: 100%;
    padding: 10px;
    margin-bottom: 15px;
    border-radius: 5px;
    border: 1px solid #ddd;
  }

  .modal button {
    padding: 10px 20px;
    background-color: #ff5733;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    margin-right: 10px;
  }

  .modal button:hover {
    background-color: #ff5733;
  }

  .upload-area {
    border: 2px dashed #007bff;
    padding: 20px;
    text-align: center;
    cursor: pointer;
    border-radius: 5px;
    margin-bottom: 10px;
    position: relative; /* 使子元素绝对定位 */
    height: 200px; /* 设置固定高度 */
    overflow: hidden; /* 隐藏超出部分 */
  }

  .upload-area img {
    position: absolute; /* 绝对定位 */
    top: 0; /* 填充顶部 */
    left: 0; /* 填充左侧 */
    width: 100%; /* 使图片宽度占满容器 */
    height: 100%; /* 使图片高度占满容器 */
    object-fit: cover; /* 保持图片比例并裁剪 */
  }

  .dish-card img {
    position: absolute; /* 绝对定位 */
    top: 0; /* 填充顶部 */
    left: 0; /* 填充左侧 */
    width: 100%; /* 使图片宽度占满容器 */
    height: 100%; /* 使图片高度占满容器 */
    object-fit: cover; /* 保持图片比例并裁剪 */
    z-index: 1; /* 确保图片在底层 */
  }

  .upload-area button {
    position: relative; /* 相对定位 */
    z-index: 2; /* 确保按钮在图片上方 */
    margin-top: 100px; /* 添加一些间距 */
  }

  </style>
