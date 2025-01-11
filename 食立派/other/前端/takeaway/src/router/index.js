import {
  createRouter,
  createWebHistory
} from 'vue-router'
import Home from '@/views/HomeViews/index.vue'
import MerchantPage from '../components/MerchantPage.vue'
import Login from '../components/Login/Login.vue'
import Rider from '../components/RiderPage.vue'
import User from '../components/UserPage.vue'
import System from '../components/SystemLayout.vue'
import Data from '../components/System/Data.vue'
import Welcome from '../components/Welcome.vue'
import Furonglou from '@/components/User/Furonglou.vue'
import Reverso from '@/views/ReversoView/ReversoView.vue'
import Contact from '@/views/ReversoView/contactUs.vue'

const router = createRouter({
    history: createWebHistory(
      import.meta.env.BASE_URL),
    routes: [{
        path: '/',
        name: 'Home',
        component: Home,
      },
      {
        path: '/merchant',
        name: 'MerchantPage',
        component: MerchantPage,
        children: [{
            path: 'information',
            name: 'StoreInformation',
            component: () =>
              import('../components/Merchant/StoreInformation.vue'),
          },
          {
            path: 'management',
            name: 'ProductsManagement',
            component: () =>
              import('../components/Merchant/ProductsManagement.vue'),
          },
          {
            path: 'orders',
            name: 'OrderProcessing',
            component: () =>
              import('../components/Merchant/OrderProcessing.vue'),
          },
          {
            path: 'data',
            name: 'AnalysisData',
            component: () =>
              import('../components/Merchant/AnalysisData.vue'),
          },
        ]

      },

      {
        path: '/login',
        name: 'Login',
        component: Login,
      },

      //骑手页面
      {
        path: '/rider',
        name: 'Rider',
        component: Rider,
        children: [{
            path: 'order',
            name: 'OrderView',
            component: () =>
              import('../components/Rider/OrderView.vue'),
          },
          {
            path: 'order',
            name: 'OrderView',
            component: () =>
              import('../components/Rider/OrderView.vue'),
          }, {
            path: 'menu',
            name: 'MenuView',
            component: () =>
              import('../components/Rider/MenuPlaza.vue'),
          },
          {
            path: 'riderinfo',
            name: 'RiderInfo',
            component: () =>
              import('../components/Rider/RiderInfo.vue'),
          },
        ]
      },
      //用户页面
      {
        path: '/user',
        name: 'User',
        component: User,
        children: [{
            path: 'order',
            name: 'OrderInquire',
            component: () =>
              import('../components/User/OrderInquire.vue'),
          },
          {
            path: 'dish',
            name: 'DishRecommand',
            component: () =>
              import('../components/User/DishRecommand.vue'),
          },
          {
            path: 'info',
            name: 'UserInfo',
            component: () =>
              import('../components/User/UserInfo.vue'),
          },

        ]
      },
      //商铺界面
      {
        path: '/furonglou',
        name: 'Furonglou',
        component: Furonglou,
        children: [{
          path: 'order',
          name: 'PayOrder',
          component: () =>
            import('../components/User/PayOrder.vue'),
        }, ]
      },
      //认证界面
      {
        path: '/merver',
        name: 'Merver',
        component: () =>
          import('../components/Certification/MerVerification.vue'),

      },
      {
        path: '/riderver',
        name: 'Riderver',
        component: () =>
          import('../components/Certification/RiderVerification.vue'),
      },
      {
        path: '/system',
        name: 'System',
        component: System,
        children: [{
          path: 'certification',
          name: 'Certification',
          component: () =>
            import('../components/System/Certification.vue'),
        }, ]
      },
      {
        path: '/data',
        name: 'Data',
        component: Data,
      },
      {
        path: '/welcome',
        name: 'Welcome',
        component: Welcome,
      },
      {
        path: '/reverso',
        name: 'Reverso',
        component: Reverso,
      },
      {
        path: '/contact',
        name: 'Contact',
        component: Contact,
      },
    ],
  }

)

export default router
