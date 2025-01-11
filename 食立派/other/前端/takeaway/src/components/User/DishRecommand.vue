<template>
  <div class="dish-recommend">
    <div class="store-list">
      <div
        class="store"
        v-for="store in stores"
        :key="store.id"
        @click="handleClick(store)"
      >
        <div class="store-header"></div>
        <!-- 菜品图片和信息 -->
        <div class="dish-item">
          <div class="dish-image-container">
            <img :src="store.images" alt="dish.name" class="dish-image" />
          </div>
          <div class="dish-info">
            <h4>{{ store.name }}</h4>
            <p>{{ store.description }}</p>
            <p>
              <strong style="color: orange">{{ store.rate }}分</strong> 月售{{
                store.sales
              }}
              人均¥{{ store.price }}
            </p>

            <div style="display: flex; gap: 200px; width: 100%">
              <span>起送¥15 免配送费</span>
              <span style="margin-left: 650px">40分钟 3.9km</span>
            </div>
            <div
              style="display: flex; gap: 5px; width: 20%; border-radius: 5px"
            >
              <p
                style="
                  border: 0.2px solid red;
                  color: red;
                  padding: 3px;
                  border-radius: 5px;
                "
              >
                新客减1
              </p>
              <p
                style="
                  border: 2px solid red;
                  color: red;
                  padding: 3px;
                  border-radius: 5px;
                "
              >
                收藏领1元券
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from "vue";
import axios from "axios";
import { onMounted } from "vue";
import MerchantLogin from "../Login/MerchantLogin.vue";

interface Store {
  id: number;
  name: string;
  rate: number;
  sales: string;
  description: string;
  images: string;
  price: number;
}

export default defineComponent({
  setup() {
    const stores = reactive<Store[]>([]);

    const fetchStores = async () => {
      const token = sessionStorage.getItem("token");
      axios
        .post("http://localhost:8088/logic/plat/getAllShop", {},{
        headers: {
          tjpu22s10: token,
        },
      })
        .then((res) => {
          if (res.data.code == 200) {
            console.log("上传成功");
            stores.splice(
              0,
              stores.length,
              ...res.data.data.map((item) => ({
                id: item.id,
                name: item.name,
                rate: item.rate,
                sales: item.sales,
                description: item.description,
                images: "http://106.3.99.64:20028/tjpu22s06/" + item.images,
                price: item.price,
              }))
            );
            console.log(res.data);
          } else {
            console.log(res.data.msg);
          }
        })
        .catch((error) => {
          console.log("上传失败,请检查网络");
        });
    };

    onMounted(() => {
      fetchStores();
    });
    return {
      stores,
    };
  },
  methods: {
    handleClick(store: Store) {
      // 使用 query 参数传递数据
      this.$router.push({
        path: "/furonglou",
        query: {
          MerchantId: store.id,
          MerchantName: store.name,
          MerchantDescription: store.description,
        },
      });
      console.log(store.id, store.name, store.description);
    },
  },
});
</script>

<style scoped>
.dish-recommend {
  padding: 0px;
  background-color: #f4f6f9; /* 背景色 */
  font-family: "Arial", sans-serif;
}

.store-list {
  display: flex;
  flex-direction: column;
  gap: 30px; /* 店铺纵向排列 */
}

.store {
  background-color: #fff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  text-align: left;
  transition: all 0.3s ease-in-out;
  cursor: pointer;
}

.store:hover {
  transform: translateY(-5px); /* 鼠标悬停时微微上浮 */
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.15);
}

.store-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.dish-item {
  display: flex;
  align-items: flex-start; /* 左对齐 */
  gap: 20px; /* 图片和文字之间的间距 */
}

.dish-image-container {
  width: 120px;
  height: 120px;
}

.dish-image {
  width: 100%;
  height: 100%;
  border-radius: 8px;
  object-fit: cover;
}

.dish-info {
  flex: 1;
}

.dish-info h4 {
  font-size: 20px;
  color: #333;
  margin-bottom: 5px;
}

.dish-info p {
  color: #555;
  margin: 5px 0;
}

.dish-info .price-info {
  display: flex;
  justify-content: space-between;
  margin: 10px 0;
}

.dish-info .discount-info {
  display: flex;
  gap: 5px;
  margin-top: 10px;
}

.dish-info .discount-info p {
  border: 1px solid red;
  color: red;
  padding: 5px;
  border-radius: 5px;
}
</style>
