<template>
  <div class="product-management">
    <el-page-header :icon="ArrowLeft" @click="$router.go(-1)">
      <template #content>
        <span class="text-large font-600 mr-3">店铺</span>
      </template>
    </el-page-header>
    <!-- 店铺简介 -->
    <div class="store-intro">
      <h1>{{ MerchantName }}</h1>
      <p>{{ MerchantDescription }}</p>
    </div>

    <!-- 菜品展示区 -->
    <div class="dishes-gallery">
      <div
        v-for="dish in dishes"
        :key="dish.id"
        class="dish-card"
        :style="getDishBackground(dish)"
      >
        <h3>{{ dish.name }}</h3>
        <p>{{ dish.description }}</p>
        <p style="color: #ff6a00; font-size: 28px; margin-top: 20px">
          ￥{{ dish.price }}
        </p>
        <button @click="addToCart(dish, $event)">+</button>
      </div>
    </div>

    <!-- 购物车按钮 -->
    <div
      class="cart-button"
      @click="toggleCart"
      @mousedown="startDrag"
      @mouseup="stopDrag"
      @mousemove="onDrag"
      ref="cartButton"
      :style="{
        left: cartButtonPosition.left + 'px',
        top: cartButtonPosition.top + 'px',
      }"
    >
      <span>🛒</span>
      <div class="cart-count" v-if="cartCount > 0">{{ cartCount }}</div>
    </div>

    <!-- 购物车面板 -->
    <div
      v-if="isCartOpen"
      class="cart-panel"
      @mousedown="startDrag"
      @mouseup="stopDrag"
      @mousemove="onDrag"
      ref="cartPanel"
      :style="{
        left: cartPanelPosition.left + 'px',
        top: cartPanelPosition.top + 'px',
      }"
    >
      <div class="cart-header">
        <h3>购物车</h3>
        <button
          @click="clearCart"
          style="background-color: white; border: none; cursor: pointer"
        >
          <span>🗑️清空</span>
        </button>
      </div>
      <ul>
        <li v-for="item in cart" :key="item.id">
          {{ item.name }} - ￥{{ item.price * item.quantity }}
          <el-input-number
            v-model="item.quantity"
            :min="0"
            :max="10"
            @change="handleChange(item, $event)"
            style="width: 96px; margin-left: 20px"
          />
        </li>
      </ul>

      <div class="cart-total">
        <strong>总计：</strong> ￥{{ totalPrice.toFixed(2) }}
      </div>
      <button class="checkout-button" @click="nextPay">结算</button>
    </div>
  </div>
  <router-view></router-view>
</template>

<script>
import { ref, computed, nextTick } from "vue";
import axios from "axios";
import qs from "qs";
import { useToast } from "vue-toastification"; // 导入 useToast

export default {
  name: "ProductManagement",
  data() {
    return {
      dishes: [],
      cart: [],
      isCartOpen: false,
      dragging: false,
      dragOffsetX: 0,
      dragOffsetY: 0,
      cartButtonPosition: { left: 20, top: 160 }, // 购物车按钮位置
      cartPanelPosition: { left: 20, top: 170 }, // 购物车面板位置

      // 接收路由参数
      MerchantName: "",
      MerchantDescription: "",
      MerchantId: "",
    };
  },
  computed: {
    totalPrice() {
      return this.cart.reduce(
        (total, item) => total + item.price * item.quantity,
        0
      );
    },
    cartCount() {
      return this.cart.reduce((count, item) => count + item.quantity, 0);
    },
  },
  methods: {
    nextPay() {
      const toast = useToast(); // 获取 toast 实例
      if (this.totalPrice === 0) {
        toast.error("请选择商品"); // 显示错误提示
        return;
      }
      this.$router.push("/furonglou/order");
    },
    async shopcartAdd(dish) {
      const token = sessionStorage.getItem("token");
      const response = await axios.post(
        "http://localhost:8088/logic/user/addDish",
        qs.stringify({
          dish_id: dish.id,
        }),
        {
          headers: {
            tjpu22s10: token,
          },
        }
      );
    },
    async shopcartSub(dish) {
      const token = sessionStorage.getItem("token");
      const response = await axios.post(
        "http://localhost:8088/logic/user/deleteDish",
        qs.stringify({
          dish_id: dish.id,
        }),
        {
          headers: {
            tjpu22s10: token,
          },
        }
      );
    },
    getDishBackground(dish) {
      return {
        backgroundImage: `url(${dish.imageUrl})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        padding: "20px",
        borderRadius: "8px",
        color: "white",
      };
    },
    addToCart(dish) {
      this.shopcartAdd(dish);
      const existingDish = this.cart.find((item) => item.id === dish.id);
      if (existingDish) {
        existingDish.quantity += 1;
        existingDish.oldQuantity += 1;
      } else {
        this.cart.push({ ...dish, quantity: 1, oldQuantity: 1 });
      }
    },
    toggleCart() {
      this.isCartOpen = !this.isCartOpen;
      nextTick(() => {
        if (this.isCartOpen) {
          const buttonRect = this.$refs.cartButton?.getBoundingClientRect();
          if (buttonRect) {
            this.cartButtonPosition = {
              left: buttonRect.left,
              top: buttonRect.top,
            };
            this.cartPanelPosition = {
              left: buttonRect.left,
              top: buttonRect.top + 50,
            }; // 设置购物车面板相对位置
          }
        }
      });
    },
    startDrag(event) {
      if (this.isCartOpen) {
        nextTick(() => {
          const cartButton = this.$refs.cartButton;
          if (cartButton) {
            this.dragging = true;
            const buttonRect = cartButton.getBoundingClientRect();
            this.dragOffsetX = event.clientX - buttonRect.left;
            this.dragOffsetY = event.clientY - buttonRect.top;
            document.body.style.userSelect = "none"; // 禁止文本选择
            document.addEventListener("mousemove", this.onDrag); // 监听鼠标移动事件
            document.addEventListener("mouseup", this.stopDrag); // 监听鼠标松开事件
          }
        });
      }
    },
    onDrag(event) {
      if (this.dragging) {
        // 更新购物车按钮位置
        this.cartButtonPosition = {
          left: event.clientX - this.dragOffsetX,
          top: event.clientY - this.dragOffsetY,
        };

        // 更新购物车面板位置
        this.cartPanelPosition = {
          left: event.clientX - this.dragOffsetX,
          top: event.clientY - this.dragOffsetY + 50, // 面板偏移
        };
      }
    },
    stopDrag() {
      if (this.dragging) {
        document.removeEventListener("mousemove", this.onDrag);
        document.removeEventListener("mouseup", this.stopDrag);
        this.dragging = false;
        document.body.style.userSelect = ""; // 恢复文本选择
      }
    },
    handleChange(item, newQuantity) {
      const oldQuantity = item.oldQuantity;
      console.log(newQuantity);
      console.log(oldQuantity);
      if (newQuantity > oldQuantity) {
        this.shopcartAdd(item);
      } else if (newQuantity < oldQuantity) {
        this.shopcartSub(item);
      }
      item.oldQuantity = newQuantity;
      // 更新数量
      if (item.quantity === 0) {
        this.cart = this.cart.filter((cartItem) => cartItem.id !== item.id);
      }
    },
    async clearCart() {
      this.cart = [];
      this.cartCount = 0;
      const token = sessionStorage.getItem("token");
      const response = await axios.post(
        "http://localhost:8088/logic/user/deleteCart",
        qs.stringify({}),
        {
          headers: {
            tjpu22s10: token,
          },
        }
      );
    },
  },
  created() {
    // 通过解构赋值从 this.$route.query 中获取路由参数
    const { MerchantName, MerchantDescription, MerchantId } = this.$route.query;

    // 将获取到的参数赋值给组件中的 data 属性
    this.MerchantName = MerchantName || ""; // 如果 MerchantName 为 undefined，则使用空字符串
    this.MerchantDescription = MerchantDescription || ""; // 同理
    this.MerchantId = MerchantId || ""; // 同理

    // 输出到控制台查看参数是否正确
    console.log("MerchantName", this.MerchantName);
    console.log("MerchantDescription", this.MerchantDescription);
    console.log("MerchantId", this.MerchantId);
  },
  mounted() {
    const token = sessionStorage.getItem("token");
    console.log("token:", token);
    console.log("MerchantId:", this.MerchantId);
    const form = new FormData();
    form.append("merchant_id", this.MerchantId);
    axios
      .post("http://localhost:8088/logic/user/getDishes", form, {
        headers: {
          tjpu22s10: token,
        },
      })
      .then((response) => {
        console.log("response:", response);
        this.dishes.splice(
          0,
          this.dishes.length,
          ...response.data.data.map((item) => ({
            id: item.dishId,
            name: item.dishName,
            price: item.price,
            description: item.description,
            imageUrl: "http://106.3.99.64:20028/tjpu22s06/" + item.image,
          }))
        );
      })
      .catch((error) => {});
    console.log("token:", this.dishes);
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
  height: 100%;
  overflow: hidden;
}

.back-button {
  position: absolute;
  top: 20px;
  left: 20px;
  color: #333;
  font-size: 28px;
  cursor: pointer;
  background: #ff5733;
  width: 50px;
}

/* 店铺简介样式 */
.store-intro {
  text-align: center;
  margin-top: 40px;
  margin-bottom: 80px;
}

.store-intro h1 {
  font-size: 2em;
  color: #333;
  margin-bottom: 10px;
}

.store-intro p {
  font-size: 1.2em;
  color: #777;
}

/* 菜品展示区 */
.dishes-gallery {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

/* 每个菜品卡片 */
.dish-card {
  position: relative;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  height: 200px;
  background-color: rgba(0, 0, 0, 0.3);
  /* 半透明背景 */
  text-shadow: 1px 1px 2px white, 0 0 25px white, 0 0 5px white;
  overflow: hidden;
}

.dish-card h3 {
  font-size: 1.5em;
  color: rgb(0, 0, 0);
  margin-bottom: 10px;
}

.dish-card p {
  font-size: 1em;
  color: rgb(0, 0, 0);
}

.dish-card button {
  background-color: #ff5733;
  color: white;
  border: none;
  padding: 5px 10px;
  font-size: 20px;
  border-radius: 5px;
  cursor: pointer;
  position: absolute;
  /* 使用绝对定位 */
  top: 20px;
  /* 距离卡片顶部 10px */
  right: 20px;
  /* 距离卡片右侧 10px */
}

.dish-card button:hover {
  background-color: #e04a2e;
}

/* 购物车按钮 */
/* 购物车按钮 */
.cart-button {
  position: absolute;
  cursor: pointer;
  left: 270px;
  top: 120px;
  background-color: #ff5733;
  color: white;
  padding: 15px;
  border-radius: 50%;
  font-size: 24px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  z-index: 1000;
  /* 确保按钮在其他元素上方 */
  width: 30px;
  /* 可以根据需要调整 */
  height: 30px;
  /* 可以根据需要调整 */
}

.cart-button:hover {
  background-color: #e04a2e;
}

/* 购物车面板 */
.cart-panel {
  position: fixed;
  bottom: 80px;
  right: 20px;
  background-color: white;
  width: 300px;
  height: 400px;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  padding: 20px;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  z-index: 2000;
}

.cart-panel h3 {
  font-size: 1.8em;
  color: #333;
  margin-bottom: 20px;
}

.cart-panel ul {
  flex-grow: 1;
  list-style: none;
  padding: 0;
}

.cart-panel li {
  font-size: 1.2em;
  margin-bottom: 10px;
}

.cart-panel .cart-total {
  font-size: 1.5em;
  margin-top: 10px;
}

.cart-panel .checkout-button {
  background-color: #ff5733;
  color: white;
  padding: 10px;
  border-radius: 5px;
  cursor: pointer;
  border: none;
}

.cart-panel .checkout-button:hover {
  background-color: #e04a2e;
}

.cart-header {
  display: flex;
  justify-content: space-between;
}

/* 响应式调整：适应不同屏幕尺寸 */
@media (max-width: 768px) {
  .dishes-gallery {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .dishes-gallery {
    grid-template-columns: 1fr;
  }
}

.cart-count {
  position: absolute;
  top: -5px;
  right: -1px;
  background: red;
  color: white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  text-align: center;
  font-size: 12px;
}

.dot {
  position: absolute;
  background-color: red;
  border-radius: 50%;
  width: 10px;
  height: 10px;
  z-index: 1000;
  /* 确保红点显示在其他元素之上 */
}
</style>