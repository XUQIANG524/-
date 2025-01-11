<template>
  <div class="order-item" v-for="order in orders" :key="order.id">
    <div class="order-container">
      <!-- 订单信息展示 -->
      <el-card class="order-card">
        <div class="card-header"></div>

        <div class="order-details">
          <el-row
            :gutter="20"
            class="order-header"
            type="flex"
            justify="space-between"
          >
            <!-- 时间 -->
            <el-col :span="12">
              <div class="order-item">
                <span class="label">时间：</span>
                <span>{{ order.time }}</span>
              </div>
            </el-col>
          </el-row>

          <!-- 单价浮动在右上方 -->
          <div class="order-item price-item">
            <span class="price">￥{{ order.price }}</span>
          </div>

          <el-row :gutter="20" class="order-address" style="font-size: 22px">
            <!-- 取餐地址 -->
            <el-col :span="12">
              <div class="order-item" style="font-size">
                <span class="label">取餐地址：</span>
                <span>{{ order.pickupAddress }}</span>
              </div>
            </el-col>

            <!-- 送餐地址 -->
            <el-col :span="12">
              <div class="order-item">
                <span class="label">送餐地址：</span>
                <span>{{ order.deliveryAddress }}</span>
              </div>
            </el-col>
          </el-row>
        </div>

        <div class="action-buttons">
          <el-button
            type="primary"
            @click="grabOrder(order)"
            class="grab-order-btn"
            >接受订单</el-button
          >
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      orders: [],
    };
  },
  methods: {
    grabOrder(order) {
      this.$message.success("订单已接受！");
      const token = sessionStorage.getItem("token");
      const form = new FormData();
      form.append("id", order.id);
      console.log(order.id);
      axios
        .post("http://localhost:8088/api/orders/updateState1", form, {
          headers: {
            tjpu22s10: token,
          },
        })
        .then(() => {
          // 订单接受成功后，删除当前订单
          const index = this.orders.findIndex((o) => o.id === order.id);
          if (index !== -1) {
            this.orders.splice(index, 1); // 从数组中删除该订单
          }
        });
    },
  },
  mounted() {
    const token = sessionStorage.getItem("token");
    axios
      .post(
        "http://localhost:8088/api/orders/findAllByState0",
        {},
        {
          headers: {
            tjpu22s10: token,
          },
        }
      )
      .then((response) => {
        console.log("response:", response);
        this.orders.splice(
          0,
          this.orders.length,
          ...response.data.data.map((item) => ({
            id: item.id,
            deliveryAddress: item.deliveryAddress,
            time: item.time,
            price: item.price,
            pickupAddress: item.pickupAddress,
          }))
        );
      })
      .catch((error) => {});
  },
};
</script>

<style scoped>
.order-card {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  width: 97%;
  position: relative;
  /* 使其成为定位的参考对象 */
  height: 200px;
  margin-bottom: 20px;
}

.card-header {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
}

.order-details .order-item {
  margin-bottom: 10px;
}

.order-item .label {
  font-weight: bold;
  color: #333;
}

.action-buttons {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.el-button {
  width: 86%;
  background-color: #ff5733;
  border: #ff5733;
}
.el-button:hover {
  background-color: #e93d08;
  border: #e93d08;
}

.order-address {
  display: flex;
  flex-direction: column;
  /* 垂直排列子元素 */
}

.order-header {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

/* 单价浮动在右上方 */
.price-item {
  position: absolute;
  /* 绝对定位 */
  top: 20px;
  /* 距离顶部 20px */
  right: 20px;
  /* 距离右侧 20px */
  font-size: 40px;
  font-weight: bold;
  color: #ff0000;
}
.grab-order-btn {
  width: 90%;
}
</style>
