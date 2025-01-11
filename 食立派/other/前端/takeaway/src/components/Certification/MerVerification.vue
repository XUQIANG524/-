<template>
  <div class="rider-verification">
    <div class="image">
      <h2>认证即刻开启</h2>
      <p>在这里，您的餐饮故事将与更多顾客相遇</p>
    </div>
    <div class="form">
      <!-- 状态条 -->
      <el-steps style="max-width: 600px; margin: 20px auto" :active="active" finish-status="success">
        <el-step title="个人信息认证" />
        <el-step title="相关凭证认证" />
        <el-step title="完成" />
      </el-steps>

      <!-- 第一个表单：个人信息认证 -->
      <div v-if="active === 0" class="form-item">
        <el-form :model="form" label-width="auto" style="max-width: 600px; margin: 0 auto">
          <el-form-item label="商家姓名">
            <el-input v-model="form.merchantname" placeholder="在此处输入商家名" />
          </el-form-item>
          <el-form-item label="商家身份证号">
            <el-input v-model="form.idcard" placeholder="在此处输入商家身份号" />
          </el-form-item>
          <el-form-item label="店铺名">
            <el-input v-model="form.name" placeholder="在此处输入店铺名" />
          </el-form-item>

          <el-form-item label="营业时间">
            <el-col :span="11">
              <el-time-picker v-model="form.date1" placeholder="选择开始时间" style="width: 100%" format="HH:mm" />
            </el-col>
            <el-col :span="2" class="text-center">
              <span class="text-gray-500">--</span>
            </el-col>
            <el-col :span="11">
              <el-time-picker v-model="form.date2" placeholder="选择结束时间" style="width: 100%" format="HH:mm" />
            </el-col>
          </el-form-item>

          <el-form-item label="店铺类型">
            <el-checkbox-group v-model="form.type">
              <el-checkbox value="Online activities" name="type">中餐馆</el-checkbox>
              <el-checkbox value="Promotion activities" name="type">西餐馆</el-checkbox>
              <el-checkbox value="Offline activities" name="type">烧烤店</el-checkbox>
            </el-checkbox-group>
          </el-form-item>

          <el-form-item label="店铺地址">
            <el-input v-model="form.address" type="textarea" />
          </el-form-item>
          <el-form-item label="联系方式">
            <el-input v-model="form.phone" placeholder="在此处输入联系方式" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleSubmit">提交</el-button>
            <el-button>取消</el-button>
          </el-form-item>
        </el-form>
      </div>

      <div v-if="active === 1" class="form-item">
        <el-form :model="form" label-width="auto" style="max-width: 600px; margin: 0 auto">
          <!-- 这里是第二阶段的表单内容 -->
          <el-form-item label="上传身份证">
            <el-upload action="#" :http-request="submitUpload" style="width: 100px; height: 100px; margin-bottom: 40px">
              <div style="width: 100px;height: 100px; background: none; border: darkcyan dashed 1px;">
                <el-image style="width: 100px;height: 100px;" fit="fill" :src="url1" />
              </div>
            </el-upload>
          </el-form-item>

          <el-form-item label="上传营业执照">
            <el-upload action="#" :http-request="submitUpload1"
              style="width: 100px; height: 100px; margin-bottom: 40px">
              <div style="width: 100px;height: 100px; background: none; border: darkcyan dashed 1px;">
                <el-image style="width: 100px;height: 100px;" fit="fill" :src="url2" />
              </div>
            </el-upload>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="next">完成</el-button>
            <el-button @click="prev">返回</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 第三个表单：完成 -->
      <div v-if="active === 2" class="form3">
        <div class="icon">
          <el-icon style="font-size: 100px; color: #4caf50">
            <CircleCheck />
          </el-icon>
        </div>
        <p>认证完成！感谢您的提交，我们会尽快处理。</p>
        <el-button type="primary" @click="resetForm"
          style="height: 60px; width: 150px; font-size: large">返回修改</el-button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from "vue";
import axios from "axios";
import { ElMessage } from 'element-plus';

const active = ref(0);
const merchantId = ref('');
const url1 = ref()
const url2 = ref()
const submitUpload = async (f) => {
  const file = f.file
  const token = sessionStorage.getItem("token");
  const form = new FormData()
  form.append('file', file)
  axios.post("http://localhost:8088/api/merchants/merchantuploadauthentication", form,{
      headers: {
        "tjpu22s10": token,
      },
    }).then(
    (res) => {
      if (res.data.code == 200) {
        console.log('上传成功');
        url1.value = 'http://106.3.99.64:20028/tjpu22s06/' + res.data.msg
      } else {
        console.log(res.data.msg);
      }

    },
    (error) => {
      console.log('上传失败,请检查网络');
    }

  )
}
const submitUpload1 = async (f) => {
  const file = f.file
  const token = sessionStorage.getItem("token");
  const form = new FormData()
  form.append('file', file)
  axios.post("http://localhost:8088/api/merchants/merchantuploadhealth", form,{
      headers: {
        "tjpu22s10": token,
      },
    }).then(
    (res) => {
      if (res.data.code == 200) {
        console.log('上传成功');
        url2.value = 'http://106.3.99.64:20028/tjpu22s06/' + res.data.msg
      } else {
        console.log(res.data.msg);
      }

    },
    (error) => {
      console.log('上传失败,请检查网络');
    }

  )
}
const form = reactive({
  merchantname: "",
  name: "",
  region: "",
  date1: "",
  date2: "",
  delivery: false,
  type: [],
  phone: "",
  credentials: "",
  address: "",
  idcard: "",
});
const next = () => {
  if (active.value < 2) active.value++;
};

const prev = () => {
  if (active.value > 0) active.value--;
};
// 修改为直接从 sessionStorage 获取商家ID
onMounted(() => {
  const token = sessionStorage.getItem("token");
  console.log('当前保存的 token:', token);
  // 检查其他相关信息
  console.log('当前保存的信息:', {
    role: sessionStorage.getItem("role"),
    username: sessionStorage.getItem("username"),
    merchantId: sessionStorage.getItem("merchantId")
  });
});

const handleSubmit = async () => {
  try {
    const token = sessionStorage.getItem("token");
    console.log('准备发送请求，token:', token);

    if (!token) {
      ElMessage.error('用户未登录或缺少Token');
      return;
    }

    console.log('表单数据:', form);
    // 表单验证
    if (!form.merchantname || !form.idcard || !form.name || !form.address || !form.type.length) {
      ElMessage.warning('请填写完整的商家信息！');
      return;
    }
    if (!/^\d{15}|\d{18}$/.test(form.idcard)) { // 验证身份证格式
      ElMessage.warning('身份证号码格式不正确！');
      return;
    }
    if (form.merchantname.length < 2 || form.merchantname.length > 20) { // 验证姓名长度
      ElMessage.warning('姓名长度应在2到20个字符之间！');
      return;
    }

    const requestData = {
      merchantName: form.merchantname,
      idNum: form.idcard,
      shopName: form.name
    };

    console.log('发送的数据:', requestData);
    let merchantId = sessionStorage.getItem("merchantId");
    console.log('请求前的商家ID:', merchantId);
    const response = await axios({
      method: 'post',
      url: `http://localhost:8088/api/merchants/${merchantId}`,
      data: requestData,
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      }
    });
    console.log('响应结果:', response);

    // 处理响应
    if (response.status === 200) {
      ElMessage.success(response.data.msg || '提交成功！');
      next();
    } else {
      console.error('提交失败，响应数据:', response.data);
      ElMessage.error(response.data?.message || '提交失败，请检查数据是否正确');
    }
  } catch (error: any) {
    console.error('请求失败:', error);
    if (error.response?.data) {
      console.error('错误详情:', error.response.data);
    }
    ElMessage.error('提交失败，请稍后重试');
  }
};

const resetForm = () => {
  active.value = 0;
  Object.keys(form).forEach(key => {
    if (Array.isArray(form[key])) {
      form[key] = [];
    } else if (typeof form[key] === 'boolean') {
      form[key] = false;
    } else {
      form[key] = "";
    }
  });
};

const handlePreview = (file: any) => {
  console.log("Preview file:", file);
};
</script>
<style scoped>
.rider-verification {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  box-sizing: border-box;
  background-color: #f9f9f9;
  padding: 20px;
  position: relative;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  z-index: 10;
}

.image {
  width: 25%;
  text-align: center;
  background-image: url("/images/merchant.jpg");
  background-position: center;
  background-repeat: no-repeat;
  background-size: cover;
  height: 80%;
  border-radius: 15px 0 0 15px;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
}

.image h2 {
  font-size: 42px;
  color: white;
  margin-bottom: 20px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.image p {
  font-size: 18px;
  color: white;
  margin-bottom: 20px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
  margin-right: 10px;
}

.form {
  width: 50%;
  display: flex;
  flex-direction: column;
  height: 80%;
  background-color: white;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  border-radius: 0 15px 15px 0;
}

.form-item {
  margin-top: 20px;
}

.el-steps {
  width: 100%;
  max-width: 900px;
  margin: 0 auto;
  margin-bottom: 30px;
}

/*别动突然就好了*/
.el-icon--upload {
  font-size: 20px;
  color: #909399;
  margin-right: 10px;
}

/*第三个界面*/
.form3 {
  display: flex;
  flex-direction: column;
  /* 垂直排列 */
  justify-content: center;
  /* 垂直居中 */
  align-items: center;
  /* 水平居中 */
  height: 100%;
  /* 父容器的高度需要为100%，才能使其内容居中 */
  text-align: center;
  /* 确保文本内容居中对齐 */
}

.icon {
  width: 100px;
  height: 100px;
  display: flex;
  justify-content: center;
  /* 水平居中 */
  align-items: center;
  /* 垂直居中 */
  margin-bottom: 40px;
  /* 可以添加一些间距，确保图标与其他内容之间不太紧凑 */
}

p {
  margin-bottom: 60px;
  /* 控制段落与按钮之间的间距 */
  font-size: 30px;
}
</style>
