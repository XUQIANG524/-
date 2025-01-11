<template>
  <div class="order-details">
    <!-- 订单列表 -->
    <div class="order-item" v-for="order in orders" :key="order.id">
      <div class="order-info">
        <h4>订单号：{{ order.id }}</h4>
        <p><strong>送达地点：</strong>{{ order.destination }}</p>
        <p><strong>支付金额：</strong>￥{{ order.payment }}</p>
        <p><strong>状态：</strong>{{ order.status }}</p>
      </div>
      <button class="view-details-btn" @click="viewDetails(order)">
        查看详情
      </button>
    </div>

    <!-- 订单详情模态框，只显示地图 -->
    <div v-if="isModalVisible" class="modal-overlay">
      <div class="modal">
        <!-- 地图容器 -->
        <div id="map" style="width: 100%; height: 400px"></div>
        <button @click="closeModal" style="margin: 10px">关闭</button>
        <button @click="orderOver" style="margin: 10px">订单送达</button>
      </div>
    </div>
  </div>
</template>

<script>
import L from "leaflet"; // 引入 Leaflet
import "leaflet/dist/leaflet.css"; // 引入 Leaflet CSS
import "leaflet-routing-machine"; // 引入 Leaflet Routing Machine
import axios from "axios";

export default {
  name: "OrderDetails",
  data() {
    return {
      orders: [
        {
          id: 1,
          destination: "第一大道",
          payment: 20,
          status: "待派送",
          deliveryMethod: "骑手配送",
          orderTime: "2024-12-28 14:30",
          latitude: 39.055822041417464, // 纬度
          longitude: 117.11306366089744, // 经度
        },
        {
          id: 2,
          destination: "第二大道",
          payment: 30,
          status: "已派送",
          deliveryMethod: "骑手配送",
          orderTime: "2024-12-28 15:00",
          latitude: 39.9142, // 示例经度
          longitude: 116.4174, // 示例纬度
        },
      ],
      isModalVisible: false,
      selectedOrder: null,
      userLocation: null, // 用户当前位置
    };
  },
  methods: {
    orderOver() {
      // 订单送达
      this.selectedOrder.status = "已完成";
      this.closeModal();
      async;
    },
    // 查看订单详情
    viewDetails(order) {
      this.selectedOrder = order;
      this.isModalVisible = true;
      this.$nextTick(() => {
        // 在模态框中显示地图
        this.getUserLocation(); // 获取用户当前位置
      });
    },
    // 关闭模态框
    closeModal() {
      this.isModalVisible = false;
      this.selectedOrder = null;
    },
    // 获取用户当前位置
    getUserLocation() {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          (position) => {
            // 获取用户的经纬度
            this.userLocation = {
              lat: 39.068405,
              lng: 117.102135,
            };
            this.initMap(); // 获取位置成功后初始化地图
            console.log("用户当前位置:", this.userLocation);
          },
          (error) => {
            console.error("定位失败:", error);
            alert("无法获取当前位置，请确保您的定位服务已开启。");
          },
          {
            enableHighAccuracy: true, // 尝试使用更高精度的定位方式
            timeout: 5000, // 定位超时时间（5秒）
            maximumAge: 0, // 不缓存位置
          }
        );
      } else {
        alert("浏览器不支持定位功能！");
      }
    },

    // 初始化地图
    initMap() {
      const map = L.map("map").setView(
        [this.userLocation.lat, this.userLocation.lng],
        12
      );

      // 使用 OpenStreetMap 作为地图瓦片源
      L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png").addTo(
        map
      );

      // 创建用户位置的标记
      L.marker([this.userLocation.lat, this.userLocation.lng])
        .addTo(map)
        .bindPopup("您的当前位置")
        .openPopup();

      // 创建订单目的地的标记
      L.marker([this.selectedOrder.latitude, this.selectedOrder.longitude])
        .addTo(map)
        .bindPopup(this.selectedOrder.destination)
        .openPopup();

      // 创建导航路线
      L.Routing.control({
        waypoints: [
          L.latLng(this.userLocation.lat, this.userLocation.lng), // 用户当前位置
          L.latLng(this.selectedOrder.latitude, this.selectedOrder.longitude), // 订单目的地
        ],
        routeWhileDragging: true, // 拖拽时显示路线
      }).addTo(map);
    },
  },
  mounted() {
    const token = sessionStorage.getItem("token");
    axios
      .post(
        "http://localhost:8088/api/orders/findAllByWorkerId",
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
            destination: item.address,
            orderTime: item.orderTime,
            payment: item.price,
            deliveryMethod: "骑手配送",
            status: item.orderState,
            latitude: 39.055822041417464, // 纬度
            longitude: 117.11306366089744, // 经度
          }))
        );
      })
      .catch((error) => {});
  },
};
</script>

<style scoped>
/* 订单详情样式 */
.order-details {
  padding: 20px;
  font-family: Arial, sans-serif;
  background-color: #f9f9f9;
  border-radius: 20px;
}

.order-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #ffffff;
  padding: 15px;
  margin-bottom: 15px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.view-details-btn {
  background-color: #ff5733;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.view-details-btn:hover {
  background-color: #e04a2e;
}

/* 订单详情模态框 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background-color: #ffffff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 80%;
}

button {
  background-color: #ff5733;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  width: 20%;
}

button:hover {
  background-color: #e04a2e;
}
</style>
