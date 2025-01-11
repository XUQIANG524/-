<template>
  <div class="order-inquire">
    <!-- Tabs for order states -->
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick" style="color: black; font-size: 20px">
      <el-tab-pane label="全部订单" name="first">
        <!-- Orders list -->
        <div class="orders" v-for="order in orders" :key="order.id">
          <div class="order-item">
            <div class="order-header">
              <div class="storename">{{ order.storename }}</div>
              <div class="order-time">订单时间：{{ order.ordertime }}</div>
              <div class="order-right">
                <div :class="['order-status', order.status]">
                  {{ order.status }}
                </div>
              </div>
            </div>

            <!-- Order details -->
            <div class="order-details">
              <div class="order-detail-left">
                <div v-for="item in order.orderdetail" :key="item.name" class="order-detail-item">
                  <img :src="item.img" alt="item.name" class="order-item-img"
                    style="border: 1px solid #ccc; width: 120px; height: 80px" />
                  <span style="font-size: 16px">{{ item.name }}</span>
                </div>
              </div>
              <div class="totalprice" style="color: black; margin-top: 30px">
                ￥{{ order.totalprice }}
              </div>
            </div>
          </div>
        </div>
        <!-- Empty state -->
        <div v-if="orders.length === 0">暂无订单</div>
      </el-tab-pane>

      <el-tab-pane label="待评价" name="second">
        <div class="orders" v-for="order in orders.filter((order) => order.status === '已完成')" :key="order.id">
          <div class="order-item">
            <div class="order-header">
              <div class="storename">{{ order.storename }}</div>
              <div class="order-time">订单时间：{{ order.ordertime }}</div>
              <div class="order-right">
                <div :class="['order-status', order.status]">
                  {{ order.status }}
                </div>
              </div>
            </div>

            <!-- Order details -->
            <div class="order-details">
              <div class="order-detail-left">
                <div v-for="item in order.orderdetail" :key="item.name" class="order-detail-item">
                  <img :src="item.img" alt="item.name" class="order-item-img"
                    style="border: 1px solid #ccc; width: 120px; height: 80px" />
                  <span>{{ item.name }}</span>
                </div>
              </div>
              <div class="totalprice" style="color: black; margin-top: 30px">
                ￥{{ order.totalprice }}
              </div>
            </div>
          </div>
        </div>
        <div v-if="
          orders.filter((order) => order.status === '已完成').length === 0
        ">
          暂无待评价订单
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import type { TabsPaneContext } from "element-plus";
import { onMounted } from "vue";
import axios from "axios";

const activeName = ref("first");

const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event);
};

// Sample data for orders
const orders = ref([
  {
    id: "1",
    storename: "餐厅A",
    ordertime: "2025-01-08 12:30",
    totalprice: "120.50",
    status: "已接单",
    orderdetail: [
      { name: "炸鸡", img: "/path/to/chicken.jpg" },
      { name: "可乐", img: "/path/to/coke.jpg" },
    ],
  },
  {
    id: "2",
    storename: "餐厅B",
    ordertime: "2025-01-07 19:00",
    totalprice: "85.00",
    status: "配送中",
    orderdetail: [
      { name: "披萨", img: "/path/to/pizza.jpg" },
      { name: "啤酒", img: "/path/to/beer.jpg" },
    ],
  },
  {
    id: "3",
    storename: "餐厅C",
    ordertime: "2025-01-06 10:00",
    totalprice: "99.00",
    status: "已完成",
    orderdetail: [
      { name: "牛排", img: "/path/to/steak.jpg" },
      { name: "红酒", img: "/path/to/wine.jpg" },
    ],
  },
  {
    id: "4",
    storename: "餐厅D",
    ordertime: "2025-01-05 14:30",
    totalprice: "55.00",
    status: "待评价",
    orderdetail: [
      { name: "沙拉", img: "/path/to/salad.jpg" },
      { name: "果汁", img: "/path/to/juice.jpg" },
    ],
  },
]);
onMounted(() => {
  const token = sessionStorage.getItem('token');
  axios
    .post("http://localhost:8088/api/orders/findAllByUserId", {}, {
      headers: {
        tjpu22s10: token,
      },
    })
    .then((response) => {
      console.log("response:", response);
      orders.value.splice(
        0,
        orders.value.length,
        ...response.data.data.map((item) => ({
          id: item.id,
          storename: item.storename,
          ordertime: item.ordertime,
          totalprice: item.price,
          status: item.status,
          orderdetail: item.orderdetail.map((detail) => ({
            name: detail.name, // 假设 detail 中包含 name 字段
            img: `http://106.3.99.64:20028/tjpu22s06/${detail.image}`,
          })),
        }))
      );
    })
    .catch((error) => { });
});
</script>

<style scoped>
.demo-tabs>.el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}

.demo-tabs {
  font-size: 22px !important;
  /* 调整字体大小 */
}

/* 使用 ::v-deep 来确保深层样式应用 */
::v-deep .demo-tabs .el-tabs__header .el-tab-pane.is-active {
  color: #ff5733 !important;
  /* 修改选中的标签文字颜色 */
}

::v-deep .el-tabs__header .el-tab {
  font-size: 22px !important;
  /* 修改字体大小 */
}

::v-deep .el-tabs__nav-scroll {
  color: #8d1b1b !important;
}

.order-inquire {
  background-color: #f5f5f5;
  width: 100%;
  height: 100%;
}

.orders {
  margin-bottom: 20px;
}

.order-item {
  border: #6b778c 1px solid;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.storename {
  font-size: 18px;
  font-weight: bold;
}

.order-status {
  font-size: 14px;
  padding: 2px 10px;
  border-radius: 4px;
  color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
}

.order-status.已接单 {
  background-color: #f39c12;
}

.order-status.配送中 {
  background-color: #3498db;
}

.order-status.已完成 {
  background-color: #2ecc71;
}

.order-status.待评价 {
  background-color: #e74c3c;
}

.order-time {
  color: #757373;
}

.totalprice {
  color: #2667ff;
}

.order-details {
  margin-top: 10px;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  /* Align to the left */
}

.order-detail-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 10px;
}

.order-detail-left {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  /* Align to the left */
}

.order-item-img {
  width: 50px;
  height: 50px;
  margin-right: 10px;
  border-radius: 5px;
}
</style>
