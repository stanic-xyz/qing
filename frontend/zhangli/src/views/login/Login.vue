<template>
  <div>
    <el-button type="success">这是按钮</el-button>
  </div>
</template>

<script>
import request from '@/utils/request.js'

export default {
  data () {
    return {
      name: 'thisi name'
    }
  },
  components: {},
  methods: {
    test: function () {
      console.log('this is test File')
      console.log(request)
      request.get('/test', { test: 'test1' })
    }
  }
}
</script>

<style>
</style>
