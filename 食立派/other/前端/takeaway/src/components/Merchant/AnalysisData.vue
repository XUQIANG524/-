<template>
  <div class="analysis-dashboard">
    <div class="charts-container">
      <div class="chart-item" id="sales-chart"></div>
      <div class="chart-item" id="age-distribution-chart"></div>
    </div>
    <div class="charts-container">
      <div class="chart-item" id="user-growth-chart"></div>
      <div class="chart-item" id="region-distribution-chart"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";

export default {
  mounted() {
    this.initCharts();
    this.resizeCharts(); // Resize charts on mount
    window.addEventListener('resize', this.resizeCharts); // Re-resize on window resize
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeCharts);
  },
  methods: {
    initCharts() {
      this.initSalesChart();
      this.initAgeDistributionChart();
      this.initUserGrowthChart();
      this.initRegionDistributionChart();
    },

    initSalesChart() {
      const chartContainer = document.getElementById("sales-chart");
      if (chartContainer) {
        const myChart = echarts.init(chartContainer);

        const option = {
          title: {
            text: "月度销售额趋势",
          },
          tooltip: {},
          xAxis: {
            type: "category",
            data: ["1月", "2月", "3月", "4月", "5月", "6月"],
          },
          yAxis: {
            type: "value",
          },
          series: [
            {
              name: "销售额",
              type: "line",
              data: [12000, 13000, 15000, 17000, 16000, 18000],
            },
          ],
        };

        myChart.setOption(option);
      }
    },

    initAgeDistributionChart() {
      const chartContainer = document.getElementById("age-distribution-chart");
      if (chartContainer) {
        const myChart = echarts.init(chartContainer);

        const option = {
          title: {
            text: "菜品销售占比",
          },
          tooltip: {},
          series: [
            {
              name: "菜品销售占比",
              type: "pie",
              radius: "50%",
              data: [
                { value: 335, name: "红烧肉" },
                { value: 250, name: "酸辣土豆丝" },
                { value: 200, name: "麻婆豆腐" },

              ],
            },
          ],
        };

        myChart.setOption(option);
      }
    },

    initUserGrowthChart() {
      const chartContainer = document.getElementById("user-growth-chart");
      if (chartContainer) {
        const myChart = echarts.init(chartContainer);

        const option = {
          title: {
            text: "月度订单量增长",
          },
          tooltip: {},
          xAxis: {
            type: "category",
            data: ["1月", "2月", "3月", "4月", "5月", "6月"],
          },
          yAxis: {
            type: "value",
          },
          series: [
            {
              name: "新增用户",
              type: "bar",
              data: [500, 600, 700, 800, 900, 1000],
            },
          ],
        };

        myChart.setOption(option);
      }
    },

    initRegionDistributionChart() {
      const chartContainer = document.getElementById("region-distribution-chart");
      if (chartContainer) {
        const myChart = echarts.init(chartContainer);

        const option = {
          title: {
            text: "顾客地域分布",
          },
          tooltip: {},
          series: [
            {
              name: "地域分布",
              type: "pie",
              radius: "50%",
              data: [
                { value: 335, name: "西青区" },
                { value: 310, name: "南开区" },
                { value: 234, name: "红桥区" },
                { value: 135, name: "河北区" },
              ],
            },
          ],
        };

        myChart.setOption(option);
      }
    },

    resizeCharts() {
      // Automatically resize each chart
      const charts = ["sales-chart", "age-distribution-chart", "user-growth-chart", "region-distribution-chart"];
      charts.forEach(chartId => {
        const chartContainer = document.getElementById(chartId);
        if (chartContainer) {
          const myChart = echarts.getInstanceByDom(chartContainer);
          if (myChart) {
            myChart.resize();
          }
        }
      });
    }
  },
};
</script>

<style scoped>
.analysis-dashboard {
  font-family: Arial, sans-serif;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fffefe;
  overflow: hidden; /* Prevent scrolling */
}

.charts-container {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.chart-item {
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  flex: 1 1 48%; /* Flex layout with 48% width for even distribution */
  margin-right: 2%; /* Space between items */
  height: 300px; /* Fixed height for each chart */
}

.chart-item:last-child {
  margin-right: 0; /* Remove right margin for the last item */
}
</style>
