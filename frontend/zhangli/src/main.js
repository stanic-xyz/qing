// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'

import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import router from './router'
import request from './utils/request'
import db from './utils/localstorage'

Vue.config.productionTip = false
Vue.use(Element, { size: 'small', zIndex: 3000 })
Vue.use(db)

Vue.prototype.$post = request.post
Vue.prototype.$get = request.get

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
