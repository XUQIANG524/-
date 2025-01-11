<template>
  <div class="order-management">
    <!-- 订单列表 -->
    <div class="order-list">
      <div v-for="order in orders" :key="order.id" class="order-card">
        <div class="order-header">
          <span class="order-id">订单号: {{ order.id }}</span>
          <span class="order-status" :class="order.statusClass">{{
            order.status
          }}</span>
        </div>
        <div class="order-info">
          <p><strong>客户名称:</strong> {{ order.customerName }}</p>
          <p><strong>订单时间:</strong> {{ order.orderTime }}</p>
          <p><strong>用户地址:</strong> {{ order.address }}</p>
          <p><strong>备注:</strong> {{ order.date }}</p>
          <p><strong>订单金额:</strong> ¥{{ order.totalPrice }}</p>
          <div class="order-details">
            <div class="order-detail">
              <div v-for="order in order.orderdetail" :key="order.dishName" class="order-detail-item" style="
                  display: flex;
                  justify-content: space-between;
                  align-items: center;
                ">
                <img :src="order.img" alt="item.name" class="order-item-img"
                  style="border: 1px solid #ccc; width: 120px; height: 80px" />
                <div>
                  <span style="font-size: 20px">{{ order.dishName }}</span>×{{ order.amount }}
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="order-actions">
          <button v-if="order.status === '待接单'" @click="processOrder(order.id, '接单')" class="action-btn accept-btn">
            接单
          </button>
          <button v-if="order.status === '待接单'" @click="processOrder(order.id, '取消订单')" class="action-btn cancel-btn">
            取消订单
          </button>
          <button v-if="order.status === '待配送'" @click="processOrder(order.id, '完成订单')" class="action-btn complete-btn">
            完成订单
          </button>
        </div>
      </div>
    </div>

    <!-- 编辑订单弹窗 -->
    <div v-if="isEditing" class="modal">
      <div class="modal-content">
        <h3>编辑订单</h3>
        <div class="modal-item">
          <label for="orderId">订单号:</label>
          <input type="text" v-model="currentOrder.id" id="orderId" disabled />
        </div>
        <div class="modal-item">
          <label for="status">订单状态:</label>
          <select v-model="currentOrder.status" id="status">
            <option value="待接单">待接单</option>
            <option value="待配送">待配送</option>
            <option value="已完成">已完成</option>
          </select>
        </div>
        <div class="modal-actions">
          <button @click="saveOrder">保存</button>
          <button @click="cancelEdit">取消</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      isEditing: false, // 控制编辑模式
      currentOrder: null, // 当前编辑的订单
      orders: [
        {
          id: "1001",
          customerName: "张三",
          orderTime: "2021-08-01 10:00:00",
          address: "北京市海淀区西二旗",
          date: "无",
          totalPrice: 100.0,
          status: "待接单",
          statusClass: "pending",
          orderdetail: [
            {
              dishName: "汉堡",
              img: "https://img.t.sinajs.cn/t6/style/images/global_nav/WB_logo.png",
              amount: 2,
            },
          ],
        },
        {
          id: "1002",
          customerName: "李四",
          amount: 30.0,
          status: "待配送",
          statusClass: "in-progress",
        },
        {
          id: "1003",
          customerName: "王五",
          amount: 80.0,
          status: "已完成",
          statusClass: "completed",
        },
      ],
    };
  },
  methods: {
    // 处理订单操作（接单、完成、取消）
    processOrder(orderId, action) {
      const order = this.orders.find((order) => order.id === orderId);
      if (action === "接单") {
        order.status = "待配送";
        order.statusClass = "in-progress";
      } else if (action === "取消订单") {
        order.status = "已取消";
        order.statusClass = "cancelled";
      } else if (action === "完成订单") {
        order.status = "已完成";
        order.statusClass = "completed";
      }
    },
    // 启动编辑订单模式
    editOrder(orderId) {
      this.currentOrder = {
        ...this.orders.find((order) => order.id === orderId),
      };
      this.isEditing = true;
    },
    // 保存订单修改
    saveOrder() {
      const orderIndex = this.orders.findIndex(
        (order) => order.id === this.currentOrder.id
      );
      if (orderIndex !== -1) {
        this.orders.splice(orderIndex, 1, this.currentOrder);
      }
      this.isEditing = false;
    },
    // 取消编辑
    cancelEdit() {
      this.isEditing = false;
    },
  },
  // id: "1001",
  //         customerName: "张三",
  //         orderTime: "2021-08-01 10:00:00",
  //         address: "北京市海淀区西二旗",
  //         date: "无",
  //         totalPrice: 100.0,
  //         status: "待接单",
  //         statusClass: "pending",
  //         orderdetail: [
  //           {
  //             dishName: "汉堡",
  //             img: "https://img.t.sinajs.cn/t6/style/images/global_nav/WB_logo.png",
  //             amount: 2,
  //           },
  //         ],
  mounted(){
  const token = sessionStorage.getItem('token');
  axios
    .post("http://localhost:8088/api/orders/findAllByMerchantId", {}, {
      headers: {
        tjpu22s10: token,
      },
    })
    .then((response) => {
      console.log("response:", response);
      this.orders.splice(
        0,
        this.orders.length,
        ...response.data.data.map((item) => ({
          id: item.id,
          customerName: item.customerName,
          orderTime: item.ordertime,
          address: item.address,
          date:item.date,
          totalPrice: item.price,
          status: item.status,
          orderdetail: item.orderdetail.map((detail) => ({
            dishName: detail.name, // 假设 detail 中包含 name 字段
            img: `http://106.3.99.64:20028/tjpu22s06/${detail.image}`,
            amount:detail.amount
          })),
        }))
      );
    })
    .catch((error) => { });
  },
}
</script>

<style scoped>
h2 {
  font-size: 2.4em;
  color: #333;
  text-align: center;
  margin-bottom: 20px;
}

.order-list {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 20px;
  margin-top: 20px;
}

.order-card {
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.order-card:hover {
  transform: translateY(-5px);
}

.order-header {
  display: flex;
  justify-content: space-between;
  font-size: 1.2em;
  margin-bottom: 15px;
}

.order-status {
  font-weight: bold;
  padding: 5px 10px;
  border-radius: 20px;
  color: white;
}

.pending {
  background-color: #ff9800;
}

.in-progress {
  background-color: #2196f3;
}

.completed {
  background-color: #4caf50;
}

.cancelled {
  background-color: #f44336;
}

.order-info p {
  font-size: 1.1em;
  color: #555;
  margin-bottom: 8px;
}

.order-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
}

.action-btn {
  padding: 10px 20px;
  font-size: 1.1em;
  background-color: #ff5733;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  flex: 1;
  margin-right: 10px;
}

.action-btn:hover {
  background-color: #e64a24;
}

.accept-btn {
  background-color: #4caf50;
}

.accept-btn:hover {
  background-color: #45a049;
}

.cancel-btn {
  background-color: #f44336;
}

.cancel-btn:hover {
  background-color: #d32f2f;
}

.complete-btn {
  background-color: #2196f3;
}

.complete-btn:hover {
  background-color: #1976d2;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  width: 400px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

h3 {
  font-size: 1.8em;
  margin-bottom: 20px;
}

.modal-item {
  margin-bottom: 15px;
}

.modal-item label {
  font-size: 1.1em;
  margin-bottom: 5px;
}

.modal-item input,
.modal-item select {
  width: 100%;
  padding: 8px;
  font-size: 1.1em;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.modal-actions {
  display: flex;
  justify-content: space-between;
}

.modal-actions button {
  padding: 8px 16px;
  font-size: 1.2em;
  background-color: #ff5733;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.modal-actions button:hover {
  background-color: #e64a24;
}

.modal-actions button:nth-child(2) {
  background-color: #f44336;
}

.modal-actions button:nth-child(2):hover {
  background-color: #d32f2f;
}
</style>
