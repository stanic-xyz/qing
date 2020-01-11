<template>
  <div class="jvm-info">
    <div class="alert">
      <a type="success" :show-icon="true">
        <div slot="message">
          数据获取时间 {{this.time}}
          <a style="margin-left: 24px" @click="create">点击刷新</a>
        </div>
      </a>
    </div>
    <table>
      <tr>
        <th>参数</th>
        <th>描述</th>
        <th>当前值</th>
      </tr>
    </table>
  </div>
</template>

<script>
export default {
  data () {
    return {
      activeIndex: '1',
      activeIndex2: '1',
      time: '',
      loading: true,
      jvm: {
        memory: {
          max: 0,
          committed: 0,
          used: 0
        },
        buffer: {
          memory: {
            used: 0
          },
          count: 0
        },
        threads: {
          daemon: 0,
          live: 0,
          peak: 0
        },
        classes: {
          loaded: 0,
          unloaded: 0
        },
        gc: {
          memory: {
            allocated: 0,
            promoted: 0
          },
          maxDataSize: 0,
          liveDataSize: 0,
          pause: {
            totalTime: 0,
            count: 0
          }
        }
      }
    }
  },
  mounted () {
    this.create()
  },
  methods: {
    handleSelect (key, keyPath) {
      console.log(key, keyPath)
    },
    create () {
      console.log('this is test')
      this.$get('actuator', null)
    }
  }
}
</script>
