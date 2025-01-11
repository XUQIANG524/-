<template>
  <div class="shadowBox">
    <button
      class="pay-order-back"
      style="
        position: absolute;
        top: 0px;
        right: 450px;
        cursor: pointer;
        font-size: 40px;
        border-radius: 50%;
        border: none;
      "
      @click="goBack"
    >
      ×
    </button>
    <div class="pay-order">
      <div class="pay-order-header">
        <h2>{{ address }}</h2>
        <p>{{ username }} {{ phone }}</p>
        <div
          class="pay-time"
          :class="{ selected: selectedButton === 1 }"
          style="
            padding: 10px;
            border-radius: 2px;
            margin-top: 5px;
            cursor: pointer;
            border-radius: 20px;
            border: 1px solid gray;
          "
          @click="selectButton(1)"
        >
          <p>立即配送 预计10:55送达</p>
        </div>
        <div
          class="pay-time"
          :class="{ selected: selectedButton === 2 }"
          style="
            padding: 10px;
            border-radius: 5px;
            margin-top: 5px;
            cursor: pointer;
            border-radius: 20px;
            border: 1px solid gray;
          "
          @click="selectButton(2)"
        >
          <p>
            预约配送
            <el-input
              v-model="time"
              style="width: 200px"
              placeholder="请输入预约时间"
            />
          </p>
        </div>
        <p style="color: yellowgreen; font-size: 15px; margin-top: 0px">
          🔈点餐请适量，环保又健康，祝您用餐愉快！
        </p>
      </div>
      <div class="pay-order-body">
        <div v-for="(item, index) in items" :key="index" class="pay-order-item">
          <img :src="item.image" alt="没有图片" />
          <div class="pay-order-item-info">
            <h1
              style="
                width: 150px;
                font-size: 20px;
                margin: 10px;
                align-items: left; /* 居中对齐 */
              "
            >
              {{ item.name }}
            </h1>
            <div
              class="pay-order-item-count"
              style="margin: 10px; align-items: left; /* 居中对齐 */"
            >
              x{{ item.count }}
            </div>
          </div>
          <div class="pay-order-item-price">
            <span>¥{{ item.price * item.count }}</span>
          </div>
        </div>
        <div class="pay-order-price" style="margin-top: 20px">
          <p
            style="
              margin-top: 20px;
              display: flex;
              justify-content: space-between;
              align-items: center;
            "
          >
            打包费 <span style="margin-left: "> ¥1</span>
          </p>
          <p
            style="
              margin-top: 20px;
              display: flex;
              justify-content: space-between;
              align-items: center;
            "
          >
            配送费 <span style="margin-left: ">¥6</span>
          </p>
          <p
            style="
              margin-top: 20px;
              display: flex;
              justify-content: space-between;
              align-items: center;
            "
          >
            商家代金券 <span>-¥20</span>
          </p>

          <el-divider />
          <h1 style="margin-bottom: 25px">
            总计
            <span style="margin-left: 380px">¥{{ totalPrice + 7 - 20 }}</span>
          </h1>
        </div>
      </div>
      <div
        class="pay-order-footer"
        style="
          box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
          border-radius: 20px;
          padding: 10px;
          margin-top: 5px;
          width: 93%;
          align-items: flex-start;
          height: 4%;
        "
      >
        备注<el-input
          v-model="date"
          style="width: 290px; margin-left: 20px"
          placeholder="请填写您的要求"
        />
      </div>
      <div
        class="payfor"
        style="
          width: 95%;
          height: 9%;
          box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
          border-radius: 20px;
          margin-top: 5px;
          display: flex;
        "
      >
        <div
          class="payfor-text"
          style="
            width: 70%;
            background-color: #303133;
            border-radius: 20px 0 0 20px;
            color: white;
          "
        >
          <p style="margin-left: 20px">
            合计<span style="margin-left: 10px; font-size: 28px"
              >¥{{ totalPrice + 7 - 20 }}</span
            >
          </p>
          <p style="margin-left: 20px">已优惠¥20</p>
        </div>
        <button
          class="payfor-btn"
          style="
            width: 30%;
            background-color: yellow;
            border-radius: 0 20px 20px 0;
            cursor: pointer;
            height: 66px;
          "
          @click="submitPayorder"
        >
          <p style="font-size: 18px">提交订单并支付</p>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent, ref, reactive, onUnmounted } from "vue";
import { ArrowLeft } from "@element-plus/icons-vue";
import axios from "axios"; // 引入 axios
import { mount } from "@vue/test-utils";
import { onMounted } from "vue";
import { computed } from "vue";
import { useToast } from "vue-toastification";
import { useRouter } from "vue-router";

export default defineComponent({
  name: "PayOrder",
  components: {
    ArrowLeft,
  },
  setup() {
    // 从 sessionStorage 中获取地址、用户名和电话
    const address = sessionStorage.getItem("address");
    const username = sessionStorage.getItem("username");
    const phone = sessionStorage.getItem("phone");
    const date = ref("");
    const time = ref("");
    const payOrder = ref(null); // 用来引用 pay-order 区域的 DOM 元素
    const toast = useToast(); // 获取 toast 实例
    const router = useRouter(); // 获取路由实例

    // 定义商品列表
    const items = reactive([
      {
        name: "", // 商品名称
        price: "", // 商品单价
        count: "", // 商品数量
        image: "", // 商品图片
      },
    ]);
    // 定义总价数据
    const totalPrice = computed(() => {
      let sum = 0;
      for (let i = 0; i < items.length; i++) {
        sum += items[i].price * items[i].count;
      }
      return sum;
    });

    // goBack 方法：点击阴影区域时返回上一页
    // goBack 方法：点击阴影区域时返回上一页
    const goBack = (event) => {
      window.history.back(); // 回到上一页
    };

    // 计算属性，生成订单数据
    const orderData = computed(() => {
      return {
        address,
        username,
        phone,
        items,
      };
    });

    // 提交订单的方法
    const submitOrder = async () => {
      try {
        console.log(1);
        // 获取 token (假设它存储在 sessionStorage 中)
        const token = sessionStorage.getItem("token");

        // 使用 axios 发送 POST 请求，并将 token 放入请求头中
        console.log("开始提交订单"); // 在请求之前
        const response = await axios.post(
          "http://localhost:8088/logic/user/getCart",
          {},
          {
            headers: {
              tjpu22s10: token,
            },
          }
        );
        items.splice(
          0,
          items.length,
          ...response.data.data.map((item) => ({
            count: item.count,
            price: item.price,
            name: item.name,
            image: "http://106.3.99.64:20028/tjpu22s06/" + item.image,
          }))
        );
        // 处理响应
        if (response.status === 200) {
          console.log("订单提交成功:", response.data);
        } else {
          console.log("订单提交失败:", response.data);
        }
      } catch (error) {
        // 错误处理
        console.error("提交订单失败", error);
      }
    };

    // 在页面加载完毕后调用 submitOrder
    onMounted(() => {
      submitOrder();
      console.log("组件挂载成功");
    });

    //支付订单
    const submitPayorder = async () => {
      const token = sessionStorage.getItem("token");
      const form = new FormData();
      form.append("description", date.value);
      console.log(date.value);
      try {
        // 使用 axios 向后端发送 POST 请求
        const response = await axios.post(
          "http://localhost:8088/logic/user/order",
          form,
          {
            headers: {
              tjpu22s10: token,
            },
          }
        );

        // 打印后端返回的响应
        console.log("Server response:", response.data);
        toast.success("请求已成功提交");
        setTimeout(() => {
          router.push("/user/order");
          console.log("跳转到订单页面");
        }, 1000); // 延迟 6 秒后跳转到订单页面
      } catch (error) {
        console.error("Error submitting data:", error);
        toast.error("提交失败");
      }
    };
    const selectedButton = ref(1);
    const selectButton = (buttonNumber) => {
      selectedButton.value = buttonNumber;
    };

    return {
      payOrder, // 将 payOrder 引用返回到模板
      address,
      username,
      phone,
      date,
      time,
      items,
      orderData,
      submitOrder, // 提交订单的方法
      totalPrice, // 总价数据
      goBack, // 返回上一页面的方法
      selectedButton,
      selectButton,
      submitPayorder,
    };
  },
});
</script>

<style scoped>
.pay-order-back {
  background-color: rgba(244, 1, 1, 0); /* 字体颜色 */
  color: rgb(254, 252, 252); /* 字体颜色 */
}

.pay-time.selected {
  background-color: #f7f2d1d3; /* 选中时的背景颜色 */
  color: #f16135; /* 选中时的字体颜色 */
}
/* 灰暗背景层 */
.shadowBox {
  position: fixed; /* 固定定位 */
  top: 0; /* 距离顶部为 0 */
  left: 0; /* 距离左边为 0 */
  width: 100%; /* 宽度占满整个视口 */
  height: 100%; /* 高度占满整个视口 */
  background-color: rgba(48, 49, 51, 0.5); /* 半透明灰色 */
  z-index: 2099; /* 设置较低的层级，确保其在其他内容之下 */
}

/* pay-order 样式 */
.pay-order {
  position: absolute; /* 设置为绝对定位 */
  top: 56%; /* 使它在垂直方向居中 */
  left: 50%; /* 使它在水平方向居中 */
  transform: translate(-50%, -50%); /* 通过 transform 来精确居中 */
  display: flex; /* 使用 flex 布局 */
  flex-direction: column; /* 设置列方向 */
  align-items: center; /* 居中对齐 */
  background-color: rgb(255, 255, 255); /* 背景颜色 */
  width: 45%; /* 宽度占满 70% */
  height: 100%; /* 高度占满整个视口 */
  z-index: 9999; /* 设置较高的层级，确保其在其他内容之上 */
  padding: 20px; /* 给内容加点内边距 */
  box-sizing: border-box; /* 确保宽高计算包含内边距 */
  border-radius: 20px; /* 圆角 */
}
.pay-order-header {
  display: flex; /* 使用 flex 布局 */
  flex-direction: column; /* 设置列方向 */
  margin-bottom: 10px; /* 给内容加点外边距 */
  border-radius: 20px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
  width: 95%;
  height: 25%;
  padding: 10px;
}
.pay-order-body {
  display: flex; /* 使用 flex 布局 */
  flex-direction: column; /* 设置列方向 */
  align-items: center; /* 居中对齐 */
  margin-bottom: 5px; /* 给内容加点外边距 */
  border-radius: 20px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
  width: 97%;
  height: 48%;
  overflow: auto; /* 允许内容溢出 */
}
.pay-order-item {
  display: flex; /* 使用 flex 布局 */
  margin-top: 30px; /* 给内容加点外边距 */
  width: 85%;
  height: 20%;
  justify-content: space-between;
  align-items: center;
  border-radius: 20px;
}
.pay-order-item img {
  width: 15%;
  height: 100%;
  border-radius: 20px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
}

.pay-order-item-price {
  display: grid; /* 启用网格布局 */
  align-items: center; /* 垂直居中对齐 */
  gap: 5px; /* 行间距，确保内容之间有空隙 */
  font-size: 16px; /* 设置字体大小 */
  margin-left: 48%; /* 确保整体布局 */
  width: auto; /* 根据内容宽度自适应 */
}

.pay-order-item-price span {
  text-align: left; /* 默认文本左对齐 */
}

.pay-order-item-price span:last-child {
  font-size: 22px; /* 合计部分的字体稍大 */
  font-weight: bold; /* 合计部分加粗 */
  color: #ff5722; /* 合计部分颜色 */
  text-align: right; /* 合计部分右对齐 */
}
.pay-order-price {
  display: flex;
  flex-direction: column;
  font-size: 18px;
  margin-top: 20px;
  width: 95%;
}
</style>
