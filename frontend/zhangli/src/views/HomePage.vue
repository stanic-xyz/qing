<template>
  <div class="jvm-info">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>活动管理</el-breadcrumb-item>
      <el-breadcrumb-item>活动列表</el-breadcrumb-item>
      <el-breadcrumb-item>活动详情</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="alert">
      <a type="success" :show-icon="true">
        <div slot="message">
          <span class="el-icon-info"></span>
          数据获取时间 {{this.time}}
          <el-link :underline="false" @click="create" style="margin-left: 24px">点击刷新</el-link>
        </div>
      </a>
    </div>
    <table border="2" width="100%">
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
      time: '2020年01月15日 21时50分19秒',
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
<style>

.jvm-info {
  margin: 20px;
}
.alert {
  text-align: left;
  margin: 10px;
}
</style>
