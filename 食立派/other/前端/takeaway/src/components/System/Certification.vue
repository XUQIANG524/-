<template>
  <div class="certification-page">
    <el-container>
      <div class="certification-container">
        <!-- 商家认证列 -->
        <div class="merchant-column">
          <h2>商家认证</h2>
          <div
            class="certification-item"
            v-for="merchant in merchants"
            :key="merchant.id"
          >
            <div class="certification-info">
              <h4>认证号：{{ merchant.id }}</h4>
              <p><strong>商家名称：</strong>{{ merchant.name }}</p>
              <p><strong>商家地址：</strong>{{ merchant.address }}</p>
              <p><strong>认证时间：</strong>{{ merchant.certificationTime }}</p>
              <p><strong>认证状态：</strong>{{ merchant.certification === "TRUE" ? '已认证' : '未认证' }}</p>
            </div>
            <button class="view-details-btn" @click="viewDetails(merchant, 'merchant')">
              查看详情
            </button>
          </div>
        </div>

        <div class="rider-column">
          <h2>骑手认证</h2>
          <div class="certification-item" v-for="rider in riders" :key="rider.id">
            <div class="certification-info">
              <h4>认证号：{{ rider.id }}</h4>
              <p><strong>骑手姓名：</strong>{{ rider.name }}</p>
              <p><strong>骑手地址：</strong>{{ rider.address }}</p>
              <p><strong>认证时间：</strong>{{ rider.certificationTime }}</p>
              <p><strong>认证状态：</strong>{{ rider.certification === "TRUE" ? '已认证' : '未认证' }}</p>
            </div>
            <button class="view-details-btn" @click="viewDetails(rider, 'rider')">
              查看详情
            </button>
          </div>
        </div>
        <!-- 认证详情模态框 -->
        <el-dialog
          v-model="isModalVisible"
          :title="'认证详细信息'"
          width="60%"
          :before-close="closeModal"
          class="certification-dialog"
        >
          <div class="modal-content" v-if="selectedCertification">
            <p><strong>认证号：</strong>{{ selectedCertification.id }}</p>
            <p v-if="selectedCertification.type === 'rider'">
              <strong>骑手姓名：</strong>{{ selectedCertification.name }}
            </p>
            <p v-else>
              <strong>商家名称：</strong>{{ selectedCertification.name }}
            </p>
            <p><strong>地址：</strong>{{ selectedCertification.address }}</p>
            <p><strong>认证时间：</strong>{{ selectedCertification.certificationTime }}</p>

            <div class="certification-images" v-if="selectedCertification.certificationImages.length">
              <p><strong>相关凭证：</strong></p>
              <div class="images-container">
                <el-image
                  v-for="(image, index) in selectedCertification.certificationImages"
                  :key="index"
                  :src="image"
                  :preview-src-list="selectedCertification.certificationImages"
                  :initial-index="index"
                  fit="cover"
                  class="cert-image"
                >
                  <template #error>
                    <div class="image-error">
                      <el-icon><picture-filled /></el-icon>
                      <span>加载失败</span>
                    </div>
                  </template>
                </el-image>
              </div>
            </div>

            <div class="certification-status">
              <p><strong>认证状态：</strong>{{ selectedCertification.certification === "TRUE" ? '已认证' : '未认证' }}</p>
              <div class="action-buttons">
                <el-button
                  type="success"
                  @click="handleCertificationUpdate(true)"
                  :loading="updating"
                >
                  通过认证
                </el-button>
                <el-button
                  type="danger"
                  @click="handleCertificationUpdate(false)"
                  :loading="updating"
                >
                  拒绝认证
                </el-button>
              </div>
            </div>
          </div>
        </el-dialog>
      </div>
    </el-container>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref, onMounted } from "vue";
import { ElMessage } from 'element-plus';
import axios from "axios";
import { PictureFilled } from '@element-plus/icons-vue';

interface Certification {
  id: number;
  name: string;
  address: string;
  certificationTime: string;
  certificationImages: string[];
  approved: boolean;
  type: string;
  certification: string;
}

export default defineComponent({
  name: "CertificationPage",
  components: {
    PictureFilled,
  },
  setup() {
    const riders = reactive<Certification[]>([]);
    const merchants = reactive<Certification[]>([]);
    const isModalVisible = ref(false);
    const selectedCertification = ref<Certification | null>(null);
    const updating = ref(false);

    const fetchRiders = async () => {
      try {
        const res = await axios.post("http://localhost:8088/logic/plat/getAllWorker");
        if (res.data.code === 200) {
          riders.splice(
            0,
            riders.length,
            ...res.data.data.map((item: any) => ({
              ...item,
              type: "rider",
              certificationImages: (item.certificationImages || []).map(
                (image: string) => `http://106.3.99.64:20028/tjpu22s06/${image}`
              ),
            }))
          );
        }
      } catch (error) {
        console.error("获取骑手信息失败", error);
        ElMessage.error('获取骑手信息失败');
      }
    };

    const fetchMerchants = async () => {
      try {
        const res = await axios.post("http://localhost:8088/logic/plat/getAllMerchant");
        if (res.data.code === 200) {
          merchants.splice(
            0,
            merchants.length,
            ...res.data.data.map((item: any) => ({
              ...item,
              type: "merchant",
              certificationImages: (item.certificationImages || []).map(
                (image: string) => `http://106.3.99.64:20028/tjpu22s06/${image}`
              ),
            }))
          );
        }
      } catch (error) {
        console.error("获取商家信息失败", error);
        ElMessage.error('获取商家信息失败');
      }
    };

    const viewDetails = (item: Certification, type: string) => {
      selectedCertification.value = { ...item, type };
      isModalVisible.value = true;
    };
    const handleCertificationUpdate = async (approved: boolean) => {
      if (!selectedCertification.value) return;

      updating.value = true;
      try {
        const endpoint = selectedCertification.value.type === 'rider'
          ? `/api/workers/certification/${selectedCertification.value.id}`
          : `http://localhost:8088/api/merchants/${selectedCertification.value.id}/certification`;

        const response = await axios.put(endpoint, null, {
          params: { approved },
        });

        if (response.status === 200) {
          ElMessage.success(`认证${approved ? '通过' : '拒绝'}成功`);
          if (selectedCertification.value) {
            // 同时更新 approved 和 certification 字段
            selectedCertification.value.approved = approved;
            selectedCertification.value.certification = approved ? "TRUE" : "FALSE";

            // 更新列表中对应项的状态
            if (selectedCertification.value.type === 'merchant') {
              const merchantIndex = merchants.findIndex(m => m.id === selectedCertification.value?.id);
              if (merchantIndex !== -1) {
                merchants[merchantIndex].approved = approved;
                merchants[merchantIndex].certification = approved ? "TRUE" : "FALSE";
              }
            } else {
              const riderIndex = riders.findIndex(r => r.id === selectedCertification.value?.id);
              if (riderIndex !== -1) {
                riders[riderIndex].approved = approved;
                riders[riderIndex].certification = approved ? "TRUE" : "FALSE";
              }
            }
          }
        }
      } catch (error) {
        console.error("更新认证状态失败", error);
        ElMessage.error('更新认证状态失败');
      } finally {
        updating.value = false;
      }
    };
    const closeModal = () => {
      isModalVisible.value = false;
      selectedCertification.value = null;
    };

    onMounted(() => {
      fetchRiders();
      fetchMerchants();
    });

    return {
      riders,
      merchants,
      isModalVisible,
      selectedCertification,
      updating,
      viewDetails,
      closeModal,
      handleCertificationUpdate,
    };
  },
});
</script>

<style scoped>
.certification-page {
  background-color: #f5f5f5;
  min-height: 100vh;
  padding: 20px;
}

.certification-container {
  display: flex;
  justify-content: space-between;
  gap: 30px;
  margin-top: 20px;
  width: 100%;
}

.rider-column,
.merchant-column {
  width: 48%;
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.certification-item {
  background-color: #f8f9fa;
  padding: 15px;
  margin-bottom: 15px;
  border-radius: 5px;
  transition: all 0.3s ease;
}

.certification-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.certification-info h4 {
  margin: 0 0 10px 0;
  color: #333;
}

.certification-info p {
  margin: 5px 0;
  color: #666;
}

.view-details-btn {
  background-color: #409EFF;
  color: #fff;
  border: none;
  padding: 8px 16px;
  font-size: 14px;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s ease;
}

.view-details-btn:hover {
  background-color: #66b1ff;
}

.certification-dialog :deep(.el-dialog__body) {
  max-height: calc(90vh - 150px);
  overflow-y: auto;
}

.modal-content {
  padding: 20px;
}

.certification-images {
  margin-top: 20px;
}

.images-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 10px;
  margin-top: 10px;
}

.cert-image {
  width: 150px;
  height: 100px;
  border-radius: 4px;
  overflow: hidden;
}

.image-error {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  background-color: #f5f7fa;
  color: #909399;
}

.certification-status {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.action-buttons {
  display: flex;
  gap: 10px;
  margin-top: 15px;
}

/* 确保弹窗内的滚动条样式统一 */
:deep(.el-dialog__body)::-webkit-scrollbar {
  width: 6px;
}

:deep(.el-dialog__body)::-webkit-scrollbar-thumb {
  background-color: #909399;
  border-radius: 3px;
}

:deep(.el-dialog__body)::-webkit-scrollbar-track {
  background-color: #f5f7fa;
}
</style>
