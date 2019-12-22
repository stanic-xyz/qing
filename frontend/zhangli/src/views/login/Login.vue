<template>
  <div>
这里是默认登陆页面内
{{name}}
<Button @click="test">这是button</Button>
  </div>
</template>

<script>
import { Button } from 'vant'
import request from '@/utils/request.js'

export default {
  data () {
    return {
      name: 'thisi name'
    }
  },
  components: {
    Button
  },
  methods: {
    test: function () {
      console.log('this is test File')
      console.log(request)
      request.get('/test', {'test': 'test1'})
    }

  }
}
</script>

<style>

</style>
