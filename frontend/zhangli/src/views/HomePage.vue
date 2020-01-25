<template>
  <div class="jvm-info">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>活动管理</el-breadcrumb-item>
      <el-breadcrumb-item>活动列表</el-breadcrumb-item>
      <el-breadcrumb-item>活动详情</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="alert" border>
      <a type="success" :show-icon="true">
        <div slot="message">
          <span class="el-icon-info"></span>
          数据获取时间 {{this.time}}
          <el-link :underline="false" @click="create" style="margin-left: 24px">点击刷新</el-link>
        </div>
      </a>
    </div>
    <table>
      <tr>
        <th>参数</th>
        <th>描述</th>
        <th>当前值</th>
      </tr>
      <tr>
        <td>
          <a-tag color="purple">jvm.memory.max</a-tag>
        </td>
        <td>JVM 最大内存</td>
        <td>{{jvm.memory.max}} MB</td>
      </tr>
      <tr>
        <td>
          <a-tag color="purple">jvm.memory.committed</a-tag>
        </td>
        <td>JVM 可用内存</td>
        <td>{{jvm.memory.committed}} MB</td>
      </tr>
      <tr>
        <td>
          <a-tag color="purple">jvm.memory.used</a-tag>
        </td>
        <td>JVM 已用内存</td>
        <td>{{jvm.memory.used}} MB</td>
      </tr>
      <tr>
        <td>
          <a-tag color="cyan">jvm.buffer.memory.used</a-tag>
        </td>
        <td>JVM 缓冲区已用内存</td>
        <td>{{jvm.buffer.memory.used}} MB</td>
      </tr>
      <tr>
        <td>
          <a-tag color="cyan">jvm.buffer.count</a-tag>
        </td>
        <td>当前缓冲区数量</td>
        <td>{{jvm.buffer.count}} 个</td>
      </tr>
      <tr>
        <td>
          <a-tag color="green">jvm.threads.daemon</a-tag>
        </td>
        <td>JVM 守护线程数量</td>
        <td>{{jvm.threads.daemon}} 个</td>
      </tr>
      <tr>
        <td>
          <a-tag color="green">jvm.threads.live</a-tag>
        </td>
        <td>JVM 当前活跃线程数量</td>
        <td>{{jvm.threads.live}} 个</td>
      </tr>
      <tr>
        <td>
          <a-tag color="green">jvm.threads.peak</a-tag>
        </td>
        <td>JVM 峰值线程数量</td>
        <td>{{jvm.threads.peak}} 个</td>
      </tr>
      <tr>
        <td>
          <a-tag color="orange">jvm.classes.loaded</a-tag>
        </td>
        <td>JVM 已加载 Class 数量</td>
        <td>{{jvm.classes.loaded}} 个</td>
      </tr>
      <tr>
        <td>
          <a-tag color="orange">jvm.classes.unloaded</a-tag>
        </td>
        <td>JVM 未加载 Class 数量</td>
        <td>{{jvm.classes.unloaded}} 个</td>
      </tr>
      <tr>
        <td>
          <a-tag color="pink">jvm.gc.memory.allocated</a-tag>
        </td>
        <td>GC 时, 年轻代分配的内存空间</td>
        <td>{{jvm.gc.memory.allocated}} MB</td>
      </tr>
      <tr>
        <td>
          <a-tag color="pink">jvm.gc.memory.promoted</a-tag>
        </td>
        <td>GC 时, 老年代分配的内存空间</td>
        <td>{{jvm.gc.memory.promoted}} MB</td>
      </tr>
      <tr>
        <td>
          <a-tag color="pink">jvm.gc.max.data.size</a-tag>
        </td>
        <td>GC 时, 老年代的最大内存空间</td>
        <td>{{jvm.gc.maxDataSize}} MB</td>
      </tr>
      <tr>
        <td>
          <a-tag color="pink">jvm.gc.live.data.size</a-tag>
        </td>
        <td>FullGC 时, 老年代的内存空间</td>
        <td>{{jvm.gc.liveDataSize}} MB</td>
      </tr>
      <tr>
        <td>
          <a-tag color="blue">jvm.gc.pause.count</a-tag>
        </td>
        <td>系统启动以来GC 次数</td>
        <td>{{jvm.gc.pause.count}} 次</td>
      </tr>
      <tr>
        <td>
          <a-tag color="blue">jvm.gc.pause.totalTime</a-tag>
        </td>
        <td>系统启动以来GC 总耗时</td>
        <td>{{jvm.gc.pause.totalTime}} 秒</td>
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
      this.$notify.success({
        title: 'Info',
        message: '这是一条没有关闭按钮的消息',
        showClose: false
      })
      // this.$get('actuator', null)
    }
  }
}
</script>
<style>
.jvm-info {
  width: 100%;
}
.alert {
  text-align: left;
  margin: 10px;
}
</style>
